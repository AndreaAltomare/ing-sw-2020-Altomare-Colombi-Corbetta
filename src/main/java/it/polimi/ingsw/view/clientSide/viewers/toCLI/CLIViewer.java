package it.polimi.ingsw.view.clientSide.viewers.toCLI;

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
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.*;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.undoUtility.CLICheckWrite;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.undoUtility.CLIStopTimeScanner;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;
import it.polimi.ingsw.view.exceptions.NotFoundException;

import java.util.Scanner;

public class CLIViewer extends Viewer{

    private CLIStatusViewer cliStatusViewer = null;

    public CLIViewer(){
        Viewer.registerViewer(this);
    }


    //todo: check it in the after simulation test if the refresh message are always after change subStatus message, in it isn't
    // change refresh() and prepareSubStatus()
    /**
     * For all the subTurnStatus which use the board, this method checks the subTurnStatus and shows it if it shows the board
     */
    @Override
    public void refresh() {

        if ( cliStatusViewer != null) {
            try {
                if (ViewSubTurn.getActual() == ViewSubTurn.PLACEWORKER && ViewPlayer.searchByName(ViewNickname.getMyNickname()).getWorkers()[1] == null)
                    return;
            } catch (NotFoundException ignore) {
                cliStatusViewer.show();
            }
            if (!ViewSubTurn.getActual().isMyTurn()) {
                cliStatusViewer.show();
            }
        }

    }

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
     * @param queuedEvent
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
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 1);
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", 7); //starting space
                    System.out.println(ANSIStyle.RED.getEscape() + "[Error Message]: " + viewMessage.getPayload() + ANSIStyle.RESET);
                    if ( this.cliStatusViewer != null && !this.isEnqueuedType(ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN) && !this.isEnqueuedType(ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS)) {
                        cliStatusViewer.show();
                    }
                    break;
                case FATAL_ERROR_MESSAGE:
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 1);
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", 7); //starting space
                    System.out.println(ANSIStyle.RED.getEscape() + "[Fatal Error Message]: " + viewMessage.getPayload() + ANSIStyle.RESET);
                    end = true;
                    break;
                case FROM_SERVER_MESSAGE:
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 1);
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", 7); //starting space
                    System.out.println(ANSIStyle.GREEN.getEscape() + "[Server Message]: " + viewMessage.getPayload() + ANSIStyle.RESET);
                    break;
                default:
                    ;
            }
        }
        return end;
    }

    private void undo() {
        final int STARTING_SPACE = 7;
        final String WRITE_MARK = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() + ANSIStyle.RESET;


        CLICheckWrite cliCheckWrite = new CLICheckWrite();
        int waitingTime = 5; // in sec
        Thread stopScannerThread = new Thread( new CLIStopTimeScanner(cliCheckWrite, waitingTime));
        String input;

        ViewBoard.getBoard().toCLI();       // print the board to see last move1


        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        stopScannerThread.start();
        System.out.printf("Press ENTER bottom in %d second to undo your move:\n", waitingTime);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.print( WRITE_MARK );
        input = new Scanner(System.in).nextLine();
        if ( cliCheckWrite.firstToWrite() ) {
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
                        this.refresh(); //todo: active after connection test if it is necessary
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

            } catch (EmptyQueueException e) {
                ;
            }
        }

        //this.exit();
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
