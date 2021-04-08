package it.polimi.ingsw.exceptions.playerboard;

public class IllegalDecoratorException extends RuntimeException {
    public IllegalDecoratorException(){
        super("Action not allowed");
    }
}
