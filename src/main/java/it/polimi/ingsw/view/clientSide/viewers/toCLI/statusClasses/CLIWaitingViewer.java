package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.statusViewers.WaitingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;

public class CLIWaitingViewer extends CLIStatusViewer {

    private WaitingViewer waitingViewer;

    final String WAITING_MESSAGE = "Please waiting ";
    final int SLEEP_TIME = 1000; //in ns

    /**
     * Constructor to set the correct StatusViewer
     * @param waitingViewer
     */
    public CLIWaitingViewer(WaitingViewer waitingViewer) {
        this.waitingViewer = waitingViewer;
    }

    /**
     * Prints a message of waiting,
     * then prints some colored block at regular time intervals like a waiting animation
     */
    @Override
    public void show() {
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.print( WAITING_MESSAGE );
        //animation
        try {
            Thread.sleep(SLEEP_TIME);
            System.out.print( ANSIStyle.RED.getEscape() + UnicodeSymbol.HIGH_BLOCK_2.getEscape());
            Thread.sleep(SLEEP_TIME);
            System.out.print( ANSIStyle.YELLOW.getEscape() + UnicodeSymbol.HIGH_BLOCK_5.getEscape());
            Thread.sleep(SLEEP_TIME);
            System.out.print( ANSIStyle.GREEN.getEscape() + UnicodeSymbol.HIGH_BLOCK_8.getEscape());
            } catch (InterruptedException ignored) {
        }
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
    }
}
