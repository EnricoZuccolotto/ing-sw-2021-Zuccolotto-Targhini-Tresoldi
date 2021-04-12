package it.polimi.ingsw.exceptions.playerboard;

public class InsufficientResourcesException  extends RuntimeException {
    public InsufficientResourcesException(){
        super("Number of resources not sufficient");
    }
}