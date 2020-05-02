package it.polimi.ingsw.controller.events;

/**
 * Event: Error message from Controller (Server).
 * [MVEvent]
 */
public class ErrorMessageEvent extends MessageEvent {

    public ErrorMessageEvent(String message) {
        super(message);
    }
}
