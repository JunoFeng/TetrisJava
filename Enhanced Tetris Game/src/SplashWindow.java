import javax.swing.*;
import java.awt.*;

public class SplashWindow extends JWindow {

    public SplashWindow() {
        JLabel label = new JLabel("Welcome to Enhanced Tetris!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setOpaque(true);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        add(label);
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Show splash screen for 3 seconds, then transition to MainMenu
        Timer timer = new Timer(3000, e -> {
            dispose();
            new MainMenu();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashWindow splash = new SplashWindow();
            splash.setVisible(true);
        });
    }
}
