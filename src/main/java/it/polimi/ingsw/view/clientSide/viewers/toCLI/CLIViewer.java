package it.polimi.ingsw.view.clientSide.viewers.toCLI;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.Symbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLIChooseCardsPhase;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLISelectMyCardPhase;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;

import java.util.HashMap;
import java.util.Map;


public class CLIViewer extends Viewer {

    private static Map<ViewPlayer, Symbols> workerMap = new HashMap<>(3);
    private CLIStatusViewer cliStatusViewer = null;

    public CLIViewer(){
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

    @Override
    public void refresh() {

        System.out.println();
        System.out.println();
        ViewBoard.getBoard().toCLI();
        System.out.println();

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
     * @param queuedEvent Event to read ( after check that his Type == CARDSELECTION )
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
//                        this.refresh();
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
