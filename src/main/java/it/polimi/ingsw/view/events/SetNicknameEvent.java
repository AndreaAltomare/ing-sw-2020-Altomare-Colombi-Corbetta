package it.polimi.ingsw.view.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player has submitted his/her nickname.
 * [VCEvent]
 */
public class SetNicknameEvent extends EventObject {
    // TODO inserire attributi

    public SetNicknameEvent(Object o) {
        super(o);
    }
}
