import java.awt.*;
import javax.swing.JOptionPane;

public class GameState {
    private int score = 0;
    private Tetromino currentPiece;
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private final GameBoard gameBoard;  // Reference to the GameBoard
    private SharedTetrominoGenerator tetrominoGenerator;

    public Tetromino getCurrentPiece() {
        return currentPiece;
    }

    public void movePieceTo(int x) {
        int deltaX = x - currentPiece.getCoordinates()[0].x;
        for (int i = 0; i < Math.abs(deltaX); i++) {
            if (deltaX > 0) {
                moveRight();
            } else {
                moveLeft();
            }
        }
    }

    // Constructor that accepts the GameBoard
    public GameState(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        spawnPiece();
    }

    public int getScore() {
        return score;
    }

    // Method to add points to the score
    public void addScore(int points) {
        score += points;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
        if (isPaused) {
            // Stop background music when paused
            gameBoard.getSoundManager().stopBackgroundMusic();
        } else if (!isGameOver) {
            // Resume background music when unpaused
            if (gameBoard.getSoundManager().isMusicOn()) {
                gameBoard.getSoundManager().playBackgroundMusic("src/Background.mid");
            }
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void spawnPiece() {
        if (tetrominoGenerator != null) {
            currentPiece = tetrominoGenerator.getNextTetromino();
        } else {
            currentPiece = TetrominoFactory.getRandomTetromino();;
        }

        // Start playing background music only if it is not already playing
        if (!isGameOver && gameBoard.getSoundManager().isMusicOn() && !gameBoard.getSoundManager().isBackgroundMusicPlaying()) {
            gameBoard.getSoundManager().playBackgroundMusic("Enhanced Tetris Game/src/Background.mid");
        }

        // Check if the new Tetromino can be placed
        if (!canPlacePiece(currentPiece, gameBoard.getGrid())) {
            isGameOver = true;
            showGameOverDialog();

            // Stop background music and play game over sound
            gameBoard.getSoundManager().stopBackgroundMusic();
            if (gameBoard.getSoundManager().isSoundEffectsOn()) {
                gameBoard.getSoundManager().playSoundEffect("Enhanced Tetris Game/src/End.wav");
            }

            // If the player achieved a high score, prompt for their name
            if (score > 0) {
                String playerName = JOptionPane.showInputDialog(gameBoard, "Game Over! Enter your name for the high score:");
                if (playerName != null && !playerName.trim().isEmpty()) {
                    HighScoreScreen highScoreScreen = new HighScoreScreen();
                    highScoreScreen.addNewHighScore(playerName, score);
                }
            }
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

            // Play rotation sound effect if enabled
            if (gameBoard.getSoundManager().isSoundEffectsOn()) {
                gameBoard.getSoundManager().playSoundEffect("Enhanced Tetris Game/src/Move.mid");
            }
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
                if (gameBoard.getSoundManager().isSoundEffectsOn()) {
                    gameBoard.getSoundManager().playSoundEffect("src/Move.wav");  // Play sound effect when piece settles
                }
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
                addScore(100);      // Add score for clearing the row
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

    public void setTetrominoGenerator(SharedTetrominoGenerator generator) {
        this.tetrominoGenerator = generator;
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
