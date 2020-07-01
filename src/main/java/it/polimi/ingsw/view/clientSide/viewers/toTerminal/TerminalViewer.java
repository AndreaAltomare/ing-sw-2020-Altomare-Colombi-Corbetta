package it.polimi.ingsw.view.clientSide.viewers.toTerminal;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.exceptions.CheckQueueException;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;

/**
 * <code>Viewer</code> referring to the Terminal and managing it (and its' working).
 *
 * It was used into the first development stages and then left apart.
 * It hasn't been removed because of it's possible utility in the future development.
 */
public class TerminalViewer extends Viewer {
    /**
     * Method doing the Refresh of the Viewer
     */
    @Override
    public void refresh() { }

    /**
     * Constructor.
     */
    public TerminalViewer(){
        super();
        Viewer.registerViewer(this);
    }

    /**
     * Method that adds a ViewerQueuedEvent to the blockingQueue.
     * It'll add all the ViewerQueuedEvent that cannot be executed
     * immediately and so will be retrieved by a different thread.
     *
     * It immediately prints the Messages.
     *
     * @param event (the ViewerQueuedEvent to add to the BlockingQueue).
     */
    @Override
    protected void enqueue(ViewerQueuedEvent event){
        if(event.getType()==ViewerQueuedEvent.ViewerQueuedEventType.MESSAGE){
            new Thread(){
                String mySt;

                Thread setSt(String st){
                    mySt = st;
                    return this;
                }

                public void run(){
                    System.out.println(mySt);
                }
            }.setSt((new String("[Message: " + ((ViewMessage)event.getPayload()).getMessageType() + "]\t" + ((ViewMessage)event.getPayload()).getPayload()))).start();
        }else{
            super.enqueue(event);
        }
    }


    /**
     * MEthod to start, initialise and execute the TerminalViewer.
     */
    @Override
    public void run() {
        ViewerQueuedEvent queued;
        while(true){
            waitNextEvent();

            while(true){
                try {
                    queued = getNextEvent();
                } catch (EmptyQueueException e) {
                    break;
                }
                if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.EXIT) return;
                if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS){
                    System.out.println(ViewStatus.getActual().toString());
                    ((StatusViewer)queued.getPayload()).toTerminal().setMyTerminalViewer(this);
                    try {
                        ((StatusViewer)queued.getPayload()).toTerminal().print();
                    } catch (CheckQueueException ignore) {
                    }
                }
                if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.MESSAGE){
                    System.out.println();
                }
            }
        }
    }
}
