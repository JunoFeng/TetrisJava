import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;  //

public class HighScoreScreen extends JFrame {

    private static List<HighScore> highScoreList = new ArrayList<>(); // Store high scores in memory
    private JTable highScoreTable;
    private DefaultTableModel tableModel;


    public HighScoreScreen() {
        loadHighScores(); // Load the high scores from the file when the screen is initialized
        initUI();
        updateHighScoreDisplay();
    }

    private void initUI() {
        setTitle("High Scores");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set up the JTable
        String[] columns = {"#", "Name", "Score"};
        tableModel = new DefaultTableModel(columns, 0);
        highScoreTable = new JTable(tableModel);
        highScoreTable.setEnabled(false);  // Disable editing

        JScrollPane scrollPane = new JScrollPane(highScoreTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);

        JButton clearButton = new JButton("Clear High Score");
        clearButton.addActionListener(e -> clearHighScores());
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void addNewHighScore(String playerName, int score) {
        highScoreList.add(new HighScore(playerName, score));
        highScoreList.sort((a, b) -> b.score - a.score); // Sort by score descending

        if (highScoreList.size() > 10) {
            highScoreList = highScoreList.subList(0, 10); // Keep only top 10 scores
        }

        saveHighScores(); // Save the updated high scores to the file
        updateHighScoreDisplay(); // Update the display to show the new high score list
    }

    private void updateHighScoreDisplay() {
        tableModel.setRowCount(0); // Clear the existing rows

        // Populate the table with high scores
        int rank = 1;
        for (HighScore hs : highScoreList) {
            tableModel.addRow(new Object[]{rank++, hs.playerName, hs.score});
        }
    }

    private void clearHighScores() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear all high scores? This action is not reversible.",
                "Clear All High Scores",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            highScoreList.clear(); // Clear all high scores
            saveHighScores(); // Save the empty high score list to the file
            updateHighScoreDisplay(); // Update the display to reflect the changes
        }
    }

    private void saveHighScores() {
        try (FileWriter writer = new FileWriter("highscores.json")) {
            Gson gson = new Gson();
            gson.toJson(highScoreList, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving high scores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    private void loadHighScores() {
        try (FileReader reader = new FileReader("highscores.json")) {
            Gson gson = new Gson();
            java.lang.reflect.Type highScoreListType = new TypeToken<List<HighScore>>() {}.getType(); // Fully qualify Type

            highScoreList = gson.fromJson(reader, highScoreListType);  // Correct usage with Type

            if (highScoreList == null) {
                highScoreList = new ArrayList<>(); // Initialize to an empty list if the file is empty
            }
        } catch (IOException e) {
            System.err.println("High score file not found or could not be loaded. Starting with an empty high score list.");
            highScoreList = new ArrayList<>(); // Initialize to an empty list if there's an error
        }
    }

    // Inner class to represent a high score entry
    static class HighScore {
        String playerName;
        int score;

        HighScore(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
        }
    }
}
