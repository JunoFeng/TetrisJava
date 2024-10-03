import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;

public class Tetris extends JFrame {

    private static final int TILE_SIZE = 30;
    private int boardWidth;
    private int boardHeight;

    private GameBoard gameBoard;

    public Tetris() {
        GameBoard.Configuration config = loadConfiguration();  // Use GameBoard.Configuration here
        initUI(config);
    }

    private void initUI(GameBoard.Configuration config) {
        gameBoard = new GameBoard(config);  // Pass the correct configuration object to the GameBoard
        add(gameBoard);  // Add the GameBoard to the JFrame

        setTitle("Tetris Game");  // Set the window title
        pack();  // Automatically adjust the frame size to fit the components
        setResizable(false);  // Prevent the window from being resized
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Close the application when the window is closed
        setLocationRelativeTo(null);  // Center the window on the screen
    }

    @Override
    public Dimension getPreferredSize() {
        // Calculate the preferred size of the JFrame based on the game board dimensions and tile size
        int gamePanelWidth = boardWidth * TILE_SIZE;   // Width of the game board
        int gamePanelHeight = boardHeight * TILE_SIZE; // Height of the game board

        int infoPanelWidth = 200;  // Width of the side panel with game info (matches the width in GameBoard.java)

        // Add extra space for the button panel and footer label
        int buttonPanelHeight = 70;  // Approximate height for the button panel and footer
        int totalHeight = gamePanelHeight + buttonPanelHeight;

        // Add the width for the information panel
        int totalWidth = gamePanelWidth + infoPanelWidth;

        // Add some extra space for window decorations (borders, title bar, etc.)
        Insets insets = getInsets();
        totalWidth += insets.left + insets.right;
        totalHeight += insets.top + insets.bottom;

        return new Dimension(totalWidth, totalHeight);
    }

    private GameBoard.Configuration loadConfiguration() {
        try (FileReader reader = new FileReader("config.json")) {
            Gson gson = new Gson();
            GameBoard.Configuration config = gson.fromJson(reader, GameBoard.Configuration.class);

            if (config != null) {
                boardWidth = config.fieldWidth;
                boardHeight = config.fieldHeight;
            } else {
                // Default values in case the configuration is missing
                boardWidth = 10;
                boardHeight = 20;
            }
            return config;
        } catch (IOException e) {
            System.out.println("Configuration file not found or could not be loaded. Using default settings.");
            // Default values if the configuration file cannot be loaded
            boardWidth = 10;
            boardHeight = 20;
            return new GameBoard.Configuration();  // Return a default configuration
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tetris tetris = new Tetris();  // Create a new instance of Tetris
            tetris.setVisible(true);  // Make the window visible
        });
    }
}
