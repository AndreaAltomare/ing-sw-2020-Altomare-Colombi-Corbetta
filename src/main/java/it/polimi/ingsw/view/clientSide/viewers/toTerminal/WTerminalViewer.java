package it.polimi.ingsw.view.clientSide.viewers.toTerminal;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.UndoExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.undoUtility.CLICheckWrite;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.undoUtility.CLIStopTimeScanner;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalChooseCardsPhase;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalLoosePhase;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalSelectMyCardPhase;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalWinPhase;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.undoUtility.WTerminalCheckWrite;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.undoUtility.WTerminalStopTimeScanner;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;
import it.polimi.ingsw.view.exceptions.NotFoundException;

import java.util.Scanner;


public class WTerminalViewer extends Viewer {

    private WTerminalStatusViewer wTerminalStatusViewer = null;

    public WTerminalViewer(){
        Viewer.registerViewer(this);
    }

    //todo: check it in the after simulation test if the refresh message are always after change subStatus message, in it isn't
    // change refresh() and prepareSubStatus()
    /**
     * For all the subTurnStatus which use the board, this method checks the subTurnStatus and shows it if it shows the board
     */
    @Override
    public void refresh() {

        if ( wTerminalStatusViewer != null) {
            try {
                if (ViewSubTurn.getActual() == ViewSubTurn.PLACEWORKER && ViewPlayer.searchByName(ViewNickname.getMyNickname()).getWorkers()[1] == null)
                    return;
            } catch (NotFoundException ignore) {
                wTerminalStatusViewer.show();
            }
            if (!ViewSubTurn.getActual().isMyTurn()) {
                wTerminalStatusViewer.show();
            }
        }

    }

    private void prepareStatusViewer(ViewerQueuedEvent queuedEvent) {
        StatusViewer statusViewer = (StatusViewer) queuedEvent.getPayload();
        WTerminalStatusViewer wTerminalStatusViewer;

        if ( statusViewer != null) {
            wTerminalStatusViewer = statusViewer.toWTerminal();
            if ( wTerminalStatusViewer != null ) {
                this.wTerminalStatusViewer = wTerminalStatusViewer;
                this.wTerminalStatusViewer.show();
            }
        }
    }

    /**
     * Checks the ViewerQueuedEvent when its type is SET_SUBTURN, then adds its WTerminalSubTurn (if there is) to WTerminalStatus (if it is correct)
     * @param queuedEvent
     */
    private void prepareSubTurnViewer(ViewerQueuedEvent queuedEvent) {
        SubTurnViewer subTurnViewer = (SubTurnViewer) queuedEvent.getPayload();
        WTerminalSubTurnViewer WTerminalSubTurnViewer;

        if ( subTurnViewer != null) {
            WTerminalSubTurnViewer = subTurnViewer.toWTerminal();
            if ( WTerminalSubTurnViewer != null) {
                this.wTerminalStatusViewer.setMyWTerminalSubTurnViewer(WTerminalSubTurnViewer);
                this.wTerminalStatusViewer.show();
            }
        }
    }

    /**
     * Sets and shows the correct WTerminalSubTurnViewer between ChooseCardsPhase and SelectMyCardsPhase using the length of
     * cardSelection's List in queuedEvent's payload when viewStatus == GAME_PREPARATION, or it doesn't anything for other cases
     * @param queuedEvent Event to read ( after check that his Type == CARDSELECTION )
     */
    private void prepareCardsPhase(ViewerQueuedEvent queuedEvent) {
        CardSelection cardSelection;

        cardSelection = (CardSelection) queuedEvent.getPayload();
        if ( cardSelection != null) {
            if (cardSelection.getCardList().size() > ViewPlayer.getNumberOfPlayers()) {
                this.wTerminalStatusViewer.setMyWTerminalSubTurnViewer(new WTerminalChooseCardsPhase(cardSelection));
            } else {
                this.wTerminalStatusViewer.setMyWTerminalSubTurnViewer(new WTerminalSelectMyCardPhase(cardSelection));
            }
            this.wTerminalStatusViewer.show();
        }
    }

