package it.polimi.ingsw.exception.controller;

public class LobbyException extends RuntimeException {
    private final LobbyError lobbyError;

    public LobbyException(LobbyError e) {
        super("");
        this.lobbyError = e;
    }

    public LobbyError getLobbyError() {
        return lobbyError;
    }
}
