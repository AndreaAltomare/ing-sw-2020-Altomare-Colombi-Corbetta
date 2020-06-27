package it.polimi.ingsw.view.clientSide.viewCore.executers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.UndoExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ViewSender;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.EventObject;

/**
 * Interface to implement the events to be send to the Controller
 *
 * @author giorgio
 */
public abstract class Executer{

    private static ViewSender sender;

    /**
     * Static method to set the default object on which call the <code>send</code> method.
     *
     * @param newSender (the <code>ViewSender</code> on which call the <code>send</code>).
     */
    public static void setSender(ViewSender newSender){
        sender = newSender;
    }

    /**
     * Method that reset the executer with initial values.
     */
    public abstract void clear();

    /**
     * Static method that returns aunivoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    @NotNull
    @Contract(pure = true)
    public static String myType(){ return "[Executer]"; }

    /**
     * Method that implements the recommended way to send the event.
     *
     * @throws CannotSendEventException (if the Executor doesn't have all the information required by the event)
     */
    public void doIt() throws CannotSendEventException { this.asyncSend(); }

    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    public EventObject getMyEvent()throws CannotSendEventException { throw new CannotSendEventException("This message shouldn't be shown... If so the game is probably f**ked up!!"); }

    /**
     * Method that returns aunivoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public String exType(){ return myType(); }

    /**
     *  Method that checks weather 2 Executers are of the same type. It works checking the exType().
     *
     * @param ex (Executer to be checked)
     * @return (true if ex is the same type Executer of this)
     */
    public boolean isSameType(@NotNull Executer ex){ return (ex.exType().equals(this.exType())); }

    /**
     * Method to send the event to the Server within the same thread of the call.
     *
     * @throws CannotSendEventException (if the Executor doesn't have all the information required by the event)
     */
    public void syncSend() throws CannotSendEventException {
        try {
            send(this.getMyEvent());
        } catch (WrongEventException | NullPointerException e) {
            throw new CannotSendEventException("Too fast!");
        }
        this.clear();
    }

    /**
     * Method that sends the event to the Server.
     *
     * @param event (EventObject to be submitted to the server)
     */
    public void send(Object event) throws NullPointerException, WrongEventException {
        if(event == null) throw new NullPointerException();
        if(this.checkUndo() && UndoExecuter.getDontSendEvent()){
            throw new WrongEventException();
        }
        sender.send(event);
    }

    /**
     * Method to send the event to the Server in a new thread.
     *
     * @throws CannotSendEventException (if the Executor doesn't have all the information required by the event)
     */
    public void asyncSend()throws CannotSendEventException {

        class asyncExecutor extends Thread {
            private EventObject myObj;
            asyncExecutor(EventObject obj){
                super();
                this.myObj = obj;
            }

            @Override
            public void run() {
                try {
                    send(myObj);
                } catch (WrongEventException | NullPointerException ignore) {
                }
            }

        }

        new asyncExecutor(this.getMyEvent()).start();
        this.clear();
    }

    /**
     * Method returning ture iif this Executer has to wait undo-timeout.
     *
     * @return (ture iif this Executer has to wait undo-timeout).
     */
    protected boolean checkUndo(){
        return false;
    }

    /**
     * Method that returns the set Sender.
     *
     * @return (the <code>ViewSender</code> called for the <code>send</code>).
     */
    protected static ViewSender getSender(){
        return sender;
    }

}