    private void undo() {
        final int STARTING_SPACE = 7;


        WTerminalCheckWrite wTerminalCheckWrite = new WTerminalCheckWrite();
        int waitingTime = 5; // in sec
        Thread stopScannerThread = new Thread( new WTerminalStopTimeScanner(wTerminalCheckWrite, waitingTime));
        String input;

        ViewBoard.getBoard().toCLI();       // print the board to see last move1

        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        stopScannerThread.start();
        System.out.printf("Press ENTER bottom in %d second to undo your move:\n", waitingTime);
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.print( ">>" );
        input = new Scanner(System.in).nextLine();
        if ( wTerminalCheckWrite.firstToWrite() ) {
            try {
                UndoExecuter.undoIt();
                System.out.println("[CLIMessage]: used undoExecuter"); //todo:remove after testing
            } catch (CannotSendEventException e) {
            }
        } else {
            System.out.println("[CLIMessage]: time over, play continues"); //todo:remove after testing
            try {
                Thread.sleep(750);
            } catch (InterruptedException ignored) {
            }
            if (!this.isEnqueuedType(ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN)) {
                this.setSubTurnViewer(ViewSubTurn.getActual().getSubViewer());
            }
        }
    }

    /**
     * Sets and shows the correct representation of the message on the WTerminal using some WTerminalStatusView or WTerminalSubTurnView
     * if it is necessary
     *
     * @param queuedEvent Event to read ( after check that its Type == MESSAGE )
     */
    private boolean prepareMessage(ViewerQueuedEvent queuedEvent) {
        ViewMessage viewMessage = (ViewMessage) queuedEvent.getPayload();
        boolean end = false;

        if (viewMessage != null) {
            switch ( viewMessage.getMessageType() ) {
                case WIN_MESSAGE:
                    this.wTerminalStatusViewer.setMyWTerminalSubTurnViewer(new WTerminalWinPhase());
                    this.wTerminalStatusViewer.show();
                    break;
                case LOOSE_MESSAGE:
                    this.wTerminalStatusViewer.setMyWTerminalSubTurnViewer(new WTerminalLoosePhase());
                    this.wTerminalStatusViewer.show();
                    break;
                case FROM_SERVER_ERROR:
                    PrintFunction.printRepeatString("\n", 1);
                    PrintFunction.printRepeatString(" ", 7); // starting space
                    System.out.println("[Error Message]: " + viewMessage.getPayload());
                    if ( this.wTerminalStatusViewer != null && !this.isEnqueuedType(ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN) && !this.isEnqueuedType(ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS)) {
                        wTerminalStatusViewer.show();
                    }
                    break;
                case FATAL_ERROR_MESSAGE:
                    PrintFunction.printRepeatString("\n", 1);
                    PrintFunction.printRepeatString(" ", 7); // starting space
                    System.out.println("[Fatal Error Message]: " + viewMessage.getPayload());
                    end = true;
                    break;
                case FROM_SERVER_MESSAGE:
                    PrintFunction.printRepeatString("\n", 1);
                    PrintFunction.printRepeatString(" ", 7); // starting space
                    System.out.println("[Server Message]: " + viewMessage.getPayload());
                    break;
                default:
                    ;
            }
        }

        return  end;
    }

    @Override
    public void run() {
        ViewerQueuedEvent queuedEvent;
        boolean end = false;

        while ( !end ) {

            try {
                queuedEvent = this.getNextEvent();

                switch (queuedEvent.getType()) {
                    case EXIT:
                        end = true;
                        break;
                    case SET_STATUS:
                        this.prepareStatusViewer(queuedEvent);
                        break;
                    case SET_SUBTURN:
                        this.prepareSubTurnViewer(queuedEvent);
                        break;
                    case CARDSELECTION:
                        this.prepareCardsPhase(queuedEvent);
                        break;
                    case REFRESH:
                        this.refresh();
                        break;
                    case MESSAGE:
                        this.prepareMessage(queuedEvent);
                        break;
                    case UNDO:
                        this.undo();
                        break;
                    default:
                        ;
                }

            } catch (EmptyQueueException e) {
                ;
            }
        }

        // this.exit    eliminate if it isn't necessary
        Viewer.exitAll();

    }

    @Override
    protected void enqueue(ViewerQueuedEvent viewerQueuedEvent) {
        if ( viewerQueuedEvent.getType() == ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN && isEnqueuedType(ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN)) {
            return;
        } else {
            super.enqueue(viewerQueuedEvent);
        }
    }

}
