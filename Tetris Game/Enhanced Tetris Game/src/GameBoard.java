import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;

public class GameBoard extends JPanel {
    private int boardWidth;
    private int boardHeight;
    private final int TILE_SIZE = 30;

    private Color[][] grid;
    private static SharedTetrominoGenerator sharedGenerator = new SharedTetrominoGenerator();
    private GameState gameState;
    private Timer timer;

    private SoundManager soundManager; // Add SoundManager
    private ExternalPlayerHandler externalPlayerHandler;

    // UI components for additional information
    private JLabel scoreLabel;
    private JLabel playerTypeLabel;
    private JLabel initialLevelLabel;
    private JLabel currentLevelLabel;
    private JLabel linesErasedLabel;
    private JLabel aiDebugLabel; // Debugging label to show AI moves
    private JPanel nextTetrominoPanel;

    public GameBoard(Configuration config) {
        setLayout(new BorderLayout());

        // Set board width and height using configuration values, or use default values if config is null
        boardWidth = (config != null) ? config.fieldWidth : 10;
        boardHeight = (config != null) ? config.fieldHeight : 20;

        // Initialize SoundManager based on configuration
        soundManager = SoundManager.getInstance(config.isMusicOn, config.isSoundEffectsOn);

        // Create the game state and initialize the grid
        grid = new Color[boardHeight][boardWidth];
        gameState = new GameState(this);
        gameState.setTetrominoGenerator(sharedGenerator); // Set the shared generator

        // Title at the top
        JLabel titleLabel = new JLabel("Play", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Score label
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        // Create a panel for game information
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.setPreferredSize(new Dimension(200, 0)); // Set preferred width

        // Player type label
        playerTypeLabel = new JLabel("Player Type: " + ((config != null) ? config.playerOneType : "Human"));
        initialLevelLabel = new JLabel("Initial Level: " + ((config != null) ? config.gameLevel : 1));
        currentLevelLabel = new JLabel("Current Level: 1");
        linesErasedLabel = new JLabel("Lines Erased: 0");

        // Debugging label for AI moves
        aiDebugLabel = new JLabel("AI Moves: None");
        aiDebugLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        aiDebugLabel.setForeground(Color.RED);

        // Add labels to info panel
        infoPanel.add(new JLabel("Game Info (Player 1)"));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(playerTypeLabel);
        infoPanel.add(initialLevelLabel);
        infoPanel.add(currentLevelLabel);
        infoPanel.add(linesErasedLabel);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(scoreLabel);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(aiDebugLabel); // Add AI debug label to info panel

        // Panel to display next Tetromino
        nextTetrominoPanel = new JPanel();
        nextTetrominoPanel.setPreferredSize(new Dimension(100, 100));
        nextTetrominoPanel.setBorder(BorderFactory.createTitledBorder("Next Tetromino"));
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(nextTetrominoPanel);

        // Create a separate game panel
        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                gameState.drawGrid(g, grid, TILE_SIZE, boardWidth, boardHeight);
                gameState.drawPiece(g, TILE_SIZE);

                if (gameState.isPaused()) {
                    drawPauseMessage(g);  // Draw the pause message
                }
            }
        };

        // Adjust the height of the game panel to include the entire game area
        gamePanel.setPreferredSize(new Dimension(boardWidth * TILE_SIZE, boardHeight * TILE_SIZE));
        gamePanel.setBackground(Color.BLACK);

        // Set up key controls
        GameControls gameControls = new GameControls(this, gameState);
        gamePanel.addKeyListener(gameControls.getKeyAdapter());
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();

        // Button panel at the bottom
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            timer.stop();  // Stop the game timer
            soundManager.stopBackgroundMusic(); // Stop the background music when going back to the menu
            new MainMenu().setVisible(true);  // Go back to the main menu
            SwingUtilities.getWindowAncestor(this).dispose();  // Close the game window
        });
        buttonPanel.add(backButton, BorderLayout.CENTER);

        // Footer label with author information
        JLabel footerLabel = new JLabel("Author: Guoxin Feng", SwingConstants.CENTER);
        buttonPanel.add(footerLabel, BorderLayout.SOUTH);

        // Add components to the GameBoard
        add(titleLabel, BorderLayout.NORTH);    // Title at the top
        add(infoPanel, BorderLayout.WEST);      // Information panel on the left
        add(gamePanel, BorderLayout.CENTER);    // Game area in the center
        add(buttonPanel, BorderLayout.SOUTH);   // Button panel at the bottom

        // Start the game timer
        timer = new Timer(400 - ((config != null) ? config.gameLevel : 1) * 30, e -> {
            if (!gameState.isPaused() && !gameState.isGameOver()) {
                if (config != null && "External".equals(config.playerOneType)) {
                    handleExternalPlayer();
                } else if (config != null && "AI".equals(config.playerOneType)) {
                    performAIMove();  // Existing AI player logic
                }
                gameState.updateGame(grid);
                gamePanel.repaint();  // Repaint only the game panel
                updateScore();        // Update the score display
            }
        });
        timer.start();
    }

    // Method to perform the AI move and log it
    private void performAIMove() {
        AIplayer ai = new AIplayer(gameState, this);
        ai.makeBestMove();
        logAIMove();  // Log the AI move for debugging
    }

    // Log the AI move
    private void logAIMove() {
        System.out.println("AI made a move");
        aiDebugLabel.setText("AI Moves: Last move made at " + System.currentTimeMillis());
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void updateScore() {
        scoreLabel.setText("Score: " + gameState.getScore());
        // Update other labels as needed (e.g., current level, lines erased)
    }

    public void updateNextTetromino(Tetromino nextPiece) {
        // Code to display the next tetromino in nextTetrominoPanel
        // Use Graphics to draw the next Tetromino representation
        nextTetrominoPanel.repaint();
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public Color[][] getGrid() {
        return grid;
    }

    private void handleExternalPlayer() {
        if (externalPlayerHandler == null) {
            externalPlayerHandler = new ExternalPlayerHandler();
        }

        if (externalPlayerHandler.isConnected()) {
            String command = externalPlayerHandler.receiveCommand();
            if (command != null) {
                switch (command) {
                    case "MOVE_LEFT":
                        gameState.moveLeft();
                        break;
                    case "MOVE_RIGHT":
                        gameState.moveRight();
                        break;
                    case "ROTATE":
                        gameState.rotate();
                        break;
                    case "DROP":
                        gameState.moveDown();
                        break;
                    default:
                        System.out.println("Unknown command received: " + command);
                }
            }
        } else {
            System.err.println("Warning: External server is not connected. Blocks enter a state of no control.");
            JOptionPane.showMessageDialog(this, "External server not connected. Please start TetrisServer.", "Connection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void drawPauseMessage(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Paused", getWidth() / 2 - 50, getHeight() / 2);
    }

    private Configuration loadConfiguration() {
        try (FileReader reader = new FileReader("config.json")) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Configuration.class);
        } catch (IOException e) {
            System.out.println("Configuration file not found or could not be loaded. Using default settings.");
            return null;
        }
    }

    public void updateCurrentLevel(int level) {
        currentLevelLabel.setText("Current Level: " + level);
    }

    public void updateLinesErased(int lines) {
        linesErasedLabel.setText("Lines Erased: " + lines);
    }

    // Inner class to represent configuration data
    static class Configuration {
        public int fieldWidth;
        public int fieldHeight;
        public int gameLevel;
        public boolean isMusicOn;
        public boolean isSoundEffectsOn;
        public boolean isExtendMode;
        public String playerOneType;
    }
}
