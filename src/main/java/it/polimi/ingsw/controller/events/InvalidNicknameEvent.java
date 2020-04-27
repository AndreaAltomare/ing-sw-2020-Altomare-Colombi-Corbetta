package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Submitted nickname is invalid.
 * [MVEvent]
 */
public class InvalidNicknameEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari

    public InvalidNicknameEvent(Object o) {
        super(o);
    }
}
