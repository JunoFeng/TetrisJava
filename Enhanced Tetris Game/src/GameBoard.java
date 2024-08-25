import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final int TILE_SIZE = 30;

    private Color[][] grid;  // Your game grid
    private GameState gameState;  // Reference to the game state
    private Timer timer;

    public GameBoard() {
        setLayout(new BorderLayout());

        // Title at the top
        JLabel titleLabel = new JLabel("Play", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Create a separate game panel
        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                gameState.drawGrid(g, grid, TILE_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
                gameState.drawPiece(g, TILE_SIZE);

                if (gameState.isPaused()) {
                    drawPauseMessage(g);  // Draw the pause message
                }
            }
        };

        // Adjust the height of the game panel to include the entire game area
        gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE + 20));
        gamePanel.setBackground(Color.BLACK);

        // Create the game state and initialize the grid
        grid = new Color[BOARD_HEIGHT][BOARD_WIDTH];
        gameState = new GameState(this);

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
            new MainMenu().setVisible(true);  // Go back to the main menu
            SwingUtilities.getWindowAncestor(this).dispose();  // Close the game window
        });
        buttonPanel.add(backButton, BorderLayout.CENTER);

        // Footer label with author information
        JLabel footerLabel = new JLabel("Author: Guoxin Feng", SwingConstants.CENTER);
        buttonPanel.add(footerLabel, BorderLayout.SOUTH);

        // Add components to the GameBoard
        add(titleLabel, BorderLayout.NORTH);  // Title at the top
        add(gamePanel, BorderLayout.CENTER);  // Game area in the center
        add(buttonPanel, BorderLayout.SOUTH);  // Button panel at the bottom

        // Start the game timer
        timer = new Timer(400, e -> {
            if (!gameState.isPaused() && !gameState.isGameOver()) {
                gameState.updateGame(grid);
                gamePanel.repaint();  // Repaint only the game panel
            }
        });
        timer.start();
    }

    public GameState getGameState() {
        return gameState;
    }

    public Color[][] getGrid() {
        return grid;
    }

    private void drawPauseMessage(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Paused", getWidth() / 2 - 50, getHeight() / 2);
    }
}
