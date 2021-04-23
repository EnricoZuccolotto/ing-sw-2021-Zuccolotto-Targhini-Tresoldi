package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.exception.controller.PlayerAlreadyExistsException;
import it.polimi.ingsw.network.messages.LobbyJoinMessage;
import it.polimi.ingsw.network.messages.LobbySetMessage;
import it.polimi.ingsw.exception.controller.LobbyAlreadyCreatedException;

import java.util.ArrayList;
import java.util.Collections;

public class LobbyController {
    private ArrayList<String> inLobbyPlayer;
    private int Playernumber;
    private ArrayList<HumanPlayer> PlayerList;

    public LobbyController() {
        inLobbyPlayer = new ArrayList<>();
        Playernumber = -1;
    }

    public void handle_setLobby(LobbySetMessage message) {
        if (Playernumber == -1) {
            Playernumber = message.getPlayernumber();
            inLobbyPlayer.add(message.getPlayerName());
        } else throw new LobbyAlreadyCreatedException();
    }

    public void handle_addinLobby(LobbyJoinMessage message) {
        if (checkUserData(message.getPlayerName())) {
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
        if (inLobbyPlayer.size() == Playernumber) {
            return true;
        } else return false;
    }


    public void TransformPlayer() {
        Collections.shuffle(inLobbyPlayer);
        for (String string : inLobbyPlayer) {
            if (inLobbyPlayer.get(0).equals(string)) {
                PlayerList.add(new HumanPlayer(string, true));
            } else {
                PlayerList.add(new HumanPlayer(string, false));
            }
        }
    }

    public ArrayList<String> getPlayer() {
        return inLobbyPlayer;
    }
}