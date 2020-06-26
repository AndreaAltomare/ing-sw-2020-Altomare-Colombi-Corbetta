package it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds;

import it.polimi.ingsw.view.clientSide.View;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Class that is intended to manage the sound on the GUI
 */
public abstract class SoundEffect {
    private static boolean enabled = true;
    private static Clip loopingMusic;
    private static String loopingSong;

    /**
     * Setter for the enabled.
     * If _enabled  == true, then the music will play, else there will be no music.
     * The state of enabled is by default true, and when changed it'll remain changed till the end of the application or another change.
     *
     * If the sound is playing and _enabled=false it'll be stopped and, if the sound is looping.
     * If the sound is not playing and _enabled=true it'll enable the sound later and will start playing the looping sound suspended.
     *
     * @param _enabled (boolean, true to play the music, false not to lay it)
     */
    public static void setEnabled(boolean _enabled){
        enabled = _enabled;
        if(!enabled)
            stopLoopingMusic();
        else{
            startLoopMusic(loopingSong);
        }
    }

    /**
     * Getter of the enabled value (true iif the music will play, false if the music cannot be played).
     *
     * @return (boolean indicating if the music is muted -false- or not -true-)
     */
    public static boolean getEnabled(){
        return enabled;
    }

    /**
     * Method to play a single time a sound.
     *
     * @param url (The filename of the sound to play).
     */
    public static synchronized void playSound( String url) {
        if (enabled)
            new Thread(new Runnable() {
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

    /**
     * Method to start a sound in looping play.
     * If the music is turned off (or will be turned of while playing it), when sound returns on, it'll start playing it.
     *
     * @param url (the url of the sound).
     */
    public static synchronized void startLoopMusic(String url){
        loopingSong = url;

        if(loopingMusic!=null && loopingMusic.isRunning())
            loopingMusic.stop();

        if(!enabled)
            return;

        new Thread(new Runnable() {
            public void run() {
                try {
                    loopingMusic = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResource("/sounds" + url));
                    if(View.debugging)
                        System.out.println("MUSIC: "+"/sounds" + url);
                    loopingMusic.open(inputStream);
                    loopingMusic.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (Exception ignore) {
                }
            }
        }).start();
    }

    /**
     * Method to stop the looping music that is playing.
     */
    public static void stopLoopingMusic(){
        if(loopingMusic!=null && loopingMusic.isRunning())
            loopingMusic.stop();
    }
}
