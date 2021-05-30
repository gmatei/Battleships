package frames;

import manager.WindowManager;
import panels.BoardHolder;
import panels.LoggerHolder;
import utils.GameConnection;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

import static java.awt.BorderLayout.*;

public class ShipPlacement extends JFrame {

    private String mode = "true";
    private String opponentName;
    private final WindowManager manager;
    public LoggerHolder loggerHolder = new LoggerHolder(new JLabel("Place your boats!"));
    private BoardHolder boardHolder = new BoardHolder(this);
    private JPanel auxiliaryHolder = new JPanel();

    public ShipPlacement(WindowManager manager) {
        super("Ship placement");
        this.manager = manager;
        init();
        setLocationRelativeTo(null);
    }

    private void init() {
        var resetBtn = new JButton("RESET");
        resetBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                boardHolder.resetShips();
            }
        });
        auxiliaryHolder.add(resetBtn);

        var playBtn = new JButton("PLAY");
        playBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manager.setShipPlacementVisibility(false);
//                manager.setMenuVisibility(true);

                // TODO: establish the connection between the client and server here,
                //       then start a new game with the socket,
                if (!GameConnection.isActive()) {
                    try {
                        GameConnection.connect(manager.getName(), mode);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                GameConnection.sendBoard(boardHolder.getBoard()); // send the board to the server
                manager.setGame(new Game(manager));
                manager.setGameVisibility(true);
            }
        });
        auxiliaryHolder.add(playBtn);

        add(loggerHolder, NORTH);
        add(boardHolder, CENTER);
        add(auxiliaryHolder, SOUTH);
        pack();
    }
}
