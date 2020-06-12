package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.WaitingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;

public class WTerminalWaitingViewer extends WTerminalStatusViewer {

    private final ViewStatus viewStatus = ViewStatus.WAITING;
    private WaitingViewer waitingViewer;

    final int SLEEP_TIME = 1000; //in ns
    final int NUMBER_OF_TIME = 3;

    /**
     * Constructor to set the correct StatusViewer
     * @param waitingViewer
     */
    public WTerminalWaitingViewer(WaitingViewer waitingViewer) {
        this.waitingViewer = waitingViewer;
    }

    @Override
    public ViewStatus getViewStatus() {
        return viewStatus;
    }

    /**
     * Prints a message of waiting,
     * then prints some points at regular time intervals like a waiting animation
     */
    @Override
    public void show() {
        System.out.print(   "\n\t" +
                            "Please waiting");
        for (int i = 0; i < NUMBER_OF_TIME; i++) {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace(); //todo: eliminate after testing
            }
            System.out.print(".");
        }
        System.out.println();
    }
}