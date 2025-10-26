package managers;


import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import util.SoundLoader;

public class SoundManager {
    private static final SoundManager INSTANCE = new SoundManager();

    private MediaPlayer backgroundMusicPlayer;
    private AudioClip hitSFX;

    private boolean isMusicPlaying = false;

    private SoundManager() {
        loadSounds();
    }

    public static SoundManager getInstance() {
        return INSTANCE;
    }

    public void loadSounds() {
        // load background music
       backgroundMusicPlayer = SoundLoader.loadMusic("/assets/sounds/bg.wav");
        if (backgroundMusicPlayer != null) {
            // infinite loop
            backgroundMusicPlayer.setOnEndOfMedia(() -> backgroundMusicPlayer.seek(Duration.ZERO));
        }

        // load hit music
        hitSFX = SoundLoader.loadClip("/assets/sounds/hit.wav");
    }

    public void playMusic() {
        if (backgroundMusicPlayer != null && !isMusicPlaying) {
            backgroundMusicPlayer.play();
            isMusicPlaying = true;
        }
    }

    public void stopMusic() {
        if (backgroundMusicPlayer != null && isMusicPlaying) {
            backgroundMusicPlayer.stop();
            isMusicPlaying = false;
        }
    }

    public void playHit() {
        if (hitSFX != null) {
            hitSFX.play();
        }
    }
}
