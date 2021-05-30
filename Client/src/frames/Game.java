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
    boolean gameEnded = false;

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

        add(previousMessage, NORTH);
        add(currentMessage, CENTER);

        var southPanel = new JPanel();
        southPanel.add(boardHolder);

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
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
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

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * <p>
         * Note that this method is executed only once.
         *
         * <p>
         * Note: this method is executed in a background thread.
         *
         * @return the computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        protected Object doInBackground() throws Exception {
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
                                updateLoggers("Congrats, you won! Care for a rematch?");
                            } else if (argument.equals("Loser")) {
                                updateLoggers("Sorry, you lost! Care for a rematch?");
                            }
                            gameEnded = true;
                        }
                        case "REMATCH" -> {
                            // rematch -> a new match will begin
                            updateLoggers("Prepare for a new match to begin shortly...");
                            running = false;
                            manager.setGameVisibility(false);
                            manager.setShipPlacementVisibility(true);
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
