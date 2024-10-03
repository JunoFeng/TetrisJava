import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {
    private static SoundManager instance;  // Singleton instance
    private Clip backgroundClip;
    private boolean isMusicOn;
    private boolean isSoundEffectsOn;

    // Private constructor to prevent instantiation
    private SoundManager(boolean isMusicOn, boolean isSoundEffectsOn) {
        this.isMusicOn = isMusicOn;
        this.isSoundEffectsOn = isSoundEffectsOn;
    }

    // Public method to provide access to the singleton instance
    public static SoundManager getInstance(boolean isMusicOn, boolean isSoundEffectsOn) {
        if (instance == null) {
            synchronized (SoundManager.class) {
                if (instance == null) {
                    instance = new SoundManager(isMusicOn, isSoundEffectsOn);
                }
            }
        }
        return instance;
    }

    public boolean isMusicOn() {
        return isMusicOn;
    }

    public boolean isSoundEffectsOn() {
        return isSoundEffectsOn;
    }

    // Load and play background music
    public void playBackgroundMusic(String filePath) {
        if (!isMusicOn) return;

        try {
            File musicPath = new File(filePath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioInput);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);  // Infinite loop
                backgroundClip.start();
            } else {
                System.out.println("Background music file not found: " + musicPath.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Stop background music
    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    // Play a sound effect once
    public void playSoundEffect(String filePath) {
        if (!isSoundEffectsOn) return;

        try {
            File soundPath = new File(filePath);
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("Sound effect file not found: " + soundPath.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMusicOn(boolean isMusicOn) {
        this.isMusicOn = isMusicOn;
        if (!isMusicOn) {
            stopBackgroundMusic();
        }
    }

    public void setSoundEffectsOn(boolean isSoundEffectsOn) {
        this.isSoundEffectsOn = isSoundEffectsOn;
    }

    public boolean isBackgroundMusicPlaying() {
        return backgroundClip != null && backgroundClip.isRunning();
    }
}
