package it.polimi.ingsw.view.clientSide.viewers.toCLI;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.Symbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
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

    }

    public void prepareStatusViewer(ViewerQueuedEvent queuedEvent) {
        StatusViewer statusViewer = ((StatusViewer)queuedEvent.getPayload());
        CLIStatusViewer cliStatusViewer;

        if ( statusViewer != null) {
            cliStatusViewer = statusViewer.toCLI();
            if ( cliStatusViewer != null ) {
                this.cliStatusViewer = cliStatusViewer;
                //todo:Valutare se è necessario attribuire a CliStatusViewer il CliViewer
                cliStatusViewer.setMyCLIViewer( this );
                cliStatusViewer.show();
            }
        }
    }

    @Override
    public void setSubTurnViewer(SubTurnViewer subTurnViewer) {

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

                    default:
                        ;
                }

            } catch (EmptyQueueException e) {
                ;
            }
        }


    }

}
