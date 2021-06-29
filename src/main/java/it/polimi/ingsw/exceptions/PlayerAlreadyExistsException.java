package it.polimi.ingsw.exceptions;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(){
        super("The Player already exists");
    }
}
