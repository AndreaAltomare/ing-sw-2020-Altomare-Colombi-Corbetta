package it.polimi.ingsw.view.clientSide.viewers.toCLI;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.*;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;

import java.util.HashMap;
import java.util.Map;

public class CLIViewer extends Viewer{

    private static Map<ViewPlayer, String> colorMap = new HashMap<>(3);
    private CLIStatusViewer cliStatusViewer = null;

    public CLIViewer(){
        Viewer.registerViewer(this);
    }

    /**
     * Adds in workerMap the viewPLayer as key and a string which represents  player's ANSIStyle
     * if it is possible and if there are enough string color, then returns that string color if it is correctly added or null if it isn't
     *
     * @param viewPlayer ViewPlayer to assign a Symbols for his worker
     * @return the string color assigned if it is correctly assigned, ANSIStyle.RESET if it isn't
     */
    public String assignPlayerColor(ViewPlayer viewPlayer) {
        int size;
        String playerStyle = null;

        size = colorMap.size();
        switch (size) {
            case 0:
                playerStyle = colorMap.put(viewPlayer, ANSIStyle.RED.getEscape());
                break;
            case 1:
                playerStyle = colorMap.put(viewPlayer, ANSIStyle.YELLOW.getEscape());
                break;
            case 2:
                playerStyle = colorMap.put(viewPlayer, ANSIStyle.PURPLE.getEscape());
                break;
            default:
                playerStyle = colorMap.put(viewPlayer, ANSIStyle.RESET);
                break;
        }

        return playerStyle;
    }

    /**
     * Returns the string color assigned to viewPlayer or ANSIStyle.RESET if there isn't a string color assigned to viewPLayer
     *
     * @param viewPlayer ViewPlayer with a worker's Symbols assigned
     * @return string color assigned to viewPlayer if viewPlayer has an assigned color, ANSIStyle.RESET if it haven't
     */
    public static String getPlayerColor(ViewPlayer viewPlayer) {
        String playerColor = colorMap.get(viewPlayer);

        if (playerColor == null) {
            playerColor = ANSIStyle.RESET;
        }

        return playerColor;
    }

    //todo: check it in the after simulation test if the refresh message are always after change subStatus message, in it isn't
    // change refresh() and prepareSubStatus()
    /**
     * For all the subTurnStatus which use the board, this method checks the subTurnStatus and shows it if it shows the board
     */
    @Override
    public void refresh() {

        if ( cliStatusViewer != null ) {
            if ( cliStatusViewer.getViewStatus() != null ) {
                if ( cliStatusViewer.getMyCLISubTurnViewer() != null ) {
                    switch ( cliStatusViewer.getMyCLISubTurnViewer().getSubTurn() ) {
                        case PLACEWORKER:
                        case OPPONENT_PLACEWORKER:
                        case SELECTWORKER:
                        case OPPONENT_SELECTWORKER:
                        case MOVE:
                        case OPPONENT_MOVE:
                        case BUILD:
                        case OPPONENT_BUILD:
                            cliStatusViewer.show();
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
        CLIStatusViewer cliStatusViewer;

        if ( statusViewer != null) {
            cliStatusViewer = statusViewer.toCLI();
            if ( cliStatusViewer != null ) {
                this.cliStatusViewer = cliStatusViewer;
                this.cliStatusViewer.setMyCLIViewer( this );
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
                switch ( cliSubTurnViewer.getSubTurn() ) {
                    case PLACEWORKER:
                    case OPPONENT_PLACEWORKER:
                        if ( this.cliStatusViewer.getViewStatus() == ViewStatus.GAME_PREPARATION ) {
                            this.cliStatusViewer.setMyCLISubTurnViewer(cliSubTurnViewer);
                            this.cliStatusViewer.show();
                        }
                        break;
                    case SELECTWORKER:
                    case OPPONENT_SELECTWORKER:
                    case MOVE:
                    case OPPONENT_MOVE:
                    case BUILD:
                    case OPPONENT_BUILD:
                        if ( this.cliStatusViewer.getViewStatus() == ViewStatus.PLAYING ) {
                            this.cliStatusViewer.setMyCLISubTurnViewer(cliSubTurnViewer);
                            this.cliStatusViewer.show();
                        }
                        break;
                    default:
                        ;
                }
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

        if(cliStatusViewer.getViewStatus() == ViewStatus.GAME_PREPARATION) {
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
//                    if (this.cliStatusViewer.getViewStatus() == ViewStatus.GAME_OVER) { //todo: probably they are in ViewStatus.PLAYNING, ask it
                        this.cliStatusViewer.setMyCLISubTurnViewer( new CLIWinPhase() );
                        this.cliStatusViewer.show();
//                    }
                    break;
                case LOOSE_MESSAGE:
//                    if (this.cliStatusViewer.getViewStatus() == ViewStatus.GAME_OVER) {
                        this.cliStatusViewer.setMyCLISubTurnViewer( new CLILoosePhase() );
                        this.cliStatusViewer.show();
//                    }
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
                        //this.refresh(); //todo: active after connection test
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
