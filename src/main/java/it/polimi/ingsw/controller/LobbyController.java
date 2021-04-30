package it.polimi.ingsw.controller;


import it.polimi.ingsw.exception.controller.*;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.LobbyJoinMessage;
import it.polimi.ingsw.network.messages.LobbySetMessage;

import java.util.ArrayList;
import java.util.Collections;

public class LobbyController {
    private final ArrayList<String> inLobbyPlayer;
    private int playerNumber;
    private ArrayList<HumanPlayer> PlayerList;

    public LobbyController() {
        inLobbyPlayer = new ArrayList<>();
        playerNumber = -1;
    }

    public void handle_setLobby(LobbySetMessage message) {
        if (playerNumber == -1) {
            if(message.getPlayernumber()>0 && message.getPlayernumber()<5) {
                playerNumber = message.getPlayernumber();
                inLobbyPlayer.add(message.getPlayerName());
            } else throw new IndexOutOfBoundsException();
        } else throw new LobbyAlreadyCreatedException();
    }

    public void handle_addInLobby(LobbyJoinMessage message) {
        if (checkUserData(message.getPlayerName())) {
            if(isFull()){
                throw new LobbyisFullException();
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
        Collections.shuffle(inLobbyPlayer);
        return inLobbyPlayer;
    }


}