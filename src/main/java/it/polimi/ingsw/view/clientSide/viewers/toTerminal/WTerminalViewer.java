package it.polimi.ingsw.view.clientSide.viewers.toTerminal;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLILoosePhase;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLIWinPhase;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.Symbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalChooseCardsPhase;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalLoosePhase;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalSelectMyCardPhase;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalWinPhase;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;

import java.util.HashMap;
import java.util.Map;


public class WTerminalViewer extends Viewer {

    private it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer WTerminalStatusViewer = null;

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

        if ( WTerminalStatusViewer != null ) {
            if ( WTerminalStatusViewer.getViewStatus() != null ) {
                if ( WTerminalStatusViewer.getMyWTerminalSubTurnViewer() != null ) {
                    switch ( WTerminalStatusViewer.getMyWTerminalSubTurnViewer().getSubTurn() ) {
                        case PLACEWORKER:
                        case OPPONENT_PLACEWORKER:
                        case SELECTWORKER:
                        case OPPONENT_SELECTWORKER:
                        case MOVE:
                        case OPPONENT_MOVE:
                        case BUILD:
                        case OPPONENT_BUILD:
                           WTerminalStatusViewer.show();
                           break;
                        default:
                            ;
                    }
                }
            }
        }


    }

    private void prepareStatusViewer(ViewerQueuedEvent queuedEvent) {
        StatusViewer statusViewer = (StatusViewer) queuedEvent.getPayload();
        it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer WTerminalStatusViewer;

        if ( statusViewer != null) {
            WTerminalStatusViewer = statusViewer.toWTerminal();
            if ( WTerminalStatusViewer != null ) {
                this.WTerminalStatusViewer = WTerminalStatusViewer;
                this.WTerminalStatusViewer.setMyWTerminalViewer( this );
                this.WTerminalStatusViewer.show();
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
                switch ( WTerminalSubTurnViewer.getSubTurn() ) {
                    case PLACEWORKER:
                    case OPPONENT_PLACEWORKER:
                        if ( this.WTerminalStatusViewer.getViewStatus() == ViewStatus.GAME_PREPARATION ) {
                            this.WTerminalStatusViewer.setMyWTerminalSubTurnViewer(WTerminalSubTurnViewer);
                            this.WTerminalStatusViewer.show();
                        }
                        break;
                    case SELECTWORKER:
                    case OPPONENT_SELECTWORKER:
                    case MOVE:
                    case OPPONENT_MOVE:
                    case BUILD:
                    case OPPONENT_BUILD:
                        if ( this.WTerminalStatusViewer.getViewStatus() == ViewStatus.PLAYING ) {
                            this.WTerminalStatusViewer.setMyWTerminalSubTurnViewer(WTerminalSubTurnViewer);
                            this.WTerminalStatusViewer.show();
                        }
                        break;
                    default:
                        ;
                }
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

        if(WTerminalStatusViewer.getViewStatus() == ViewStatus.GAME_PREPARATION) {
            cardSelection = (CardSelection) queuedEvent.getPayload();
            if ( cardSelection != null) {
                if (cardSelection.getCardList().size() > ViewPlayer.getNumberOfPlayers()) {
                    this.WTerminalStatusViewer.setMyWTerminalSubTurnViewer( new WTerminalChooseCardsPhase(cardSelection) );
                } else {
                    this.WTerminalStatusViewer.setMyWTerminalSubTurnViewer( new WTerminalSelectMyCardPhase(cardSelection) );
                }
                this.WTerminalStatusViewer.show();
            }
        }
    }

    /**
     * Sets and shows the correct representation of the message on the WTerminal using some WTerminalStatusView or WTerminalSubTurnView
     * if it is necessary
     *
     * @param queuedEvent Event to read ( after check that its Type == MESSAGE )
     */
    private void prepareMessage(ViewerQueuedEvent queuedEvent) {
        ViewMessage viewMessage = (ViewMessage) queuedEvent.getPayload();

        if (viewMessage != null) {
            switch ( viewMessage.getMessageType() ) {
                case WIN_MESSAGE:
                    if ( this.WTerminalStatusViewer.getViewStatus() == ViewStatus.PLAYING ) {
                        this.WTerminalStatusViewer.setMyWTerminalSubTurnViewer(new WTerminalWinPhase());
                        this.WTerminalStatusViewer.show();
                    }
                    break;
                case LOOSE_MESSAGE:
                    if ( this.WTerminalStatusViewer.getViewStatus() == ViewStatus.PLAYING ) {
                        this.WTerminalStatusViewer.setMyWTerminalSubTurnViewer(new WTerminalLoosePhase());
                        this.WTerminalStatusViewer.show();
                    }
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
                        //this.refresh();
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
