package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class UndoExecuter extends Executer {
    private static UndoExecuter instance = new UndoExecuter();
    private static long timeOut = 5000;

    private final static Object lock = new Object();

    public interface UndoExecuterListenerUpdate{


        /**
         * Method to be clled when the undo is available.
         */
        public void undoAvailable();


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
    public UndoExecuter getInstance(){
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
     * Method to notify all the listeners the undo is available.
     */
    private void notifyOn(){
        for (UndoExecuterListenerUpdate l: myListener) {
            l.undoAvailable();
        }
    }

    /**
     * Method to notify all the listeners the undo has expired.
     */
    private void notifyOff(){
        for (UndoExecuterListenerUpdate l: myListener) {
            l.undoExpired();
        }
    }

    /**
     * Method to be called outside the class to notify the undo is available
     */
    public static void startUndo(){
        instance.resetUndo();
    }

    private void resetUndo(){
        synchronized (lock){
            lock.notifyAll();
        }

        notifyOn();
        (new Thread(){
            @Override
            public void run() {
                super.run();
                synchronized (lock){
                    try {
                        lock.wait(timeOut);
                    } catch (InterruptedException ignore) {
                        ;
                    }
                }
                notifyOff();
            }
        }).start();
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
    public EventObject getMyEvent() {
        return null;
    }
}

