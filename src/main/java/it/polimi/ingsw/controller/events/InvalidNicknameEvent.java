package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Submitted nickname is invalid.
 * [MVEvent]
 */
public class InvalidNicknameEvent extends EventObject {
    //non metteerei nulla...

    public InvalidNicknameEvent(Object o) {
        super(o);
    }
}
