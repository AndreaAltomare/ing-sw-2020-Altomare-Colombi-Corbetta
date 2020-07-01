package it.polimi.ingsw.view.exceptions;

/**
 * Exception thrown when an executer cannot execute itself.
 * It contains a String with the information of the error.
 *
 * @author giorgio
 */
public class CannotSendEventException extends Exception {
    private String errorMessage;

    /**
     * Construtor.
     *
     * @param errorMessage (the String specifying the cause of the problem).
     */
    public CannotSendEventException(String errorMessage){
        super();
        this.errorMessage = errorMessage;
    }

    /**
     * Method to retrieve the String specifying the cause of the problem.
     *
     * @return (the String specifying the cause of the problem).
     */
    public String getErrorMessage(){
        return this.errorMessage;
    }
}
