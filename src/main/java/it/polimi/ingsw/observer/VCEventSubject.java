package it.polimi.ingsw.observer;

import it.polimi.ingsw.view.events.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable (Subject) class for Observer Pattern.
 *
 * VCEvent (Events form Client)
 *
 * @author AndreaAltomare
 */
public class VCEventSubject {
    private final List<VCEventListener> listeners = new ArrayList<>();

    /**
     * Generic notify method for generic Object.
     *
     * @param o (Generic Object)
     */
    protected void notify(Object o) {
        synchronized (listeners) {
            for(VCEventListener listener : listeners)
                listener.update(o);
        }
    }

    /**
     * Call proper update method on Listener.
     *
     * @param e (VCEvent EventObject)
     * @param playerNickname (Player who has generated e)
     */
    protected void notifyVCEventsListeners(Object e, String playerNickname) {
        if (e instanceof BuildBlockEvent)
            synchronized (listeners) {
                for(VCEventListener listener : listeners)
                    listener.update((BuildBlockEvent)e, playerNickname);
            }
        else if (e instanceof CardSelectionEvent)
            synchronized (listeners) {
                for(VCEventListener listener : listeners)
                    listener.update((CardSelectionEvent) e, playerNickname);
            }
        else if (e instanceof MoveWorkerEvent)
            synchronized (listeners) {
                for(VCEventListener listener : listeners)
                    listener.update((MoveWorkerEvent) e, playerNickname);
            }
        else if (e instanceof PlaceWorkerEvent)
            synchronized (listeners) {
                for(VCEventListener listener : listeners)
                    listener.update((PlaceWorkerEvent) e, playerNickname);
            }
        else if (e instanceof QuitEvent)
            synchronized (listeners) {
                for(VCEventListener listener : listeners)
                    listener.update((QuitEvent) e, playerNickname);
            }
        else if (e instanceof RemoveBlockEvent)
            synchronized (listeners) {
                for(VCEventListener listener : listeners)
                    listener.update((RemoveBlockEvent) e, playerNickname);
            }
        else if (e instanceof RemoveWorkerEvent)
            synchronized (listeners) {
                for(VCEventListener listener : listeners)
                    listener.update((RemoveWorkerEvent) e, playerNickname);
            }
        else if (e instanceof SelectWorkerEvent)
            synchronized (listeners) {
                for(VCEventListener listener : listeners)
                    listener.update((SelectWorkerEvent) e, playerNickname);
            }
        else if (e instanceof SetNicknameEvent)
            synchronized (listeners) {
                for(VCEventListener listener : listeners)
                    listener.update((SetNicknameEvent) e);
            }
        else if (e instanceof TurnStatusChangeEvent)
            synchronized (listeners) {
                for(VCEventListener listener : listeners)
                    listener.update((TurnStatusChangeEvent) e, playerNickname);
            }
        else if (e instanceof ViewRequestDataEvent)
            synchronized (listeners) {
                for(VCEventListener listener : listeners)
                    listener.update((ViewRequestDataEvent) e, playerNickname);
            }
        else {
            // todo handle evento non valido
        }
    }

    /**
     * Add a new Listener for VCEvents.
     *
     * @param listener (VCEvent Listener)
     */
    public void addVCEventsListener(VCEventListener listener){
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove a Listener for VCEvents.
     *
     * @param listener (VCEvent Listener)
     */
    public void removeVCEventsListener(VCEventListener listener){
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
}
