package it.polimi.ingsw.view.clientSide.viewers.toCLI.undoUtility;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Class which implements <code>Runnable</code> used to simulate a press enter bottom after a chosen time
 *
 * @author Marco
 */
public class CLIStopTimeScanner implements Runnable {

    private CLICheckWrite cliCheckWrite;
    private int waitingTime; // in ms

    /**
     * constructor which set the CLICheckWrite and the waiting time in second
     *
     * @param cliCheckWrite <code>CLICheckWrite</code> licked
     * @param waitingInSec time in sec to sleep before writing
     */
    public CLIStopTimeScanner(CLICheckWrite cliCheckWrite, int waitingInSec ) {
        this.cliCheckWrite = cliCheckWrite;
        this.waitingTime = waitingInSec * 1000;
    }

    /**
     * uses a robot to simulate the press of enter bottom after waiting time and only if this thread is the first
     * to write with cliCheckWrite
     */
    @Override
    public void run() {
        try {
            Robot robot = new Robot();

            Thread.sleep( this.waitingTime );
            if ( this.cliCheckWrite.firstToWrite() ) {
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }

        } catch (AWTException ignored) {
        } catch (InterruptedException ignored) {
        }
    }
}
