package frames;

import manager.WindowManager;

import javax.swing.*;

import java.awt.*;

import static java.awt.BorderLayout.*;

public class ShipPlacement extends JFrame {

    private final WindowManager manager;
    private int[][] board;
    private JPanel loggerHolder = new JPanel();
    private JPanel boardHolder = new JPanel();
    private JPanel auxiliaryHolder = new JPanel();

    public ShipPlacement(WindowManager manager) {
        super("Ship placement");
        this.manager = manager;
        init();
    }

    private void init() {
        loggerHolder.add(new JLabel("Place your boats!"));

        boardHolder.setPreferredSize(new Dimension(1024, 768));
        boardHolder.setBackground(Color.white);

        auxiliaryHolder.add(new JButton("UNDO"));
        auxiliaryHolder.add(new JButton("Carrier (5)"));
        auxiliaryHolder.add(new JButton("Battleship (4)"));
        auxiliaryHolder.add(new JButton("Cruiser (3)"));
        auxiliaryHolder.add(new JButton("Submarine (3)"));
        auxiliaryHolder.add(new JButton("Destroyer (2)"));
        auxiliaryHolder.add(new JButton("PLAY"));

        add(loggerHolder, NORTH);
        add(boardHolder, CENTER);
        add(auxiliaryHolder, SOUTH);
        pack();
    }
}
