package it.polimi.ingsw.view.clientSide.viewers.toCLI;

import it.polimi.ingsw.view.clientSide.View;
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
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLIChooseCardsPhase;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLILoosePhase;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLISelectMyCardPhase;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLIWinPhase;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;
import it.polimi.ingsw.view.exceptions.NotFoundException;

import java.io.IOException;

/**
 * Class which extend <code>Viewer</code> and menage the CLI interface
 *
 * @see Viewer
 * @author Marco
 */
public class CLIViewer extends Viewer{

    private CLIStatusViewer cliStatusViewer = null;

    /**
     * Constructor which registers this class on <code>Viewer</code> list
     */
    public CLIViewer(){
        Viewer.registerViewer(this);
    }

    /**
     * For all the subTurnStatus which use the board, this method checks the subTurnStatus and shows it if it isn't player turn
     * or if he/she must place another worker
     */
    @Override
    public void refresh() {
        if(View.debugging)
            System.out.print("REFRESHING");

        if ( cliStatusViewer != null) {
            try {
                if (ViewSubTurn.getActual() == ViewSubTurn.PLACEWORKER && ViewPlayer.searchByName(ViewNickname.getMyNickname()).getWorkers()[1] == null)
                    return;
            } catch (NotFoundException ignore) {
                cliStatusViewer.show();
            }
            if (!ViewSubTurn.getActual().isMyTurn()) {
                if(View.debugging)
                    System.out.print("SHOWING");
                cliStatusViewer.setMyCLISubTurnViewer(ViewSubTurn.getActual().getSubViewer().toCLI());
                cliStatusViewer.show();
            }
        }

    }

    /**
     * Read the <code>ViewerQueuedEvent</code> and if contains a <code>StatusViewer</code> set and show it
     *
     * @see CLIStatusViewer
     * @param queuedEvent <code>ViewerQueuedEvent</code> read ( after check that his Type == SET_TURN )
     */
    private void prepareStatusViewer(ViewerQueuedEvent queuedEvent) {
        StatusViewer statusViewer = (StatusViewer) queuedEvent.getPayload();
        CLIStatusViewer cliStatusViewer;

        if ( statusViewer != null) {
            cliStatusViewer = statusViewer.toCLI();
            if ( cliStatusViewer != null ) {
                this.cliStatusViewer = cliStatusViewer;
                this.cliStatusViewer.show();
            }
        }
    }

    /**
     * Checks the ViewerQueuedEvent when its type is SET_SUBTURN, then adds its CLISubTurn (if there is) to CLIStatus (if it is correct)
     *
     * @see CLISubTurnViewer
     * @param queuedEvent <code>ViewerQueuedEvent</code> read ( after check that his Type == SET_SUBTURN )
     */
    private void prepareSubTurnViewer(ViewerQueuedEvent queuedEvent) {
        SubTurnViewer subTurnViewer;
        try {
            subTurnViewer = ViewSubTurn.getActual().getSubViewer();
        } catch (NullPointerException e) {
            subTurnViewer = (SubTurnViewer) queuedEvent.getPayload();
        }
        CLISubTurnViewer cliSubTurnViewer;

        if ( subTurnViewer != null) {
            cliSubTurnViewer = subTurnViewer.toCLI();
            if ( cliSubTurnViewer != null) {
                this.cliStatusViewer.setMyCLISubTurnViewer(cliSubTurnViewer);
                this.cliStatusViewer.show();
            }
        }

    }

    /**
     * Sets and shows the correct CLISubTurnViewer between ChooseCardsPhase and SelectMyCardsPhase using the length of
     * cardSelection's List in queuedEvent's payload when viewStatus == GAME_PREPARATION, or it doesn't anything for other cases
     *
     * @see CardSelection
     * @param queuedEvent Event to read ( after check that its Type == CARDSELECTION )
     */
    private void prepareCardsPhase(ViewerQueuedEvent queuedEvent) {
        CardSelection cardSelection;

        cardSelection = (CardSelection) queuedEvent.getPayload();
        if ( cardSelection != null) {
            if (cardSelection.getCardList().size() > ViewPlayer.getNumberOfPlayers()) {
                this.cliStatusViewer.setMyCLISubTurnViewer( new CLIChooseCardsPhase(cardSelection) );
            } else {
                this.cliStatusViewer.setMyCLISubTurnViewer( new CLISelectMyCardPhase(cardSelection) );
            }
            this.cliStatusViewer.show();
        }

    }

