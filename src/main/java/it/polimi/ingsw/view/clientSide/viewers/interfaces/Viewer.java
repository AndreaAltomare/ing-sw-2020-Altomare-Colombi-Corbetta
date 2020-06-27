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
 * Class managing the connection between the common back-end
 * of the View (/Client) and the Viewers (front end).
 *
 * This class can (/have to) be extended with a specific one
 * for each new Viewer, but has some static methods and functionalities
 * that will make easier the communication to all the Viewers.
 *
 * @author giorgio
 */
public abstract class Viewer extends Thread{

    /**
     * Enumeration defining the various types of Events
     * notifiables to the various Viewers.
     */
    public static class ViewerQueuedEvent{
        public enum ViewerQueuedEventType{
            /**
             * Message from server/client/executers that should be shown.
             */
            MESSAGE,
            /**
             * The current SubTurn has to be set to be the one specified
             * into the event.
             */
            SET_SUBTURN,
            /**
             * The current Status has to be set to be the one specified
             * into the event.
             */
            SET_STATUS,
            /**
             * The Viewer has to refresh the screen -due to some changes
             * in the back-end representation.
             */
            REFRESH,
            /**
             * the Viewer has to manage the phase of cards selection.
             */
            CARDSELECTION,
            /**
             * This preannunces an exit, so the Viewers have to stop the various things.
             */
            EXIT;
        }

        private Object payload;
        private ViewerQueuedEventType type;

        /**
         * Method returning the type of this QueuedEvent.
         *
         * @return (the type of this).
         */
        public ViewerQueuedEventType getType(){ return type; }

        /**
         * Method returning the event of this.
         *
         * @return (the event of this).
         */
        public Object getPayload(){ return this.payload; }

        /**
         * Constructor.
         *
         * @param type      (the <code>ViewerQueuedEventType</code> type of this event).
         * @param payload   (the payload of this event).
         */
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

    /**
     * Method that executes the "setSubTurnViewer" method on each Viewer of myViewer.
     *
     * @param subTurnViewer (SubTurnViewer to be set)
     */
    public static void setAllSubTurnViewer(SubTurnViewer subTurnViewer){
        for (Viewer i: myViewers)
            i.setSubTurnViewer(subTurnViewer);
    }

    /**
     * Method that executes the "setCardSelection" method on each Viewer of myViewer.
     *
     * @param cardSelection (CardSelection to be set)
     */
    public static void setAllCardSelection(CardSelection cardSelection){
        for (Viewer i: myViewers)
            i.setCardSelection(cardSelection);
    }

    /**
     * Method that executes the "setRefresh" method on each Viewer of myViewer.
     *
     * @param payload (Object to be set, default: null and to be ignored)
     */
    public static void setAllRefresh(Object payload){
        for (Viewer i: myViewers)
            i.setRefresh(payload);
    }

    /**
     * Method that executes the "setRefresh" method on each Viewer of myViewer.
     */
    public static void setAllRefresh(){
        setAllRefresh(null);
    }

    /**
     * Method that executes the "sendMessage" method on each Viewer of myViewer.
     *
     * @param message (the ViewMessage to be notified)
     */
    public static void sendAllMessage(ViewMessage message) {
        for (Viewer i: myViewers)
            i.sendMessage(message);
    }


    /**
     * Method that executes the "exit" method on each Viewer of myViewer
     * and then terminates the execution of the program.
     */
    public static void exitAll(){
        for (Viewer i: myViewers)
            i.exit();
        System.exit(0);
    }

    /**
     * Method doing the Refresh of the Viewer
     */
    public abstract void refresh();

