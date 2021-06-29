package it.polimi.ingsw.exceptions;

public class IllegalResourceException extends RuntimeException {
    public IllegalResourceException(){
        super("Resource not allowed");
    }
}
