package it.polimi.ingsw.observer;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable (Subject) class for Observer Pattern.
 *
 * MVEvent (Events form Server)
 *
 * @author AndreaAltomare
 */
public class MVEventSubject {
    private final List<MVEventListener> listeners = new ArrayList<>();

    /**
     * Generic notify method for generic Object.
     *
     * @param o (Generic Object)
     */
    protected void notify(Object o) {
        synchronized (listeners) {
            for(MVEventListener listener : listeners)
                listener.update(o);
        }
    }

    /**
     * Call proper update method on Listener.
     *
     * @param e (MVEvent EventObject)
     */
    protected void notifyMVEventsListeners(Object e) {
        if (e instanceof BlockBuiltEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((BlockBuiltEvent) e);
            }
        else if (e instanceof BlockRemovedEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((BlockRemovedEvent) e);
            }
        else if (e instanceof CardSelectedEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((CardSelectedEvent) e);
            }
        else if (e instanceof CardsInformationEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((CardsInformationEvent) e);
            }
        else if (e instanceof ErrorMessageEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((ErrorMessageEvent) e);
            }
        else if (e instanceof GameOverEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((GameOverEvent) e);
            }
        else if (e instanceof GameResumingEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((GameResumingEvent) e);
            }
        else if (e instanceof InvalidNicknameEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((InvalidNicknameEvent) e);
            }
        else if (e instanceof LobbyFullEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((LobbyFullEvent) e);
            }
        else if (e instanceof MessageEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((MessageEvent) e);
            }
        else if (e instanceof NextStatusEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((NextStatusEvent) e);
            }
        else if (e instanceof PlayerLoseEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((PlayerLoseEvent) e);
            }
        else if (e instanceof PlayerWinEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((PlayerWinEvent) e);
            }
        else if (e instanceof RequirePlaceWorkersEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((RequirePlaceWorkersEvent) e);
            }
        else if (e instanceof RequirePlayersNumberEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((RequirePlayersNumberEvent) e);
            }
        else if (e instanceof RequireStartPlayerEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((RequireStartPlayerEvent) e);
            }
        else if (e instanceof ServerQuitEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((ServerQuitEvent) e);
            }
        else if (e instanceof ServerSendDataEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((ServerSendDataEvent) e);
            }
        else if (e instanceof TurnStatusChangedEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((TurnStatusChangedEvent) e);
            }
        else if (e instanceof UndoOkEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((UndoOkEvent) e);
            }
        else if (e instanceof WorkerMovedEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((WorkerMovedEvent) e);
            }
        else if (e instanceof WorkerPlacedEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((WorkerPlacedEvent) e);
            }
        else if (e instanceof WorkerRemovedEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((WorkerRemovedEvent) e);
            }
        else if (e instanceof WorkerSelectedEvent)
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update((WorkerSelectedEvent) e);
            }
        else {
            // todo handle evento non valido
            if(View.debugging)
                System.err.println("Invalid MVEvent Object to notify!");
            ViewMessage.populateAndSend("Invalid MVEvent Object to notify!", ViewMessage.MessageType.FROM_SERVER_ERROR);
            synchronized (listeners) {
                for(MVEventListener listener : listeners)
                    listener.update(new ErrorMessageEvent("Warning: An invalid MVEvent has been generated!"));
            }
        }
    }

    /**
     * Add a new Listener for MVEvents.
     *
     * @param listener (MVEvent Listener)
     */
    public void addMVEventsListener(MVEventListener listener){
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove a Listener for MVEvents.
     *
     * @param listener (MVEvent Listener)
     */
    public void removeMVEventsListener(MVEventListener listener){
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
}
