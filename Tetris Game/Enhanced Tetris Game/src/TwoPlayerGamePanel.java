import javax.swing.*;
import java.awt.*;

public class TwoPlayerGamePanel extends JPanel {
    private GameBoard playerOneBoard;
    private GameBoard playerTwoBoard;

    public TwoPlayerGamePanel() {
        setLayout(new GridLayout(1, 2)); // Split the panel into two sections for two players

        // Create configurations for each player
        GameBoard.Configuration playerOneConfig = loadPlayerOneConfiguration();
        GameBoard.Configuration playerTwoConfig = loadPlayerTwoConfiguration();

        // Set up both player game boards with corresponding configurations
        playerOneBoard = new GameBoard(playerOneConfig);
        playerTwoBoard = new GameBoard(playerTwoConfig);

        add(playerOneBoard);
        add(playerTwoBoard);


    }



    private GameBoard.Configuration loadPlayerOneConfiguration() {
        // Load player one configuration from the settings (e.g., human, AI, or external player)
        // Example of how to load a configuration
        GameBoard.Configuration config = new GameBoard.Configuration();
        config.playerOneType = "Human"; // Change according to user input
        config.isMusicOn = true;
        config.isSoundEffectsOn = true;
        return config;
    }

    private GameBoard.Configuration loadPlayerTwoConfiguration() {
        // Load player two configuration from the settings
        GameBoard.Configuration config = new GameBoard.Configuration();
        config.playerOneType = "AI"; // Change according to user input
        config.isMusicOn = true;
        config.isSoundEffectsOn = true;
        return config;
    }
}
