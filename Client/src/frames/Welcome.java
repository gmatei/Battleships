package frames;

import frames.Menu;
import manager.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static java.awt.BorderLayout.CENTER;

public class Welcome extends JFrame {

    private final WindowManager manager;

    public Welcome(WindowManager manager) {
        super("Welcome");
        this.manager = manager;
        init();
        setLocationRelativeTo(null);
    }

    private void init() {
        var panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 600));

        var label = new JLabel("What's your name?");
        label.setFont(new Font("Serif", Font.PLAIN, 50));
        label.setHorizontalAlignment(JLabel.CENTER);

        JTextField textField = new JTextField();
        textField.setFont(new Font("Serif", Font.PLAIN, 40));
        textField.setHorizontalAlignment(JTextField.CENTER);
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                manager.setName(textField.getText());
                manager.setWelcomeVisibility(false);
                manager.setMenu(new Menu(manager));
                manager.setMenuVisibility(true);
            }
        };
        textField.addActionListener(action);

        panel.setLayout(new GridLayout(6, 1));
        panel.add(new JPanel());
        panel.add(new JPanel());
        panel.add(label);
        panel.add(textField);
        panel.add(new JPanel());
        panel.add(new JPanel());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(panel, CENTER);
        pack();
    }
}
