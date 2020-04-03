package it.polimi.ingsw.model;

import java.util.Iterator;
import java.util.List;

public abstract class GeneralGameRoom {
    protected List<Player> players;
    // TODO: to add AbstractGameBox attribute

    // Player list manipulation
    public abstract boolean addPlayer(String nickname);
    public abstract void removePlayer(String nickname);
    public abstract Iterator<Player> getPlayers();

    // Game settings
    public abstract void setupGame();
    public abstract void chooseChallenger(String nickname);
    public abstract void chooseStartingPlayer(String nickname);
    public abstract Player getPlayer(int n);
}
