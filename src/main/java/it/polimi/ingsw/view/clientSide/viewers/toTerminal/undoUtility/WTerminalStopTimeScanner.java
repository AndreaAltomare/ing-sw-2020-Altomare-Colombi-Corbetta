package it.polimi.ingsw.view.clientSide.viewers.toTerminal.undoUtility;


import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Class which implements <code>Runnable</code> used to simulate a press enter bottom after a chosen time
 *
 * @author Marco
 */
public class WTerminalStopTimeScanner implements Runnable {

    private WTerminalCheckWrite wTerminalCheckWrite;
    private int waitingTime; // in ms

    /**
     * constructor which set the WTerminalCheckWrite and the waiting time in second
     *
     * @param wTerminalCheckWrite  <code>WTerminalCheckWrite</code> licked
     * @param waitingInSec time in sec to sleep before writing
     */
    public WTerminalStopTimeScanner(WTerminalCheckWrite wTerminalCheckWrite, int waitingInSec ) {
        this.wTerminalCheckWrite = wTerminalCheckWrite;
        this.waitingTime = waitingInSec * 1000;
    }

    /**
     * uses a robot to simulate the press of enter bottom after waiting time and only if this thread is the first
     * to write with WTerminalCheckWrite
     */
    @Override
    public void run() {
        try {
            Robot robot = new Robot();

            Thread.sleep( this.waitingTime );
            if ( this.wTerminalCheckWrite.firstToWrite() ) {
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }

        } catch (AWTException ignored) {
        } catch (InterruptedException ignored) {
        }
    }
}
