package panels;

import frames.Game;
import shapes.ContentRectangle;
import utils.GameConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BoardHolderGame extends JPanel {
    public boolean canMove = false;
    private final int W = 1650, H = 770;
    private final int boxW = W / 22, boxH = H / 11;
    private BufferedImage image;
    private Graphics2D graphics;
    private Game game; // parent
    private List<ContentRectangle> shapes = new ArrayList<ContentRectangle>();
    private int[][] board;
    private int[][] opponentBoard = new int[10][10];

    public BoardHolderGame(Game game) {
        this.game = game;
        createOffscreenImage();
        init();
    }

    private void createOffscreenImage() {
        image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, W, H);
    }

    private void init() {
        setPreferredSize(new Dimension(W, H));
        setBorder(BorderFactory.createEtchedBorder());

        Font font = new Font("Serif", Font.PLAIN, 20);
        graphics.setFont(font);

        board = GameConnection.getBoard();

        shapes.add(new ContentRectangle(0, 0, boxW, boxH, ContentRectangle.CellType.BLANK, 0));
        for (int j = 1; j < 11; j++) {
            shapes.add(new ContentRectangle(j * boxW, 0, boxW, boxH, ContentRectangle.CellType.LETTER, j - 1));
        }

        shapes.add(new ContentRectangle(11 * boxW, 0, boxW, boxH, ContentRectangle.CellType.BLANK, 0));
        for (int j = 12; j < 22; j++) {
            shapes.add(new ContentRectangle(j * boxW, 0, boxW, boxH, ContentRectangle.CellType.LETTER, j - 12));
        }

        for (int i = 1; i < 11; i++) {
            shapes.add(new ContentRectangle(0, i * boxH, boxW, boxH, ContentRectangle.CellType.DIGIT, i - 1));
            for (int j = 1; j < 11; j++) {
                var rect = new ContentRectangle(j * boxW, i * boxH, boxW, boxH, ContentRectangle.CellType.CELL, board[i - 1][j - 1]);
                shapes.add(rect);
            }

            shapes.add(new ContentRectangle(11 * boxW, i * boxH, boxW, boxH, ContentRectangle.CellType.DIGIT, i - 1));
            for (int j = 12; j < 22; j++) {
                var rect = new ContentRectangle(j * boxW, i * boxH, boxW, boxH, ContentRectangle.CellType.CELL, 0);
                shapes.add(rect);
            }
        }

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!canMove) return;
                for (var rect : shapes) {
                    if (rect.getType().equals(ContentRectangle.CellType.CELL) && rect.contains(e.getX(), e.getY()) && rect.getContent() == 0) {
                        var col = rect.x / rect.width;
                        var line = rect.y / rect.height;
                        if (col < 12) return;

                        col -= 12;
                        line--;
                        opponentBoard[line][col] = 1;
                        canMove = false;
                        updateGraphics();
                        repaint();

                        //TODO: send the move to the server
                        String move = "" + (char) (col + 'A') + (char) (line + '0');
                        System.out.println(move);
                        GameConnection.getOutputStream().println(move);
                        break;
                    }
                }
            }
        });
    }

    public void hit(String move) {
        opponentBoard[move.charAt(1) - '0'][move.charAt(0) - 'A'] = 2;
        updateGraphics();
        repaint();
    }

    public void miss(String move) {
        opponentBoard[move.charAt(1) - '0'][move.charAt(0) - 'A'] = 3;
        updateGraphics();
        repaint();
    }

    public void updateBoard(String move) {
        if (board[move.charAt(1) - '0'][move.charAt(0) - 'A'] == 1) {
            board[move.charAt(1) - '0'][move.charAt(0) - 'A'] = 2;
        } else {
            board[move.charAt(1) - '0'][move.charAt(0) - 'A'] = 3;
        }
        updateGraphics();
        repaint();
    }

    private void updateGraphics() {
        for (var rect : shapes) {
            if (!rect.getType().equals(ContentRectangle.CellType.CELL)) continue;
            var col = rect.x / rect.width;
            var line = rect.y / rect.height - 1;

            if (col >= 12) {
                col -= 12;
                rect.setContent(opponentBoard[line][col]);
            } else {
                col--;
                rect.setContent(board[line][col]);
            }
        }
    }

    private void drawRect(ContentRectangle r) {
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(1));
        graphics.drawRect(r.x, r.y, boxW, boxH);
        if (r.getType().equals(ContentRectangle.CellType.LETTER)) {
            char c = (char) (r.getContent() + 'A');
            graphics.drawString(String.valueOf(c), r.x + r.width / 2, r.y + r.height / 2);
        }
        else if (r.getType().equals(ContentRectangle.CellType.DIGIT)) {
            graphics.drawString(String.valueOf(r.getContent()), r.x + r.width / 2, r.y + r.height / 2);
        }
        else if (r.getType().equals(ContentRectangle.CellType.CELL)) {
            if (r.getContent() == 1) {
                graphics.fillRect(r.x + 5, r.y + 5, r.width - 10, r.height - 10);
            } else if (r.getContent() == 2) {
                graphics.setStroke(new BasicStroke(5));
                graphics.drawLine(r.x + 10, r.y + 10, r.x + r.width - 10, r.y + r.height - 10);
                graphics.drawLine(r.x + 10, r.y + r.height - 10, r.x + r.width - 10, r.y + 10);
                graphics.setStroke(new BasicStroke(1));
            } else if (r.getContent() == 3) {
                graphics.setColor(Color.red);
                graphics.setStroke(new BasicStroke(5));
                graphics.drawLine(r.x + 10, r.y + 10, r.x + r.width - 10, r.y + r.height - 10);
                graphics.drawLine(r.x + 10, r.y + r.height - 10, r.x + r.width - 10, r.y + 10);
                graphics.setColor(Color.black);
                graphics.setStroke(new BasicStroke(1));
            }
        } else if (r.getType().equals(ContentRectangle.CellType.BLANK)) {
            if (r.x > 0) { // opponent
                graphics.drawString(GameConnection.getOpponentName() + "'s", r.x + 10, r.y + 25);
            } else { // me
                graphics.drawString(GameConnection.getName() + "'s", r.x + 10, r.y + 25);
            }
            graphics.drawString("board", r.x + 10, r.y + 50);
        }
    }

    @Override
    public void update(Graphics g) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, W, H);
        for (var shape : shapes) {
            drawRect(shape);
        }
        repaint();
        g.drawImage(image, 0, 0, this);
    }
}
