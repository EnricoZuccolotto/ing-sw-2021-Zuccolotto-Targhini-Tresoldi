package it.polimi.ingsw.exceptions;

public class IllegalDecoratorException extends RuntimeException {
    public IllegalDecoratorException(){
        super("Action not allowed");
    }
}
