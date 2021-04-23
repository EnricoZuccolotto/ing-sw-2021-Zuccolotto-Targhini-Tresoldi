package it.polimi.ingsw.network.messages;

public class LobbyJoinMessage extends Message{
    public LobbyJoinMessage(String playerName){
        super(playerName, MessageType.JOIN_GAME);
    }
}
