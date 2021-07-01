package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.WinnerException;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.communication.CommunicationMessage;
import it.polimi.ingsw.model.enums.BotActions;
import it.polimi.ingsw.model.enums.Colors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
/**
 * This class represents a bot player.
 * currentGameBoard is the game board used by the bot player
 * botActions represents the stack of solo action token(DiscardPurple,DiscardGreen,DiscardYellow,DiscardBlue,Blackcross1Shuffle,BlackCross2, BlackCross2);
 * currentAction is an index used to scroll the array list of botAction, represents the next bot action that the bot must do.
 */
public class BotPlayer extends Player implements Serializable {
    private final GameBoard currentGameBoard;
    private final ArrayList<BotActions> botActions;
    private int currentAction;

    /**
     * Build a new bot player working in the game board
     */
    public BotPlayer(GameBoard gameBoard) {
        super("Lorenzo il Magnifico");
        this.currentGameBoard = gameBoard;
        this.currentAction = 0;
        this.botActions = new ArrayList<>(7);
        init();
    }

    /**
     * Generate the list of the possible action that the bot can do, and it shuffles the list.
     */
    private void init() {
        botActions.add(BotActions.DiscardPurple);
        botActions.add(BotActions.DiscardGreen);
        botActions.add(BotActions.DiscardYellow);
        botActions.add(BotActions.DiscardBlue);
        botActions.add(BotActions.BlackCross1Shuffle);
        botActions.add(BotActions.BlackCross2);
        botActions.add(BotActions.BlackCross2);
        Collections.shuffle(botActions);
    }

    /**
     * Make the bot do the next action
     */
    public void doAction() {
        currentGameBoard.setPublicCommunication(botActions.get(currentAction).toString(), CommunicationMessage.BOT_ACTION);
        switch (botActions.get(currentAction)) {
            case BlackCross2:
                currentGameBoard.movePlayerFaithPath(1, 2);
                break;
            case BlackCross1Shuffle:
                currentGameBoard.movePlayerFaithPath(1, 1);
                Collections.shuffle(botActions);
                currentAction = -1;
                break;
              case DiscardBlue:
                  discard(Colors.BLUE);
                  break;
              case DiscardGreen:
                  discard(Colors.GREEN);
                  break;
            case DiscardPurple:
                discard(Colors.PURPLE);
                break;
            case DiscardYellow:
                discard(Colors.YELLOW);
                break;
        }
        currentAction++;
    }

    /**
     * Discard 2 Development Cards of the indicated type from the bottom of the grid, from the lowest level to the highest
     *
     * @param c color type.
     */
    public void discard(Colors c) {
        int cont = 0;
        int i = 1;
        int sum = 0;
        while (cont < 2 && i < 4) {
            if (currentGameBoard.getDeck(c, i).DeckLength() > 0) {
                currentGameBoard.getDecks().popLastCard(c,i);
                cont++;
            } else
                i++;
        }
        for (i = 1; i < 4; i++) {
            sum += currentGameBoard.getDeck(c, i).DeckLength();
        }
        if (sum == 0) {
            throw new WinnerException();
        }
    }

    /**
     * Gets the current action of the bot.
     *
     * @return the current BotAction.
     */
    public BotActions getCurrentAction() {
        return botActions.get(currentAction);
    }

}
