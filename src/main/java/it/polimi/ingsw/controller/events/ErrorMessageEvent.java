package it.polimi.ingsw.controller.events;

/**
 * Event: Error message from Controller (Server).
 * [MVEvent]
 */
public class ErrorMessageEvent extends MessageEvent {

    /**
     * Constructs an ErrorMessageEvent to inform the View about the event (error) occurred.
     *
     * @param message Custom message
     */
    public ErrorMessageEvent(String message) {
        super(message);
    }
}
