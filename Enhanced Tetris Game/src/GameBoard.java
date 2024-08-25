import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final int TILE_SIZE = 30;

    private Color[][] grid;  // Your game grid
    private GameState gameState;  // Reference to the game state
    private Timer timer;
    private JButton backButton;  // Back button

    public GameBoard() {
        initBoard();
    }

    private void initBoard() {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        // Initialize the grid and game state
        grid = new Color[BOARD_HEIGHT][BOARD_WIDTH];
        gameState = new GameState(this);

        // Create a JPanel to hold the game and the back button
        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                gameState.drawGrid(g, grid, TILE_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
                gameState.drawPiece(g, TILE_SIZE);
            }
        };
        gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE));
        gamePanel.setBackground(Color.BLACK);

        // Create the back button
        backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            timer.stop();  // Stop the game timer
            new MainMenu().setVisible(true);  // Go back to the main menu
            SwingUtilities.getWindowAncestor(this).dispose();  // Close the game window
        });

        // Add the game panel to the center and the back button to the bottom
        add(gamePanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // Start the game timer
        timer = new Timer(400, e -> {
            gameState.updateGame(grid);
            gamePanel.repaint();  // Trigger the repaint to update the game board
        });
        timer.start();
    }

    public GameState getGameState() {
        return gameState;
    }

    public Color[][] getGrid() {
        return grid;
    }
}
