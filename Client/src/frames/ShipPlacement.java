package frames;

import manager.WindowManager;
import panels.BoardHolder;
import panels.LoggerHolder;

import javax.swing.*;

import static java.awt.BorderLayout.*;

public class ShipPlacement extends JFrame {

    private final WindowManager manager;
    private int[][] board;
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
                manager.setMenuVisibility(true);
            }
        });
        auxiliaryHolder.add(playBtn);

        add(loggerHolder, NORTH);
        add(boardHolder, CENTER);
        add(auxiliaryHolder, SOUTH);
        pack();
    }
}
