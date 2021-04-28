package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

/**
 *
 */
public class SecondActionMessage extends Message {

    ArrayList<Resources> resources;

    public SecondActionMessage(String playerName, ArrayList<Resources> resources) {
        super(playerName, MessageType.SECOND_ACTION);
        this.resources = resources;
    }

    public ArrayList<Resources> getResources() {
        return resources;
    }


}
