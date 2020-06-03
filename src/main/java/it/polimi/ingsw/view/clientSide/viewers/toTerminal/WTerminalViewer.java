package it.polimi.ingsw.view.clientSide.viewers.toTerminal;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.Symbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalChooseCardsPhase;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalSelectMyCardPhase;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;

import java.util.HashMap;
import java.util.Map;


public class WTerminalViewer extends Viewer {

    private static Map<ViewPlayer, Symbols> workerMap = new HashMap<>(3);
    private it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer WTerminalStatusViewer = null;

    public WTerminalViewer(){
        Viewer.registerViewer(this);
    }

    /**
     * Adds in workerMap the viewPLayer as key and a Symbols which represents worker's image of that player
     * if it is possible and if there are enough Symbols for workers, then returns that Symbols if it is correctly added or null if it isn't
     * @param viewPlayer ViewPlayer to assign a Symbols for his worker
     * @return the Symbols assigned if it is correctly assigned, null if it isn't
     */
    public Symbols assignWorkerSymbol(ViewPlayer viewPlayer) {
        int size;
        Symbols workerSymbol = null;

        size = workerMap.size();
        switch (size) {
            case 0:
                workerSymbol = workerMap.put(viewPlayer, Symbols.WORKER_1);
                break;
            case 1:
                workerSymbol= workerMap.put(viewPlayer, Symbols.WORKER_2);
                break;
            case 2:
                workerSymbol= workerMap.put(viewPlayer, Symbols.WORKER_3);
                break;
            default:
                ;
        }

        return workerSymbol;
    }

    /**
     * Returns the Symbols assigned to viewPlayer or null if there isn't a Symbols assigned to viewPLayer
     * @param viewPlayer ViewPlayer with a worker's Symbols assigned
     * @return Symbols assigned to viewPlayer if viewPlayer has an assigned Symbols, null if it haven't
     */
    public static Symbols getWorkerSymbol(ViewPlayer viewPlayer) {
        Symbols workerSymbol = workerMap.get(viewPlayer);

        return workerSymbol;
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
