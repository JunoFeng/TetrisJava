import javax.swing.*;
import java.awt.*;

public class SplashWindow extends JWindow {

    public SplashWindow() {
        // Load the image from the specified path
        ImageIcon splashImage = new ImageIcon("D:\\Tetris Game\\Enhanced Tetris Game\\src\\TetrisGame.jpg");
        JLabel imageLabel = new JLabel(splashImage);

        // Add the image to the window
        add(imageLabel);

        // Set the size of the window to match the image size
        setSize(splashImage.getIconWidth(), splashImage.getIconHeight());
        setLocationRelativeTo(null);

        // Show splash screen for 3 seconds, then transition to MainMenu
        Timer timer = new Timer(3000, e -> {
            dispose();
            new MainMenu().setVisible(true);
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
