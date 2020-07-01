package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player wants to resume a saved game.
 * [VCEvent]
 */
public class GameResumingResponseEvent extends EventObject {
    private boolean wantToResumeGame;

    /**
     * Constructs a GameResponseEvent to notify the Server weather
     * the player wants to resume a previous game or not.
     *
     * @param wantToResumeGame (true iif the player wants to resume a previous game).
     */
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
