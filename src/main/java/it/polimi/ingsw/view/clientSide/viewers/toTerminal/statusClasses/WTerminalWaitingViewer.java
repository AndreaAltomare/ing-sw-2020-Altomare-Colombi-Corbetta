package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.statusViewers.WaitingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;

/**
 * Class that represents the <code>WTerminalStatusViewer</code> Waiting on the Windows Terminal
 * using methods of <code>PrintFunction</code>
 *
 * @see WTerminalStatusViewer
 * @see PrintFunction
 * @author Marco
 */
public class WTerminalWaitingViewer extends WTerminalStatusViewer {

    private WaitingViewer waitingViewer;

    final String WAITING_MESSAGE = "Please waiting ";
    final int SLEEP_TIME = 1000; //in ns
    final int NUMBER_OF_TIME = 3;

    /**
     * Constructor to set correct <code>StatusViewer</code>
     *
     * @see WaitingViewer
     * @param waitingViewer  <code>StatusViewer</code> linked at this class
     */
    public WTerminalWaitingViewer(WaitingViewer waitingViewer) {
        this.waitingViewer = waitingViewer;
    }

    /**
     * Prints a message of waiting,
     * then prints some points at regular time intervals like waiting animation
     */
    @Override
    public void show() {
        PrintFunction.printRepeatString("\n", 2);
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print( WAITING_MESSAGE );
        for (int i = 0; i < NUMBER_OF_TIME; i++) {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
            }
            System.out.print(".");
        }
        System.out.println();
    }
}
