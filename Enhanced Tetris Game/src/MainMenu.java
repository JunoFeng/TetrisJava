import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        initUI();
    }

    private void initUI() {
        setTitle("Enhanced Tetris - Main Menu");
        setSize(400, 600);  // Adjusted size for better appearance
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Use a GridBagLayout to control the positioning of the buttons
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);  // Add padding around buttons

        // Button styling
        JButton playButton = createStyledButton("Play");
        playButton.addActionListener(e -> {
            new Tetris().setVisible(true);  // Start the game by making the Tetris window visible
            dispose();  // Close the main menu
        });

        JButton configButton = createStyledButton("Configuration");
        configButton.addActionListener(e -> {
            new ConfigurationScreen();
            dispose();
        });

        JButton highScoresButton = createStyledButton("High Scores");
        highScoresButton.addActionListener(e -> {
            new HighScoreScreen();
            dispose();
        });

        JButton exitButton = createStyledButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the frame
        add(playButton, gbc);
        add(configButton, gbc);
        add(highScoresButton, gbc);
        add(exitButton, gbc);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));  // Smaller font size
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));  // Set a fixed size for buttons
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}
