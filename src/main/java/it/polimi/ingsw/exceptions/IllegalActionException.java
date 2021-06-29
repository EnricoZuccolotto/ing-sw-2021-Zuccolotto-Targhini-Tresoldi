package it.polimi.ingsw.exceptions;

public class IllegalActionException extends RuntimeException {
    public IllegalActionException(){
        super("Action not allowed");
    }

}
