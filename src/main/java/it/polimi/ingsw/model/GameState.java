package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Bean class to enable serialization/deserialization of a game state
 * by JSON files, and to encapsulate the actual state of the game at a certain point.
 *
 * GameState objects are used for state persistence issues,
 * when it comes to deal with game savings/loadings.
 *
 * // TODO: to write about Memento Pattern being applied here.
 *
 * @author AndreaAltomare
 */
public class GameState implements Serializable {

    /* Default Constructor */
    public GameState() {}
}
