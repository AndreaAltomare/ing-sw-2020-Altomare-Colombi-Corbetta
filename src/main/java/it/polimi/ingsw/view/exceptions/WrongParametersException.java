package it.polimi.ingsw.view.exceptions;

/**
 * Exception thrown when the parameters set are incorrect.
 *
 * @author giorgio
 */
public class WrongParametersException extends Exception {
    String message;

    /**
     * Constructor.
     */
    public WrongParametersException() {
        super();
        message = "";
    }

    /**
     * Construtor.
     *
     * @param message (the String specifying the cause of the problem).
     */
    public WrongParametersException(String message) {
        this();
        this.message = message;
    }

    /**
     * Method to retrieve the String specifying the cause of the problem.
     *
     * @return (the String specifying the cause of the problem).
     */
    public String getMessage(){
        return message;
    }
}
