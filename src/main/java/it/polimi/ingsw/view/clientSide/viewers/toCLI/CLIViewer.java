package it.polimi.ingsw.view.clientSide.viewers.toCLI;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;


public class CLIViewer extends Viewer {

    public CLIViewer(){
        Viewer.registerViewer(this);
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
