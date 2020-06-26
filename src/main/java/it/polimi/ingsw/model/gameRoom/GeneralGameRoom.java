package it.polimi.ingsw.model.gameRoom;

import it.polimi.ingsw.model.player.Player;

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

    /**
     * Adds a Player to the Game room.
     *
     * @param nickname Player's nickname
     * @return True if the Player was actually added
     */
    public abstract boolean addPlayer(String nickname);

    /**
     * Removes a Player from the Game room.
     *
     * @param nickname Player's nickname
     */
    public abstract void removePlayer(String nickname);

    /**
     *
     * @return An Iterator for the Players set
     */
    public abstract Iterator<Player> getPlayers();

    /**
     *
     * @return A List of the Players set
     */
    public abstract List<Player> getPlayersList();

    /**
     * Adds all the Players to the Game room at once.
     *
     * @param players List of the Players to add
     */
    public abstract void setPlayers(List<Player> players);

    /**
     * Gets a Player provided his/her nickname.
     *
     * @param nickname Player's nickname
     * @return The reference of the Player
     */
    public abstract Player getPlayer(String nickname);

    /**
     * Gets a Player provided his/her index.
     *
     * @param n Numeric index
     * @return The reference of the Player
     */
    public abstract Player getPlayer(int n);

    /**
     * Gets the Challenger Player.
     *
     * @return The reference of the Challenger Player
     */
    public abstract Player getChallenger();

    /**
     * Gets the Starting Player.
     *
     * @return The reference of the Starting Player
     */
    public abstract Player getStartingPlayer();

    /* Game settings */

    /**
     * Setups the Game scenario.
     */
    public abstract void setupGame();

    /**
     * Chooses the Challenger Player for the game.
     *
     * @param nickname Player's nickname
     */
    public abstract void chooseChallenger(String nickname);

    /**
     * Chooses the Starting Player for the game.
     *
     * @param nickname Player's nickname
     */
    public abstract void chooseStartingPlayer(String nickname);

    /**
     * Registers Turn observers for every Player.
     */
    public abstract void registerObservers();
}
