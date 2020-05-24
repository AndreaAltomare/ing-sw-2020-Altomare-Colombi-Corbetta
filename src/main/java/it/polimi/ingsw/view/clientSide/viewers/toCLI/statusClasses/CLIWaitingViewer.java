package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.statusViewers.WaitingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;

public class CLIWaitingViewer extends CLIStatusViewer {

    private WaitingViewer waitingViewer;

    final int SLEEP_TIME = 1000; //in ns
    final int NUMBER_OF_TIME = 3;

    /**
     * Constructor to set the correct StatusViewer
     * @param waitingViewer
     */
    public CLIWaitingViewer( WaitingViewer waitingViewer) {
        this.waitingViewer = waitingViewer;
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
