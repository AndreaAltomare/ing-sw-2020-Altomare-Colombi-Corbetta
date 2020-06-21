package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.UndoActionEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class UndoExecuter extends Executer {
    private static UndoExecuter instance = new UndoExecuter();
    private static long timeOut = 5000;
    private static Thread thread;

    private static boolean dontSendEvent = false;

    private final static Object lock = new Object();

    public interface UndoExecuterListenerUpdate{


        /**
         * Method to be called when the undo is available. this method must be light and no blocking cause executed on the same thread from which cames the undoReset -so, probably, the connection thread-.
         */
        public void undoLightAvailable();

        /**
         * Method to be called when the undo is available. this method is executed on the thread carying for the timeout.
         */
        public void undoWeightAvailable();


        /**
         * Method to be called when the undo has expired.
         */
        public void undoExpired();
    }

    private List<UndoExecuterListenerUpdate> myListener = new ArrayList<UndoExecuterListenerUpdate>(3);


    /**
     * Method that returns the instnace of the UndoExecuter.
     *
     * @return (UndoExecuter insatnced)
     */
    public static UndoExecuter getInstance(){
        return instance;
    }


    /**
     * Method to add a listener to be notified on undo updates.
     *
     * @param listener (the UndoExecuterListenerUpdate to be notified to).
     */
    public void registerListener(UndoExecuterListenerUpdate listener){
        myListener.add(listener);
    }

    /**
     * Method to remove a listener.
     *
     * @param listener (the UndoExecuterListenerUpdate to be removed).
     */
    public void unregisterListener(UndoExecuterListenerUpdate listener){
        myListener.remove(listener);
    }


    /**
     * Method to notify all the listeners the undo is available (with the call of the light method).
     */
    private void notifyLOn(){
        for (UndoExecuterListenerUpdate l: myListener) {
            l.undoLightAvailable();
        }
    }

    /**
     * Method to notify all the listeners the undo has expired.
     */
    private void notifyOff(){
        for (UndoExecuterListenerUpdate l: myListener) {
            l.undoExpired();
        }
        UndoExecuter.canSendEvent();
    }

    /**
     * Method to notify all the listeners the undo is available (with the call of the weithg method).
     */
    private void notifyWOn(){
        for (UndoExecuterListenerUpdate l: myListener) {
            l.undoWeightAvailable();
        }
    }

    /**
     * Method to be called outside the class to notify the undo is available
     */
    public static void startUndo(){
        instance.resetUndo();
    }

    private void resetUndo(){
        if(thread!=null){
            Thread t1 = thread;
            thread = null;
            t1.interrupt();
            notifyOff();
        }

        notifyLOn();
        thread = new Thread(){
            @Override
            public void run() {
                super.run();
                notifyWOn();
                synchronized (lock){
                    try {
                        lock.wait(timeOut);
                    } catch (InterruptedException ignore) {
                        ;
                    }
                }
                if(thread!=null) {
                    notifyOff();
                    thread = null;
                }
            }
        };

        thread.start();
        dontSendEvent = true;
    }

    public static void undoIt() throws CannotSendEventException {
        instance.doIt();
    }

    /**
     * Method that reset the executer with initial values.
     */
    public void clear(){}

    /**
     * constructor
     */
    private UndoExecuter(){
        this.clear();
    }

    /**
     * Static method that returns aunivoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public static String myType(){ return Executer.myType() + "\tQuit"; }


    //todo: implement it
    @Override
    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     */
    public UndoActionEvent getMyEvent() {
        return new UndoActionEvent();
    }

    public void send(EventObject event) throws NullPointerException{
        if(event == null) return;
        getSender().send((UndoActionEvent)event);
    }

    public static boolean isAvailable(){

        return thread!=null;
    }

    public static void stop(){
        if(thread != null){
            thread.interrupt();
            instance.notifyOff();
        }
    }

    public static boolean getDontSendEvent(){
        return dontSendEvent;
    }

    public static void canSendEvent(){
        dontSendEvent = false;
    }
}

