import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationScreen extends JFrame {

    private JSlider widthSlider;
    private JSlider heightSlider;
    private JSlider levelSlider;
    private JCheckBox musicCheckBox;
    private JCheckBox soundEffectsCheckBox;
    private JCheckBox extendModeCheckBox;
    private JRadioButton humanPlayerRadioButton;
    private JRadioButton aiPlayerRadioButton;
    private JRadioButton externalPlayerRadioButton;

    public ConfigurationScreen() {
        initUI();
        loadConfig(); // Load saved configuration when the screen is initialized
    }

    private void initUI() {
        setTitle("Configuration");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(11, 2, 10, 10));

        // Width Slider
        add(new JLabel("Field Width (No of cells):"));
        widthSlider = new JSlider(5, 20, 10);
        widthSlider.setMajorTickSpacing(1);
        widthSlider.setPaintTicks(true);
        widthSlider.setPaintLabels(true);
        add(widthSlider);

        // Height Slider
        add(new JLabel("Field Height (No of cells):"));
        heightSlider = new JSlider(10, 30, 20);
        heightSlider.setMajorTickSpacing(1);
        heightSlider.setPaintTicks(true);
        heightSlider.setPaintLabels(true);
        add(heightSlider);

        // Game Level Slider
        add(new JLabel("Game Level:"));
        levelSlider = new JSlider(1, 10, 1);
        levelSlider.setMajorTickSpacing(1);
        levelSlider.setPaintTicks(true);
        levelSlider.setPaintLabels(true);
        add(levelSlider);

        // Music CheckBox
        musicCheckBox = new JCheckBox("Music (On/Off):");
        add(musicCheckBox);
        add(new JLabel("Off"));

        // Sound Effects CheckBox
        soundEffectsCheckBox = new JCheckBox("Sound Effect (On/Off):");
        add(soundEffectsCheckBox);
        add(new JLabel("Off"));

        // Extend Mode CheckBox
        extendModeCheckBox = new JCheckBox("Extend Mode (On/Off):");
        add(extendModeCheckBox);
        add(new JLabel("Off"));

        // Player Type - Player One
        add(new JLabel("Player One Type:"));
        JPanel playerOnePanel = new JPanel(new FlowLayout());
        ButtonGroup playerOneGroup = new ButtonGroup();
        humanPlayerRadioButton = new JRadioButton("Human");
        aiPlayerRadioButton = new JRadioButton("AI");
        externalPlayerRadioButton = new JRadioButton("External");

        playerOneGroup.add(humanPlayerRadioButton);
        playerOneGroup.add(aiPlayerRadioButton);
        playerOneGroup.add(externalPlayerRadioButton);

        playerOnePanel.add(humanPlayerRadioButton);
        playerOnePanel.add(aiPlayerRadioButton);
        playerOnePanel.add(externalPlayerRadioButton);
        humanPlayerRadioButton.setSelected(true); // Default to human player

        add(playerOnePanel);

        // Add Save and Back buttons
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveConfig()); // Save configuration when clicking "Save"
        add(saveButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new MainMenu();
            dispose();
        });
        add(backButton);

        setVisible(true);
    }

    // Method to save the current configuration to JSON
    private void saveConfig() {
        Configuration config = new Configuration();
        config.fieldWidth = widthSlider.getValue();
        config.fieldHeight = heightSlider.getValue();
        config.gameLevel = levelSlider.getValue();
        config.isMusicOn = musicCheckBox.isSelected();
        config.isSoundEffectsOn = soundEffectsCheckBox.isSelected();
        config.isExtendMode = extendModeCheckBox.isSelected();
        config.playerOneType = getPlayerOneType();

        try (FileWriter writer = new FileWriter("config.json")) {
            Gson gson = new Gson();
            gson.toJson(config, writer);
            JOptionPane.showMessageDialog(this, "Configuration saved successfully!", "Save Config", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving configuration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Method to load the configuration from JSON
    private void loadConfig() {
        try (FileReader reader = new FileReader("config.json")) {
            Gson gson = new Gson();
            Configuration config = gson.fromJson(reader, Configuration.class);

            if (config != null) {
                widthSlider.setValue(config.fieldWidth);
                heightSlider.setValue(config.fieldHeight);
                levelSlider.setValue(config.gameLevel);
                musicCheckBox.setSelected(config.isMusicOn);
                soundEffectsCheckBox.setSelected(config.isSoundEffectsOn);
                extendModeCheckBox.setSelected(config.isExtendMode);
                setPlayerOneType(config.playerOneType);
            }
        } catch (IOException e) {
            System.out.println("Configuration file not found or could not be loaded. Using default settings.");
        }
    }

    // Method to get the selected player type
    private String getPlayerOneType() {
        if (humanPlayerRadioButton.isSelected()) {
            return "Human";
        } else if (aiPlayerRadioButton.isSelected()) {
            return "AI";
        } else {
            return "External";
        }
    }

    // Method to set the selected player type based on the saved configuration
    private void setPlayerOneType(String playerType) {
        switch (playerType) {
            case "Human":
                humanPlayerRadioButton.setSelected(true);
                break;
            case "AI":
                aiPlayerRadioButton.setSelected(true);
                break;
            case "External":
                externalPlayerRadioButton.setSelected(true);
                break;
        }
    }

    // Inner class to represent configuration data
    static class Configuration {
        public int fieldWidth;
        public int fieldHeight;
        public int gameLevel;
        public boolean isMusicOn;
        public boolean isSoundEffectsOn;
        public boolean isExtendMode;
        public String playerOneType;
    }
}
