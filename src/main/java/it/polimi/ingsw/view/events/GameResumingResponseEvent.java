package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player has selected a Card.
 * [VCEvent]
 */
public class GameResumingResponseEvent extends EventObject {
    private boolean wantToResumeGame;

    public GameResumingResponseEvent(boolean wantToResumeGame) {
        super(new Object());
        this.wantToResumeGame = wantToResumeGame;
    }

    public boolean isWantToResumeGame() {
        return wantToResumeGame;
    }

    public void setWantToResumeGame(boolean wantToResumeGame) {
        this.wantToResumeGame = wantToResumeGame;
    }
}
