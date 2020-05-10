package it.polimi.ingsw.view.clientSide.viewers.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for the various visualizer
 *
 * @author giorgio
 */
public abstract class Viewer implements Runnable{

    public static class ViewerQueuedEvent{
        public enum ViewerQueuedEventType{
            MESSAGE,
            SET_SUBTURN,
            SET_STATUS,
            REFRESH,
            EXIT;
        }

        private Object payload;
        private ViewerQueuedEventType type;

        public ViewerQueuedEventType getType(){ return type; }

        public Object getPayload(){ return this.payload; }

        public ViewerQueuedEvent(ViewerQueuedEventType type, Object payload){
            this.type = type;
            this.payload = payload;
        }

    }

    public List<ViewerQueuedEvent> myViewerQueue = new ArrayList<ViewerQueuedEvent>();
    public Object wakers;

    /**
     * List of all the Viewers.
     */
    private static List<Viewer> myViewers = new ArrayList<Viewer>();

    /**
     * Method that adds the viewer to the list of viewers.
     *
     * @param viewer
     */
    public static void registerViewer(Viewer viewer){ myViewers.add(viewer); }

    /**
     * Method that executes the "setAllStatusViewer" method on each Viewer of myViewer.
     *
     * @param statusViewer (statusViewer to be set).
     */
    public static void setAllStatusViewer (StatusViewer statusViewer){ for (Viewer i: myViewers) i.setStatusViewer(statusViewer); }

    /**
     * Method that executes the "setSubTurnViewer" method on each Viewer of myViewer.
     *
     * @param subTurnViewer (SubTurnVersion to be set)
     */
    public static void setAllSubTurnViewer(SubTurnViewer subTurnViewer){ for (Viewer i: myViewers) i.setSubTurnViewer(subTurnViewer); }

    public static void sendAllMessage(ViewMessage message) { for (Viewer i: myViewers) i.sendMessage(message); }

    //Funzione che lancia l'esecuzione.
    public abstract void start();

    //Fuzione che forza un refresh della view
    public abstract void refresh();

    //Funzione che segnala al Viewer di controllare lo stato ASAP
    public void setStatusViewer(StatusViewer statusViewer){
        synchronized (myViewerQueue){
            myViewerQueue.add(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS, statusViewer));
        }
        wakers.notifyAll();
    }

    public void setSubTurnViewer(SubTurnViewer subTurnViewer){
        synchronized (myViewerQueue){
            myViewerQueue.add(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN, subTurnViewer));
        }
        wakers.notifyAll();
    }

    public void sendMessage(ViewMessage message){
        synchronized (myViewerQueue){
            myViewerQueue.add(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.MESSAGE, message));
        }
        wakers.notifyAll();
    }

}
