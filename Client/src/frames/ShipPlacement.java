package frames;

import manager.WindowManager;
import panels.BoardHolder;
import panels.LoggerHolder;
import utils.GameConnection;

import javax.swing.*;
import java.io.IOException;

import static java.awt.BorderLayout.*;

public class ShipPlacement extends JFrame {

    private String mode;
    private final WindowManager manager;
    public LoggerHolder loggerHolder = new LoggerHolder(new JLabel("Place your boats!"));
    BoardHolder boardHolder = new BoardHolder(this);
    private JPanel auxiliaryHolder = new JPanel();

    public ShipPlacement(WindowManager manager, String mode) {
        super("Ship placement");
        this.manager = manager;
        this.mode = mode;
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
                if (!boardHolder.getShips().isEmpty()) {
                    loggerHolder.getLogger().setText("You haven't placed all of your ships!");
                    return;
                }

                manager.setShipPlacementVisibility(false);

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
