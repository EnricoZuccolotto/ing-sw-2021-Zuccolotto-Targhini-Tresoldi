package it.polimi.ingsw.exceptions.playerboard;

public class InsufficientColorsException extends RuntimeException {
    public InsufficientColorsException(){
        super("Number of colors not sufficient");
    }
}