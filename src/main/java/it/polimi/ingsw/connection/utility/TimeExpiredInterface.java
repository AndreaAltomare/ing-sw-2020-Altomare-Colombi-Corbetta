package it.polimi.ingsw.connection.utility;

/**
 * Functional interface which provides
 * a method to call when a timer maximum
 * time expires.
 *
 * @author AndreaAltomare
 */
public interface TimeExpiredInterface {
    void handle(boolean responseReceived);
}
