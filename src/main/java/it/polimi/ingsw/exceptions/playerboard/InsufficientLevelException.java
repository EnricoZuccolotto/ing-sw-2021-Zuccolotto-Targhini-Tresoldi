package it.polimi.ingsw.exceptions.playerboard;

public class InsufficientLevelException  extends RuntimeException {
    public InsufficientLevelException(){
        super("Level not sufficient");
    }
}
