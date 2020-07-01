package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.statusViewers.WaitingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;

public class WTerminalWaitingViewer extends WTerminalStatusViewer {

    private WaitingViewer waitingViewer;

    final String WAITING_MESSAGE = "Please waiting ";
    final int SLEEP_TIME = 1000; //in ns
    final int NUMBER_OF_TIME = 3;

    /**
     * Constructor to set the correct StatusViewer
     * @param waitingViewer
     */
    public WTerminalWaitingViewer(WaitingViewer waitingViewer) {
        this.waitingViewer = waitingViewer;
    }

    /**
     * Prints a message of waiting,
     * then prints some points at regular time intervals like a waiting animation
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
