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
        initBoard();
    }

    private void initBoard() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE));
        grid = new Color[BOARD_HEIGHT][BOARD_WIDTH];  // Initialize the grid
        gameState = new GameState(this);  // Initialize GameState with reference to GameBoard

        GameControls gameControls = new GameControls(this, gameState);  // Initialize GameControls
        addKeyListener(gameControls.getKeyAdapter());  // Add key listener
        setFocusable(true);  // Ensure GameBoard can receive focus
        requestFocusInWindow();  // Request focus for key events

        timer = new Timer(400, e -> {
            if (!gameState.isPaused() && !gameState.isGameOver()) {
                gameState.updateGame(grid);
                repaint();  // Trigger the repaint to update the game board
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameState.drawGrid(g, grid, TILE_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
        gameState.drawPiece(g, TILE_SIZE);

        if (gameState.isPaused()) {
            drawPauseMessage(g);  // Draw the pause message
        }
    }

    private void drawPauseMessage(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Paused", getWidth() / 2 - 50, getHeight() / 2);
    }
}
