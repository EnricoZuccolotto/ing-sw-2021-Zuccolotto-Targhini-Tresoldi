package it.polimi.ingsw.exceptions.playerboard;

public class IllegalResourceException extends RuntimeException {
    public IllegalResourceException(){
        super("Resource not allowed");
    }
}
