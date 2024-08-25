import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameControls {
    private final GameBoard gameBoard;
    private final GameState gameState;

    public GameControls(GameBoard gameBoard, GameState gameState) {
        this.gameBoard = gameBoard;
        this.gameState = gameState;
    }

    public KeyAdapter getKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keycode = e.getKeyCode();

                // Toggle pause state with the 'P' key
                if (keycode == KeyEvent.VK_P) {
                    gameState.setPaused(!gameState.isPaused());
                    gameBoard.repaint();  // Repaint to show the pause message
                    return;
                }

                // If the game is paused, don't process other key events
                if (gameState.isPaused() || gameState.isGameOver()) {
                    return;
                }

                switch (keycode) {
                    case KeyEvent.VK_LEFT:
                        if (gameState.canMoveLeft(gameBoard.getGrid())) {
                            gameState.moveLeft();
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (gameState.canMoveRight(gameBoard.getGrid())) {
                            gameState.moveRight();
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (gameState.canMoveDown(gameBoard.getGrid())) {
                            gameState.moveDown();
                        }
                        break;
                    case KeyEvent.VK_UP:
                        gameState.rotate();  // Rotate the Tetromino
                        break;
                }
                gameBoard.repaint();
            }
        };
    }
}
