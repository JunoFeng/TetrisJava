import javax.swing.*;
import java.awt.*;

public class HighScoreScreen extends JFrame {

    public HighScoreScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("High Scores");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea highScoreArea = new JTextArea();
        highScoreArea.setEditable(false);
        highScoreArea.setFont(new Font("Arial", Font.PLAIN, 18));

        // Dummy data
        highScoreArea.setText("Top 10 High Scores:\n\n" +
                "1. Player 1 - 1000\n" +
                "2. Player 2 - 950\n" +
                "3. Player 3 - 900\n" +
                "4. Player 4 - 850\n" +
                "5. Player 5 - 800\n" +
                "6. Player 6 - 750\n" +
                "7. Player 7 - 700\n" +
                "8. Player 8 - 650\n" +
                "9. Player 9 - 600\n" +
                "10. Player 10 - 550");

        JScrollPane scrollPane = new JScrollPane(highScoreArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new MainMenu();
            dispose();
        });
        add(backButton, BorderLayout.SOUTH);

        setVisible(true);
    }
}
