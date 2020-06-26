package it.polimi.ingsw.connection.utility;

/**
 * Functional interface which provides
 * a method to call when a timer maximum
 * time expires.
 *
 * @author AndreaAltomare
 */
public interface TimeExpiredInterface {

    /**
     * Method called when the set time
     * for a timer is expired.
     *
     * @param responseReceived True if the expected response was received before the time expired
     */
    void handle(boolean responseReceived);
}
