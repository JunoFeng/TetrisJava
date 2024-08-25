import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        initUI();
    }

    private void initUI() {
        setTitle("Enhanced Tetris - Main Menu");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> {
            new Tetris().setVisible(true);  // Start the game by making the Tetris window visible
            dispose();  // Close the main menu
        });

        JButton configButton = new JButton("Configuration");
        configButton.addActionListener(e -> {
            new ConfigurationScreen();
            dispose();
        });

        JButton highScoresButton = new JButton("High Scores");
        highScoresButton.addActionListener(e -> {
            new HighScoreScreen();
            dispose();
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        add(playButton);
        add(configButton);
        add(highScoresButton);
        add(exitButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}
