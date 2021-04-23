package it.polimi.ingsw.exception.controller;

public class LobbyAlreadyCreatedException extends RuntimeException{
    public LobbyAlreadyCreatedException() { super("The Lobby was already created"); }
}
