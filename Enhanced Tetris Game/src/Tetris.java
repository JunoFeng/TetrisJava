import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {

    private static final int TILE_SIZE = 30;
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;

    private GameBoard gameBoard;

    public Tetris() {
        initUI();
    }

    private void initUI() {
        gameBoard = new GameBoard();  // Initialize the GameBoard
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
        int width = BOARD_WIDTH * TILE_SIZE;
        int height = BOARD_HEIGHT * TILE_SIZE;

        // Add some extra space for window decorations (borders, title bar, etc.)
        Insets insets = getInsets();
        width += insets.left + insets.right;
        height += insets.top + insets.bottom;

        return new Dimension(width, height);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tetris tetris = new Tetris();  // Create a new instance of Tetris
            tetris.setVisible(true);  // Make the window visible
        });
    }
}
