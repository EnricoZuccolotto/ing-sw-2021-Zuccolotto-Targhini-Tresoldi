package it.polimi.ingsw.exception.controller;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(){
        super("The Player already exists");
    }
}
