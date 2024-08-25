import java.awt.*;
import javax.swing.JOptionPane;

public class GameState {
    private Tetromino currentPiece;
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private final GameBoard gameBoard;  // Reference to the GameBoard

    // Constructor that accepts the GameBoard
    public GameState(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        spawnPiece();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void spawnPiece() {
        currentPiece = Tetromino.getRandomPiece();

        // Check if the new Tetromino can be placed
        if (!canPlacePiece(currentPiece, gameBoard.getGrid())) {
            isGameOver = true;
            System.out.println("Game Over!");
        }
    }

    public void moveLeft() {
        if (currentPiece != null && canMoveLeft(gameBoard.getGrid())) {
            currentPiece.moveLeft();
        }
    }

    public void moveRight() {
        if (currentPiece != null && canMoveRight(gameBoard.getGrid())) {
            currentPiece.moveRight();
        }
    }

    public void moveDown() {
        if (currentPiece != null && canMoveDown(gameBoard.getGrid())) {
            currentPiece.moveDown();
        }
    }

    public void rotate() {
        if (currentPiece != null) {
            currentPiece.rotate(gameBoard.getGrid());  // Pass the grid to the Tetromino's rotate method
        }
    }

    public boolean canMoveLeft(Color[][] grid) {
        if (currentPiece == null) return false;

        for (Point p : currentPiece.getCoordinates()) {
            int newX = p.x - 1;
            if (newX < 0 || grid[p.y][newX] != null) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveRight(Color[][] grid) {
        if (currentPiece == null) return false;

        for (Point p : currentPiece.getCoordinates()) {
            int newX = p.x + 1;
            if (newX >= grid[0].length || grid[p.y][newX] != null) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveDown(Color[][] grid) {
        if (currentPiece == null) return false;

        for (Point p : currentPiece.getCoordinates()) {
            int newY = p.y + 1;
            if (newY >= grid.length || grid[newY][p.x] != null) {
                return false;
            }
        }
        return true;
    }

    public void settlePiece(Color[][] grid) {
        if (currentPiece != null) {
            for (Point p : currentPiece.getCoordinates()) {
                grid[p.y][p.x] = currentPiece.getColor();  // Settle the Tetromino blocks in the grid
            }
        }
    }

    public void updateGame(Color[][] grid) {
        if (!isGameOver && !isPaused) {  // Don't update if the game is paused or over
            if (!canMoveDown(grid)) {
                settlePiece(grid);  // Settle the current Tetromino
                checkRows(grid);     // Check if any row is full and clear it
                spawnPiece();        // Spawn a new Tetromino
            } else {
                currentPiece.moveDown();
            }
        }
    }

    public void checkRows(Color[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            boolean fullRow = true;
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == null) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                clearRow(grid, y);  // Clear the full row
            }
        }
    }

    public void clearRow(Color[][] grid, int row) {
        for (int y = row; y > 0; y--) {
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = grid[y - 1][x];  // Shift the rows down
            }
        }
        for (int x = 0; x < grid[0].length; x++) {
            grid[0][x] = null;  // Clear the top row
        }
    }

    public void drawGrid(Graphics g, Color[][] grid, int tileSize, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] != null) {
                    g.setColor(grid[y][x]);
                    g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    public void drawPiece(Graphics g, int tileSize) {
        if (currentPiece != null) {
            for (Point p : currentPiece.getCoordinates()) {
                g.setColor(currentPiece.getColor());
                g.fillRect(p.x * tileSize, p.y * tileSize, tileSize, tileSize);
            }
        }
    }
    // Show the Game Over dialog
    private void showGameOverDialog() {
        JOptionPane.showMessageDialog(gameBoard, "Game Over! You have lost the game.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    // Check if the Tetromino can be placed on the grid
    private boolean canPlacePiece(Tetromino piece, Color[][] grid) {
        for (Point p : piece.getCoordinates()) {
            if (p.y >= 0 && grid[p.y][p.x] != null) {
                return false;  // Collision detected, cannot place piece
            }
        }
        return true;  // No collision, piece can be placed
    }
}