    /**
     * Method called when needs to be set a new StatusViewer on the Viewer.
     *
     * @param statusViewer (the StatusViewer to be set).
     */
    public void setStatusViewer(StatusViewer statusViewer){
        if(statusViewer != null)
            enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS, statusViewer));
    }

    /**
     * Method called when needs to be set a new SubTurnViewer on the Viewer.
     *
     * @param subTurnViewer (the SubTurnViewer to be set).
     */
    public void setSubTurnViewer(SubTurnViewer subTurnViewer){
        enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN, subTurnViewer));
    }

    /**
     * Method called when needs to be set a new CardSelection on the Viewer.
     *
     * @param cardSelection (the CardSelection to be set).
     */
    public void setCardSelection(CardSelection cardSelection){
        enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.CARDSELECTION, cardSelection));
    }

    /**
     * Method called when needs to be done a Refresh on the Viewer
     *
     * @param payload (should be null, but can be different. No need to check it).
     */
    public void setRefresh(Object payload){
        enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.REFRESH, payload));
    }

    /**
     * Method called when needs to be set a new ViewMessage on the Viewer.
     *
     * @param message (the ViewMessage to be set).
     */
    public void sendMessage(ViewMessage message){
        enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.MESSAGE, message));
    }

    /**
     * Method called before quitting the program.
     */
    public void exit(){
        enqueue(new ViewerQueuedEvent(ViewerQueuedEvent.ViewerQueuedEventType.EXIT, null));
    }

    /**
     * Method that adds a ViewerQueuedEvent to the blockingQueue.
     * It'll add all the ViewerQueuedEvent that cannot be executed
     * immediately and so will be retrieved by a different thread.
     * Should be overrided in each new Viewer.
     *
     * @param event (the ViewerQueuedEvent to add to the BlockingQueue).
     */
    protected void enqueue(ViewerQueuedEvent event){
        try {
            synchronized (myViewerQueue) {
                //System.out.println("Remaining: " + ((ArrayBlockingQueue)myViewerQueue).size());
                myViewerQueue.add(event);
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

    /**
     * Method that will notify all the threads waiting on the wakers Object.
     */
    protected void wakersNotify(){
        synchronized (wakers){
            wakers.notifyAll();
        }
    }


    /**
     * Method to check if the BlockingQueue is empty or not.
     * It'll not throw rhe Queue is empty, else it'll throw
     * the CheckQueueException.
     *
     * @throws CheckQueueException (iif the BlockingQueue isn't empty).
     */
    public void goOn() throws CheckQueueException {
        synchronized (myViewerQueue){
            if(myViewerQueue.isEmpty()) return;
        }
        throw new CheckQueueException();
    }

    /**
     * Method to be called to wait for the timeout
     * or throw CheckQueueException if/when BlockingQueue
     * is no longer empty.
     *
     * @param timeOut (the timeOut duration in millis).
     * @throws CheckQueueException  (iif the BlockingQueue is no empty).
     */
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

    /**
     * Method returning the first event added into the Blocking queue
     * and removes it from the queue (FIFO).
     *
     * @return (the first ViewerQueuedEvent of the BlockingQueue).
     * @throws EmptyQueueException  (if the BlockingQueue is empty).
     */
    protected ViewerQueuedEvent getNextEvent()throws EmptyQueueException {
        synchronized (myViewerQueue){
            if(!myViewerQueue.isEmpty()) return myViewerQueue.remove();
        }
        throw new EmptyQueueException();
    }

    /**
     * Method returning the type of the first element of the
     * BlockingQueue without removing it
     *
     * @return (the type of the first event enqueued into the BlockingQueue).
     * @throws EmptyQueueException (iif the BlockingQueue is empty).
     */
    public ViewerQueuedEvent.ViewerQueuedEventType seeEnqueuedType() throws EmptyQueueException{
        synchronized (myViewerQueue){
            if(myViewerQueue.isEmpty()){
                throw new EmptyQueueException();
            }
            return myViewerQueue.element().getType();
            //return myViewerQueue.get(0).getType();
        }
    }


    /**
     * Method that waits until a new event of specified type is added
     * to the BlockingQueue
     * It'll create some problems and so it's not recomended to use it
     * unless a really strong belif it's the only way.
     *
     * @param eventType (type of the event it's waiting for).
     */
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

    /**
     * Method that will wait untill there is a new event on
     * the BlockingQueue.
     */
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
