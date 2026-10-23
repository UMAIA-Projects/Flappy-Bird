import javax.sound.sampled.*;
import java.io.File;

public class Sound {

    Clip clip;

    public void setFile(String soundFileName) {

        try {
            File file = new File(soundFileName);

            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);

        }
        catch (Exception e) {
            System.err.println("Erro ao carregar som " + e.getMessage());
        }
    }

    public void play() {

        clip.setFramePosition(0);
        clip.start();
    }

}
