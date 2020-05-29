package it.polimi.ingsw.view.clientSide.viewers.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.exceptions.CheckQueueException;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Interface for the various visualizer
 *
 * @author giorgio
 */
public abstract class Viewer extends Thread{

    public static class ViewerQueuedEvent{
        public enum ViewerQueuedEventType{
            MESSAGE,
            SET_SUBTURN,
            SET_STATUS,
            REFRESH,
            CARDSELECTION,
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

    //Blocking Queue
    private final BlockingQueue<ViewerQueuedEvent> myViewerQueue = new ArrayBlockingQueue<ViewerQueuedEvent>(64);
    //NON LEVARE IL COMMENTO!!!
    //private final List<ViewerQueuedEvent> myViewerQueue = new ArrayList<ViewerQueuedEvent>();
    private final Object wakers = new Object();

    /**
     * List of all the Viewers.
     */
    private static List<Viewer> myViewers = new ArrayList<Viewer>();

    /**
     * Method that adds the viewer to the list of viewers.
     *
     * @param viewer
     */
    public static void registerViewer(Viewer viewer){
        myViewers.add(viewer);
    }

    /**
     * Method that executes the "setAllStatusViewer" method on each Viewer of myViewer.
     *
     * @param statusViewer (statusViewer to be set).
     */
    public static void setAllStatusViewer (StatusViewer statusViewer){
        for (Viewer i: myViewers)
            i.setStatusViewer(statusViewer);
    }

    /**
     * Method that executes the "setSubTurnViewer" method on each Viewer of myViewer.
     *
     * @param viewSubTurn (SubTurnVersion to be set)
     */
    public static void setAllSubTurnViewer(ViewSubTurn viewSubTurn){
        for (Viewer i: myViewers)
            i.setSubTurnViewer(viewSubTurn.getSubViewer());
    }

    public static void setAllSubTurnViewer(SubTurnViewer subTurnViewer){
        for (Viewer i: myViewers)
            i.setSubTurnViewer(subTurnViewer);
    }

    public static void setAllCardSelection(CardSelection cardSelection){
        for (Viewer i: myViewers)
            i.setCardSelection(cardSelection);
    }

    public static void setAllRefresh(Object payload){
        for (Viewer i: myViewers)
            i.setRefresh(payload);
    }

    public static void setAllRefresh(){
        setAllRefresh(null);
    }

    public static void sendAllMessage(ViewMessage message) {
        for (Viewer i: myViewers)
            i.sendMessage(message);
    }

    public static void exitAll(){
        for (Viewer i: myViewers)
            i.exit();
    }

    //Fuzione che forza un refresh della view
    public abstract void refresh();

    //Funzione che segnala al Viewer di controllare lo stato ASAP
    public void setStatusViewer(StatusViewer statusViewer){
        enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS, statusViewer));
    }

    public void setSubTurnViewer(SubTurnViewer subTurnViewer){
        enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN, subTurnViewer));
    }

    public void setCardSelection(CardSelection cardSelection){
        enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.CARDSELECTION, cardSelection));
    }

    public void setRefresh(Object payload){
        enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.REFRESH, payload));
    }

    public void sendMessage(ViewMessage message){
        enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.MESSAGE, message));
    }

    public void exit(){
        enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.EXIT, null));
    }

    protected void enqueue(ViewerQueuedEvent event){
        try {
            //todo sistemare doppio sincronised
            synchronized (wakers) {
                synchronized (myViewerQueue) {
                    //System.out.println("Remaining: " + ((ArrayBlockingQueue)myViewerQueue).size());
                    myViewerQueue.add(event);
                }
            }
        }catch (IllegalStateException ignore){
            System.out.println("FUCK IT BLOCKING QUEUE");
            wakersNotify();
            try {
                myViewerQueue.put(event);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        wakersNotify();
    }

    protected void wakersNotify(){
        synchronized (wakers){
            wakers.notifyAll();
        }
    }

    public void goOn() throws CheckQueueException {
        synchronized (myViewerQueue){
            if(myViewerQueue.isEmpty()) return;
        }
        throw new CheckQueueException();
    }

    public void waitTimeOut(long timeOut) throws CheckQueueException{
        synchronized (wakers){
            try {
                goOn();
            } catch (CheckQueueException e) {
                throw new CheckQueueException();
            }
            try {
                wakers.wait(timeOut);
            } catch (InterruptedException e) {
                throw new CheckQueueException();
            }
        }
    }

    protected ViewerQueuedEvent getNextEvent()throws EmptyQueueException {
        synchronized (myViewerQueue){
            if(!myViewerQueue.isEmpty()) return myViewerQueue.remove();
        }
        throw new EmptyQueueException();
    }

    public ViewerQueuedEvent.ViewerQueuedEventType seeEnqueuedType() throws EmptyQueueException{
        synchronized (myViewerQueue){
            if(myViewerQueue.isEmpty()){
                throw new EmptyQueueException();
            }
            return myViewerQueue.element().getType();
            //return myViewerQueue.get(0).getType();
        }
    }


    //Non usarlo a meno di sapere che c'è un thread che serve ogni tipo di evento
    @Deprecated
    public void waitNextEventType(ViewerQueuedEvent.ViewerQueuedEventType eventType){
        while(true){
            waitNextEvent();
            synchronized (myViewerQueue){
                if(!myViewerQueue.isEmpty()) {
                    //if (myViewerQueue.get(0).getType().equals(eventType)) {
                    if (myViewerQueue.element().getType().equals(eventType)) {
                        return;
                    }
                }
            }
            Object obj = new Object();
            synchronized (obj){
                try {
                    obj.wait(500);
                } catch (InterruptedException ignore) {  }
            }
        }
    }

    public void waitNextEvent(){
        synchronized (wakers){
            try {
                goOn();
            } catch (CheckQueueException e) {
                return;
            }
            try {
                wakers.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
