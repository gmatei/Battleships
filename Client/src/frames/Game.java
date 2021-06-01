package frames;

import manager.WindowManager;
import panels.BoardHolderGame;
import panels.LoggerHolder;
import utils.GameConnection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static java.awt.BorderLayout.*;

public class Game extends JFrame {

    private WindowManager manager;
    public LoggerHolder previousMessage = new LoggerHolder(new JLabel());
    public LoggerHolder currentMessage = new LoggerHolder(new JLabel());
    private BoardHolderGame boardHolder = new BoardHolderGame(this);
    private PrintWriter out;
    private BufferedReader in;
    private boolean gameEnded = false;

    public Game(WindowManager manager) {
        super(GameConnection.getName() + " V.S. " + GameConnection.getOpponentName());
        this.manager = manager;
        init();
        setLocationRelativeTo(null);
    }

    public void init() {
        out = GameConnection.getOutputStream();
        in = GameConnection.getInputStream();
        previousMessage.getLogger().setText("...");
        currentMessage.getLogger().setText("Awaiting game start...");
        currentMessage.getLogger().setFont(new Font("Dialog", Font.BOLD, 20));
        currentMessage.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));

        var names = new JPanel(new GridLayout(1, 2));
        var name1 = new JLabel(GameConnection.getName() + "'s board");
        name1.setFont(new Font("Dialog", Font.BOLD, 14));
        name1.setHorizontalAlignment(JLabel.CENTER);
        var name2 = new JLabel(GameConnection.getOpponentName() + "'s board");
        name2.setFont(new Font("Dialog", Font.BOLD, 14));
        name2.setHorizontalAlignment(JLabel.CENTER);
        names.add(name1);
        names.add(name2);


        var northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));
        northPanel.add(previousMessage);
        northPanel.add(currentMessage);
        northPanel.add(names);
        add(northPanel, NORTH);
        add(boardHolder, CENTER);

        var southPanel = new JPanel();
        var yesBtn = new JButton("YES");
        yesBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (gameEnded) {
                    out.println("YES");
                }
            }
        });
        southPanel.add(yesBtn);

        var noBtn = new JButton("NO");
        noBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (gameEnded) {
                    out.println("NO");
                }
            }
        });
        southPanel.add(noBtn);
        add(southPanel, SOUTH);

        pack();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
                }
                new BackgroundWorker().start();
            }
        });
    }

    public class BackgroundWorker extends SwingWorker<Object, Object> {

        public BackgroundWorker() {
        }

        public void start() {
            execute();
        }

        @Override
        protected Object doInBackground() {
            try {
                updateLoggers(in.readLine()); // starting game...
                boolean running;
                do {
                    running = true;
                    String serverMsg = in.readLine();
                    var tokens = serverMsg.split("_", 2);
                    String command = tokens[0];
                    String argument = tokens[1];

                    switch (command) {
                        case "MOVE" -> {
                            // move -> choose a new place to strike
                            updateLoggers(argument);
                            boardHolder.canMove = true;
                        }
                        case "UPDATE" -> {
                            // update -> update your board with the specified position (it was not your turn last)
                            boardHolder.updateBoard(argument);
                        }
                        case "HIT" -> {
                            // hit -> update your board with the specified position (it was your turn last)
                            updateLoggers("You hit!");
                            boardHolder.hit(argument);
                        }
                        case "MISS" -> {
                            // miss -> update your board with the specified position (it was your turn last)
                            updateLoggers("You missed...");
                            boardHolder.miss(argument);
                        }
                        case "STOP" -> {
                            // stop -> display a custom message depending on whether you won or lost & send YES/NO back to the server
                            if (argument.equals("Winner")) {
                                updateLoggers("Congrats, you won! Care for a rematch? (Choose by pressing the buttons below)");
                            } else if (argument.equals("Loser")) {
                                updateLoggers("Sorry, you lost! Care for a rematch? (Choose by pressing the buttons below)");
                            }
                            gameEnded = true;
                        }
                        case "REMATCH" -> {
                            // rematch -> a new match will begin
                            updateLoggers("Prepare for a new match to begin shortly...");
                            running = false;
                            manager.setGameVisibility(false);
                            manager.setShipPlacementVisibility(true);
                            manager.shipPlacement.boardHolder.resetShips();
                        }
                        case "END" -> {
                            // end -> the game will end; display the message from the server
                            updateLoggers(argument);
                            GameConnection.disconnect();
                            running = false;
                            manager.setGameVisibility(false);
                            manager.setMenuVisibility(true);
                        }
                        default -> {
                            // not good if we arrived here - no command matches our switch case
                            // TODO: implement custom exception or sth
                            System.out.println("[CLIENT] Error: bad command received from server. Exiting...");
                            System.exit(-1);
                        }
                    }
                } while (running);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void done() {
        }
    }

    private void updateLoggers(String currMsg) {
        previousMessage.getLogger().setText(currentMessage.getLogger().getText()); // awaiting game start...
        currentMessage.getLogger().setText(currMsg); // starting game...
    }
}
