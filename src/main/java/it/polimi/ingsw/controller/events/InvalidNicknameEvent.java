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
        this.message = "Your nickname is invalid or already taken! Please try again.\n(Your nickname must be at least 3 character long.)"; // TODO: maybe it's to remove (useless)
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
