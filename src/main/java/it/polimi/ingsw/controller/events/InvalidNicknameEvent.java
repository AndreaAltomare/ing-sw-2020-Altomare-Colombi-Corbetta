package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Submitted nickname is invalid.
 * [MVEvent]
 */
public class InvalidNicknameEvent extends EventObject {
    private final String message;

    public InvalidNicknameEvent() {
        super(new Object());
        this.message = "Your nickname is invalid or already taken! Choose another one.";
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
