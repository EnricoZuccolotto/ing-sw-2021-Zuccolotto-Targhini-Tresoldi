package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.HumanPlayer;

import java.util.Optional;

public class GetProductionCardMessage extends ProductionMessage{
    private final int  color;
    private final int  level;
    private  final Optional<Integer> index;

    public GetProductionCardMessage(String playerName, MessageType messageType, int[] resSpeWar, int[] resWar, int[] resStr, int color, int level, Optional<Integer> index) {
        super(playerName, messageType, resSpeWar, resWar, resStr);
        this.color = color;
        this.level = level;
        this.index = index;
    }

    public int getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

    public Optional<Integer> getIndex() {
        return index;
    }


}
