package it.polimi.ingsw.view.exceptions;

public class WrongParametersException extends Exception {
    String message;

    public WrongParametersException() {
        super();
        message = "";
    }

    public WrongParametersException(String message) {
        this();
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
