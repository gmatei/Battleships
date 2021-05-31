package frames;

import manager.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.BorderLayout.*;

public class Menu extends JFrame {

    private WindowManager manager;
    private JPanel panel;
    private final JLabel title = new JLabel("Battleships");
    private JLabel console;
    private final JTextField textField = new JTextField();
    private final JButton singleplayerBtn = new JButton("Singleplayer");
    private final JButton multiplayerBtn = new JButton("Multiplayer");
    private final JButton howToPlayBtn = new JButton("How to play");
    private final JButton exitBtn = new JButton("Exit");

    public Menu(WindowManager manager) {
        super("Battleships");
        this.manager = manager;
        init();
        setLocationRelativeTo(null);
    }

    private void init() {

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(1024, 768));
        panel.setLayout(new GridLayout(3, 3));

        // title
        title.setFont(new Font("Serif", Font.PLAIN, 80));
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        title.setHorizontalAlignment(JLabel.CENTER);
        panel.add(title);

        // console log
        console = new JLabel("Welcome, " + manager.getName() + "! Select a game mode.");
        console.setFont(new Font("Serif", Font.PLAIN, 40));
        console.setHorizontalAlignment(JLabel.CENTER);
        panel.add(console);

        var buttonFont = new Font("Serif", Font.PLAIN, 30);

        // singleplayer & multiplayer
        var line3 = new JPanel(new GridLayout(2, 2, 50, 50));
        singleplayerBtn.setFont(buttonFont);
        singleplayerBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                manager.setWaiting(new Waiting(manager));
//                manager.setMenuVisibility(false);
//                manager.setWaitingVisibility(true);
//                manager.getWaiting().display();

                manager.setShipPlacement(new ShipPlacement(manager, "true"));
                manager.setMenuVisibility(false);
                manager.setShipPlacementVisibility(true);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                console.setText("Singleplayer mode: Battle against the A.I.");
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                console.setText("Welcome, " + manager.getName() + "! Select a game mode.");
            }
        });

        multiplayerBtn.setFont(buttonFont);
        multiplayerBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                console.setText("Searching for opponent...");
                manager.setShipPlacement(new ShipPlacement(manager, "false"));
                manager.setMenuVisibility(false);
                manager.setShipPlacementVisibility(true);
            }


            public void mouseEntered(java.awt.event.MouseEvent evt) {
                console.setText("Multiplayer mode: Battle against a human opponent.");
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                console.setText("Welcome, " + manager.getName() + "! Select a game mode.");
            }
        });

        howToPlayBtn.setFont(buttonFont);
        howToPlayBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                console.setText("How to play: Instructions on how the game is played.");
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                console.setText("Welcome, " + manager.getName() + "! Select a game mode.");
            }
        });

        exitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                console.setText("Exit: Quit the game.");
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                console.setText("Welcome, " + manager.getName() + "! Select a game mode.");
            }
        });
        exitBtn.setFont(buttonFont);
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        line3.add(singleplayerBtn);
        line3.add(multiplayerBtn);
        line3.add(howToPlayBtn);
        line3.add(exitBtn);
        panel.add(line3);

        // main frame (contains the big jpanel)
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(panel, CENTER);
        pack();
    }

}
