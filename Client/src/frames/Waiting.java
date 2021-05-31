package frames;

import manager.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

import static java.awt.BorderLayout.CENTER;

public class Waiting extends JFrame {

    private final WindowManager manager;

    public Waiting(WindowManager manager) {
        super("Waiting...");
        this.manager = manager;
    }

    public void display() {
        var panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, 100));

        var label = new JLabel("Waiting for opponent...");
        label.setHorizontalAlignment(JLabel.CENTER);
//        label.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                manager.setWaitingVisibility(false);
//                manager.setShipPlacement(new ShipPlacement(manager));
//                manager.setShipPlacementVisibility(true);
//            }
//        });
        panel.add(label);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(panel, CENTER);
        pack();
    }
}
