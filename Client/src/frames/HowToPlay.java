package frames;

import manager.WindowManager;

import javax.swing.*;


import static java.awt.BorderLayout.*;

public class HowToPlay extends JFrame {

    private final WindowManager manager;
    private JPanel auxiliaryHolder = new JPanel();
    private JLabel instructions = new JLabel();

    public HowToPlay(WindowManager manager) {
        super("How To Play");
        this.manager = manager;
        init();
        setLocationRelativeTo(null);
    }

    private void init() {
        var backBtn = new JButton("BACK");
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manager.setHowToPlayVisibility(false);
                manager.setMenuVisibility(true);
            }
        });
        auxiliaryHolder.add(backBtn);

        instructions.setText
        (
                "<html>" +
        """
                    Game Objective <br/> <br/>
                        The object of Battleship is to try and sink all of the other player's before they sink all of your ships. All of the other player's ships are somewhere on his/her board. <br/>
                        You try and hit them by clicking on the coordinates of one of the squares on the board. The other player also tries to hit your ships by calling out coordinates. <br/>
                        Neither you nor the other player can see the other's board so you must try to guess where they are. The screen has two grids:  the left section for the player's ships and the right section for recording the player's guesses. <br/><br/>
                    
                    Starting a New Game <br/> <br/>
                        Each player places the 5 ships somewhere on their board.  The ships can only be placed vertically or horizontally. Diagonal placement is not allowed. <br/>
                        No part of a ship may hang off the edge of the board.  Ships may not overlap each other.  No ships may be placed on another ship. <br/>
                        Once the guessing begins, the players may not move the ships. <br/>                    
                        The 5 ships are:  Carrier (occupies 5 spaces), Battleship (4), Cruiser (3), Submarine (3), and Destroyer (2). <br/> <br/>
                      
                    Playing the Game <br/> <br/>
                    Player's take turns guessing by clicking on squares. The board is marked with a red X in case of a miss and with a black one in case of a hit. <br/>
                    When all of the squares that one your ships occupies have been hit, the ship will be sunk. As soon as all of one player's ships have been sunk, the game ends. <br/> <br/>                                                                                               
                """
                + "</html>"
        );

        add(instructions, CENTER);
        add(auxiliaryHolder, SOUTH);
        pack();
    }
}
