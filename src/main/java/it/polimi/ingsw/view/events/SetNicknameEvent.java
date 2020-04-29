package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player has submitted his/her nickname.
 * [VCEvent]
 */
public class SetNicknameEvent extends EventObject {
    private String nickname;

    public SetNicknameEvent(String nickname) {
        super(new Object());
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
