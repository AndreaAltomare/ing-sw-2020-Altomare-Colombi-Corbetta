package it.polimi.ingsw.model;

/**
 * Enumeration type for states in
 * TurnManager State Pattern.
 *
 * @author AndreaAltomare
 */
public enum StateType {
    MOVEMENT,
    CONSTRUCTION,
    NONE, // no State
    ANY // any State
}
