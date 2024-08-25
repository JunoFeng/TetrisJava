import javax.swing.*;
import java.awt.*;

public class ConfigurationScreen extends JFrame {

    private JSpinner widthSpinner;
    private JSpinner heightSpinner;
    private JSpinner levelSpinner;
    private JCheckBox musicCheckBox;
    private JCheckBox soundEffectsCheckBox;
    private JCheckBox aiPlayCheckBox;
    private JCheckBox extendModeCheckBox;

    public ConfigurationScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Configuration");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 10, 10));

        add(new JLabel("Field Width:"));
        widthSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 20, 1));
        add(widthSpinner);

        add(new JLabel("Field Height:"));
        heightSpinner = new JSpinner(new SpinnerNumberModel(20, 10, 30, 1));
        add(heightSpinner);

        add(new JLabel("Game Level:"));
        levelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        add(levelSpinner);

        musicCheckBox = new JCheckBox("Music On");
        add(musicCheckBox);

        soundEffectsCheckBox = new JCheckBox("Sound Effects On");
        add(soundEffectsCheckBox);

        aiPlayCheckBox = new JCheckBox("AI Play Mode");
        add(aiPlayCheckBox);

        extendModeCheckBox = new JCheckBox("Extend Mode");
        add(extendModeCheckBox);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new MainMenu();
            dispose();
        });

        add(backButton);

        setVisible(true);
    }
}
