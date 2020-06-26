package it.polimi.ingsw.model.exceptions;

/**
 * Specific Exception that is thrown when a player is trying to do anything outside of the board.
 */
public class OutOfBoardException extends Exception {

    /**
     * Default constructor making it possible to add a small description of the problem.
     *
     * @param s (A string describing the problem or what to do in case of problems).
     */
    public OutOfBoardException(String s) {
        super(s);
     }
}
