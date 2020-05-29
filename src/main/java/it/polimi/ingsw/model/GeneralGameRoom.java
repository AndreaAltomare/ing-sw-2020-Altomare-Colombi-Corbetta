package it.polimi.ingsw.model;

import java.util.Iterator;
import java.util.List;

/**
 * This class model the concept of a waiting room during
 * the connection phase and the game room when the match starts.
 *
 * @author AndreaAltomare
 */
public abstract class GeneralGameRoom {
    protected List<Player> players;

    /* Player list manipulation */
    public abstract boolean addPlayer(String nickname);
    public abstract void removePlayer(String nickname);
    public abstract Iterator<Player> getPlayers();
    public abstract List<Player> getPlayersList();
    public abstract void setPlayers(List<Player> players);
    public abstract Player getPlayer(String nickname);
    public abstract Player getPlayer(int n);
    public abstract Player getChallenger();
    public abstract Player getStartingPlayer();

    /* Game settings */
    public abstract void setupGame();
    public abstract void chooseChallenger(String nickname);
    public abstract void chooseStartingPlayer(String nickname);
    public abstract void registerObservers();
}
