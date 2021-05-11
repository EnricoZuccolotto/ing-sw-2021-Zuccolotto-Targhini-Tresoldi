package it.polimi.ingsw.exception.controller;

public enum LobbyError {
    FULL,
    NOT_CREATED,
    ALREADY_CREATED;


    public static String toString(LobbyError e) {
        switch (e) {
            case ALREADY_CREATED:
                return "The Lobby was already created";
            case NOT_CREATED:
                return "The lobby is not created.SET a new lobby";
            case FULL:
                return "The lobby is full.";
            default:
                return "error";
        }
    }
}
