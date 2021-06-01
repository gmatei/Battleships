package panels;

import frames.ShipPlacement;
import shapes.ContentRectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardHolder extends JPanel {
    private final int W = 1100, H = 770;
    private final int boxW = W / 11, boxH = H / 11;
    private BufferedImage image;
    private Graphics2D graphics;
    private ShipPlacement shipPlacement; // parent
    private List<ContentRectangle> shapes = new ArrayList<ContentRectangle>();
    private Map<String, Integer> ships = new HashMap<>();
    private String currentShip;
    private int[][] board;

    public BoardHolder(ShipPlacement shipPlacement) {
        this.shipPlacement = shipPlacement;
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

        Font font = new Font("Dialog", Font.PLAIN, 20);
        graphics.setFont(font);

        board = new int[10][10];

        shapes.add(new ContentRectangle(0, 0, boxW, boxH, ContentRectangle.CellType.BLANK, 0));
        for (int j = 1; j < 11; j++) {
            shapes.add(new ContentRectangle(j * boxW, 0, boxW, boxH, ContentRectangle.CellType.LETTER, j - 1));
        }
        for (int i = 1; i < 11; i++) {
            shapes.add(new ContentRectangle(0, i * boxH, boxW, boxH, ContentRectangle.CellType.DIGIT, i - 1));
            for (int j = 1; j < 11; j++) {
                var rect = new ContentRectangle(j * boxW, i * boxH, boxW, boxH, ContentRectangle.CellType.CELL, 0);
                shapes.add(rect);
            }
        }

        ships.put("Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Cruiser", 3);
        ships.put("Submarine", 3);
        ships.put("Destroyer", 2);
        currentShip = "Carrier";
        shipPlacement.loggerHolder.getLogger().setText("Place your " + currentShip + "! (size: " + ships.get(currentShip) + ")");

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (ships.isEmpty()) return;
                for (var rect : shapes) {
                    if (rect.getType().equals(ContentRectangle.CellType.CELL) && rect.contains(e.getX(), e.getY()) && rect.getContent() == 0) {
                        var col = rect.x / rect.width - 1;
                        var line = rect.y / rect.height - 1;

                        int placementResult = 3;
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            placementResult = placeShip(line, col, "H", currentShip);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            placementResult = placeShip(line, col, "V", currentShip);
                        }

                        if (placementResult == 0) { // good
                            updateGraphics();
                            repaint();
                            ships.remove(currentShip);
                            if (ships.isEmpty()) {
                                shipPlacement.loggerHolder.getLogger().setText("Ships placed! You can now start the game.");
                            } else {
                                currentShip = ships.keySet().iterator().next();
                                shipPlacement.loggerHolder.getLogger().setText("Place your " + currentShip + "! (size: " + ships.get(currentShip) + ")");
                            }
                        } else if (placementResult == 1) { // out of bounds
                            shipPlacement.loggerHolder.getLogger().setText("Ships must not go out of bounds!");
                        } else if (placementResult == 2) { // adjacent/overlapping
                            shipPlacement.loggerHolder.getLogger().setText("Ships must not intercept/overlap!");
                        } else { // neither LMB nor RMB were clicked
                            shipPlacement.loggerHolder.getLogger().setText("Use the LMB to place the ship horizontally, and the RMB to place it vertically.");
                        }
                        break;
                    }
                }
            }
        });
    }

    public int[][] getBoard() {
        return board;
    }

    public void resetShips() {
        ships.put("Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Cruiser", 3);
        ships.put("Submarine", 3);
        ships.put("Destroyer", 2);
        currentShip = "Carrier";
        shipPlacement.loggerHolder.getLogger().setText("Place your " + currentShip + "! (size: " + ships.get(currentShip) + ")");
        board = new int[10][10];
        updateGraphics();
        repaint();
    }

    private void updateGraphics() {
        for (var rect : shapes) {
            var col = rect.x / rect.width - 1;
            var line = rect.y / rect.height - 1;
            if (line >= 0 && col >= 0) {
                rect.setContent(board[line][col]);
            }
        }
    }

    private int placeShip(int line, int col, String orientation, String currentShip) {
        if (orientation.equals("H")) {
            if (col + ships.get(currentShip) > 10) {
                return 1;
            }

            boolean ok = true;
            for (int j = col; j < col + ships.get(currentShip); j++) {
                if (board[line][j] != 0 ||
                        (line + 1 <= 9 && board[line + 1][j] != 0) ||
                        (line - 1 >= 0 && board[line - 1][j] != 0) ||
                        (j + 1 <= 9 && board[line][j + 1] != 0) ||
                        (j - 1 >= 0 && board[line][j - 1] != 0)) {
                    ok = false;
                    break;
                }
            }

            if (!ok) {
                return 2;
            }

            for (int j = col; j < col + ships.get(currentShip); j++) {
                board[line][j] = 1;
            }
        } else {
            if (line + ships.get(currentShip) > 10) {
                return 1;
            }

            boolean ok = true;
            for (int i = line; i < line + ships.get(currentShip); i++) {
                if (board[i][col] != 0 ||
                        (i + 1 <= 9 && board[i + 1][col] != 0) ||
                        (i - 1 >= 0 && board[i - 1][col] != 0) ||
                        (col + 1 <= 9 && board[i][col + 1] != 0) ||
                        (col - 1 >= 0 && board[i][col - 1] != 0)) {
                    ok = false;
                    break;
                }
            }

            if (!ok) {
                return 2;
            }

            for (int i = line; i < line + ships.get(currentShip); i++) {
                board[i][col] = 1;
            }
        }
        return 0;
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
        else if (r.getType().equals(ContentRectangle.CellType.CELL) && r.getContent() == 1) {
            graphics.fillRect(r.x + 5, r.y + 5, r.width - 10, r.height - 10);
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
