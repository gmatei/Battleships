package panels;

import javax.swing.*;

public class LoggerHolder extends JPanel {

    private JLabel logger;

    public LoggerHolder(JLabel logger) {
        this.logger = logger;
        add(logger);
    }

    public JLabel getLogger() {
        return logger;
    }
}
