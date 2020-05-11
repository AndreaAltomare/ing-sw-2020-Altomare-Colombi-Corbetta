package it.polimi.ingsw.view.clientSide.viewCore.executers;

import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ViewSender;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
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
    public void syncSend() throws CannotSendEventException { Executer.send(this.getMyEvent()); this.clear(); }


    /**
     * Method that sends the event to the Server.
     *
     * @param event (EventObject to be submitted to the server)
     */
    public static void send(EventObject event) throws NullPointerException{
        if(event == null) throw new NullPointerException();
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
            public void run() { Executer.send(myObj); }
        }

        new asyncExecutor(this.getMyEvent()).start();
        this.clear();
    }

}