    /**
     * Sets and shows the correct representation of the message on the CLI using some CLIStatusView or CLISubTurnView
     * if it is necessary
     *
     * @param queuedEvent Event to read ( after check that its Type == MESSAGE )
     */
    private boolean prepareMessage(ViewerQueuedEvent queuedEvent) {
        boolean end = false;
        ViewMessage viewMessage = (ViewMessage) queuedEvent.getPayload();

        if (viewMessage != null) {
            switch ( viewMessage.getMessageType() ) {
                case WIN_MESSAGE:
                    this.cliStatusViewer.setMyCLISubTurnViewer( new CLIWinPhase() );
                    this.cliStatusViewer.show();
                    break;
                case LOOSE_MESSAGE:
                    this.cliStatusViewer.setMyCLISubTurnViewer( new CLILoosePhase() );
                    this.cliStatusViewer.show();
                    break;
                case FROM_SERVER_ERROR:
                    CLIPrintFunction.printError("ERROR: " + viewMessage.getPayload());
                    if ( this.cliStatusViewer != null && !this.isEnqueuedType(ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN) && !this.isEnqueuedType(ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS)) {
                        cliStatusViewer.show();
                    }
                    if (viewMessage.getPayload().equals("Socket closed")||viewMessage.getPayload().equals("Connection refused")) {
                        end = true;
                    }
                    break;
                case FATAL_ERROR_MESSAGE:
                    CLIPrintFunction.printError("FATAL ERROR: " + viewMessage.getPayload());
                    end = true;
                    break;
                case FROM_SERVER_MESSAGE:
                    CLIPrintFunction.printCheck(viewMessage.getPayload());
                    break;
                default:
                    ;
            }
        }
        return end;
    }

    /**
     * Menage the undo request waiting the response for some seconds, then if player responds before time, sends
     * an undo request with the executer, or doesn't anything if he/she isn't. Finally notifies the result with a printed message
     *
     * @see UndoExecuter
     */
    private void undo() {

        //CLICheckWrite cliCheckWrite = new CLICheckWrite();
        final String UNDO_ACTIVE_MESSAGE = "Undo request is correctly sent";
        final String UNDO_REJECT_MESSAGE = "The play continues";
        int waitingTime = 5; // in sec
        //Thread stopScannerThread = new Thread( new CLIStopTimeScanner(cliCheckWrite, waitingTime));

        ViewBoard.getBoard().toCLI();       // print the board to see last move1


        System.out.printf("Press ENTER bottom in %d second to undo your move:\n", waitingTime);
        Object obj = new Object();

        synchronized (obj){
            try {
                obj.wait(4000);
            } catch (InterruptedException ignore) {
            }
            try {
                if(System.in.available()>0){
                    try {
                        UndoExecuter.undoIt();
                        CLIPrintFunction.printCheck(UNDO_ACTIVE_MESSAGE);
                        return;
                    } catch (CannotSendEventException e) {
                        CLIPrintFunction.printError(e.getErrorMessage());
                    }
                }
            } catch (IOException ignore) {
            }

            CLIPrintFunction.printCheck(UNDO_REJECT_MESSAGE);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
            }
            if (!this.isEnqueuedType(ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN)) {
                this.setSubTurnViewer(ViewSubTurn.getActual().getSubViewer());
            }
        }

        /*CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        stopScannerThread.start();
        System.out.printf("Press ENTER bottom in %d second to undo your move:\n", waitingTime);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.print( CLIPrintFunction.WRITE_MARK );
        new Scanner(System.in).nextLine();
        if ( cliCheckWrite.firstToWrite() ) {
            try {
                UndoExecuter.undoIt();
                CLIPrintFunction.printCheck(UNDO_ACTIVE_MESSAGE);
            } catch (CannotSendEventException e) {
                CLIPrintFunction.printError(e.getErrorMessage());
            }
        } else {
            CLIPrintFunction.printCheck(UNDO_REJECT_MESSAGE);
            try {
                Thread.sleep(750);
            } catch (InterruptedException ignored) {
            }
            if (!this.isEnqueuedType(ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN)) {
                this.setSubTurnViewer(ViewSubTurn.getActual().getSubViewer());
            }
        }*/
    }

    /**
     * Continues to read the message and calls the correct methods as long as there is an fatal error o and EXIT message
     */
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
                        end = this.prepareMessage(queuedEvent);
                        break;
                    case UNDO:
                        this.undo();
                        break;
                    default:
                        ;
                }

            } catch (EmptyQueueException ignored) {
                ;
            }
        }

        Viewer.exitAll();

    }

    /**
     * Call the super method if the <code>ViewQueuedEvent</code> isn't a SET_SUBTURN Type and there isn't another SET_SUBTURN
     *
     * @param viewerQueuedEvent <code>ViewQueuedEvent</code> to add in read list
     */
    @Override
    protected void enqueue(ViewerQueuedEvent viewerQueuedEvent) {
        if ( viewerQueuedEvent.getType() == ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN && isEnqueuedType(ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN)) {
            return;
        } else {
            super.enqueue(viewerQueuedEvent);
        }
    }

}
