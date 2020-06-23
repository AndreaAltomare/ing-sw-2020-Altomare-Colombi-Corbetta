package it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds;

import it.polimi.ingsw.view.clientSide.View;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.applet.AudioClip;

public abstract class SoundEffect {
    private static boolean enabled = true;
    private static Clip loopingMusic;
    private static String loopingSong;

    public static void setEnabled(boolean _enabled){
        enabled = _enabled;
        if(!enabled)
            stopLoopingMusic();
        else{
            startLoopMusic(loopingSong);
        }
    }

    public static boolean getEnabled(){
        return enabled;
    }

    public static synchronized void playSound( String url) {
        if (enabled)
            new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
                public void run() {
                    try {
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResource("/sounds/" + url));
                        if(View.debugging)
                            System.out.println("MUSIC: "+"/sounds/" + url);
                        clip.open(inputStream);
                        clip.start();
                    } catch (Exception ignore) {
                    }
                }
            }).start();
    }

    public static synchronized void startLoopMusic(String url){
        loopingSong = url;

        if(loopingMusic!=null && loopingMusic.isRunning())
            loopingMusic.stop();

        if(!enabled)
            return;

        new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
            public void run() {
                try {
                    loopingMusic = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResource("/sounds" + url));
                    if(View.debugging)
                        System.out.println("MUSIC: "+"/sounds" + url);
                    loopingMusic.open(inputStream);
                    loopingMusic.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (Exception ignre) {
                }
            }
        }).start();
    }

    public static void stopLoopingMusic(){
        if(loopingMusic!=null && loopingMusic.isRunning())
            loopingMusic.stop();
    }
}
