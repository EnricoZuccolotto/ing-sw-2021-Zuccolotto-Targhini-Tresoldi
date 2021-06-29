package it.polimi.ingsw.exceptions;

public class InsufficientLevelException  extends RuntimeException {
    public InsufficientLevelException(){
        super("Level not sufficient");
    }
}
