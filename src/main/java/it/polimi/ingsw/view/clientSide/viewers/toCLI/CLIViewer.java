package it.polimi.ingsw.view.clientSide.viewers.toCLI;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.*;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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

        if ( cliStatusViewer != null ) {
            cliStatusViewer.show();
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
        SubTurnViewer subTurnViewer = (SubTurnViewer) queuedEvent.getPayload();
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
    private void prepareMessage(ViewerQueuedEvent queuedEvent) {
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
                case FATAL_ERROR_MESSAGE:
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 1);
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", 7); //starting space
                    System.out.println(ANSIStyle.RED + "[Error Message]: " + viewMessage.getPayload() + ANSIStyle.RESET);
//                    if ( this.cliStatusViewer != null ) {
//                        cliStatusViewer.show();
//                    }
                    break;
                case FROM_SERVER_MESSAGE:
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 1);
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", 7); //starting space
                    System.out.println(ANSIStyle.GREEN + "[Server Message]: " + viewMessage.getPayload() + ANSIStyle.RESET);
                    break;
                default:
                    ;
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
                        //this.refresh(); //todo: active after connection test if it is necessary
                        break;
                    case MESSAGE:
                        this.prepareMessage(queuedEvent);
                        break;
                    default:
                        ;
                }

            } catch (EmptyQueueException e) {
                ;
            }
        }

        this.exit();

    }

}
