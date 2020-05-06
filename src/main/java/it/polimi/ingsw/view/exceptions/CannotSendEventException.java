package it.polimi.ingsw.view.exceptions;

public class CannotSendEventException extends Exception {
    private String errorMessage;

    public CannotSendEventException(String errorMessage){
        super();
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }
}
