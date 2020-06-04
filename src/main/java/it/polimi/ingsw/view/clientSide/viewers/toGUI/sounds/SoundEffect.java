package it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.applet.AudioClip;

public abstract class SoundEffect {
    private static boolean enabled = true;
    private static Clip loopingMusic;

    public static void setEnabled(boolean _enabled){
        enabled = _enabled;
    }

    public static boolean getEnabled(){
        return enabled;
    }

    public static synchronized void playSound( String url) {
        new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResource("/sounds/" + url));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
    public static synchronized void startLoopMusic(String url){

        if(loopingMusic!=null && loopingMusic.isRunning())
            loopingMusic.stop();

        new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
            public void run() {
                try {
                    loopingMusic = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResource("/sounds/" + url));
                    loopingMusic.open(inputStream);
                    loopingMusic.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static void stopLoopingMusic(){
        if(loopingMusic!=null && loopingMusic.isRunning())
            loopingMusic.stop();
    }
}
