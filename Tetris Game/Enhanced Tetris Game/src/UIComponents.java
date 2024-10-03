import javax.swing.*;
import java.awt.*;

public class UIComponents {

    private GameBoard gameBoard;
    private JButton backButton;

    public UIComponents(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        initComponents();
    }

    private void initComponents() {
        // Initialize the Back button
        backButton = new JButton("Back");
        backButton.addActionListener(e -> onBackPressed());

        // Add the Back button to the GameBoard (or a container)
        gameBoard.add(backButton, BorderLayout.SOUTH);
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void drawPauseMessage(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Paused", gameBoard.getWidth() / 2 - 50, gameBoard.getHeight() / 2);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press P to resume", gameBoard.getWidth() / 2 - 75, gameBoard.getHeight() / 2 + 30);
    }

    private void onBackPressed() {
        // Pause the game
        gameBoard.getGameState().setPaused(true);

        // Show a confirmation dialog
        int result = JOptionPane.showConfirmDialog(gameBoard, "Do you want to stop the game and go back to the main menu?", "Confirm", JOptionPane.YES_NO_OPTION);

        // Handle the user's response
        if (result == JOptionPane.YES_OPTION) {
            new MainMenu().setVisible(true);
            SwingUtilities.getWindowAncestor(gameBoard).dispose();  // Close the game window
        } else {
            // Resume the game if the user doesn't want to exit
            gameBoard.getGameState().setPaused(false);
        }
    }
}
