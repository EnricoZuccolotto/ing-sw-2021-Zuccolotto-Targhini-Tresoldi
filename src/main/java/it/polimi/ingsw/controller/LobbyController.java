package it.polimi.ingsw.controller;


import it.polimi.ingsw.exception.controller.LobbyError;
import it.polimi.ingsw.exception.controller.LobbyException;
import it.polimi.ingsw.exception.controller.PlayerAlreadyExistsException;
import it.polimi.ingsw.network.messages.LobbyJoinMessage;
import it.polimi.ingsw.network.messages.LobbySetMessage;

import java.util.ArrayList;

public class LobbyController {
    private final ArrayList<String> inLobbyPlayer;
    private int playerNumber;

    public LobbyController() {
        inLobbyPlayer = new ArrayList<>();
        playerNumber = -1;
    }

    public void handle_setLobby(LobbySetMessage message) {
        if (playerNumber == -1) {
            if (message.getPlayerNumber() > 0 && message.getPlayerNumber() < 5) {
                playerNumber = message.getPlayerNumber();
                inLobbyPlayer.add(message.getPlayerName());
            } else throw new IndexOutOfBoundsException();
        } else throw new LobbyException(LobbyError.ALREADY_CREATED);

    }

    public void handle_addInLobby(LobbyJoinMessage message) {
        if (checkUserData(message.getPlayerName())) {
            if (isFull()) {
                throw new LobbyException(LobbyError.FULL);
            } else if (playerNumber <= 0) {
                throw new LobbyException(LobbyError.NOT_CREATED);
            }
            inLobbyPlayer.add(message.getPlayerName());
        } else throw new PlayerAlreadyExistsException();
    }

    public boolean checkUserData(String s) {
        for (String string : inLobbyPlayer) {
            if (s.equals(string)) {
                return false;
            }
        }
        return true;
    }

    public boolean isFull() {
        return inLobbyPlayer.size() == playerNumber;
    }


    public ArrayList<String> getPlayers() {
        return inLobbyPlayer;
    }


}