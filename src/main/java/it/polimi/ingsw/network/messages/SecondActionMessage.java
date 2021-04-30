package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;

/**
 * This message is sent in order to choose the starting resources for your game.
 */
public class SecondActionMessage extends Message {
    ArrayList<Resources> resources;

    /**
     * Default constructor.
     * @param playerName Player name.
     * @param resources Resources to add.
     */
    public SecondActionMessage(String playerName, ArrayList<Resources> resources) {
        super(playerName, MessageType.SECOND_ACTION);
        this.resources = resources;
    }

    public ArrayList<Resources> getResources() {
        return resources;
    }


}
