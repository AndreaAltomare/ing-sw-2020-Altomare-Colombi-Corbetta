package it.polimi.ingsw.model.player.turn;

/**
 * Enumeration type for states in
 * TurnManager State Pattern.
 *
 * @author AndreaAltomare
 */
public enum StateType {
    MOVEMENT,
    CONSTRUCTION,
    NONE, // no State (it's not the Player's turn)
    ANY // any State
}
