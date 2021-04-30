package it.polimi.ingsw.exception.controller;

public class LobbyisFullException extends RuntimeException {
    public LobbyisFullException(){
        super("The Lobby is full");
    }
}
