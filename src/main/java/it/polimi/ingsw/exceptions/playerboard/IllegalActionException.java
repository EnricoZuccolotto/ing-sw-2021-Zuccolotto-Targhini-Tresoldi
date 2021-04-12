package it.polimi.ingsw.exceptions.playerboard;

public class IllegalActionException extends RuntimeException {
    public IllegalActionException(){
        super("Action not allowed");
    }

}
