package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.enums.BotActions;
import it.polimi.ingsw.model.enums.Colors;

import java.util.ArrayList;
import java.util.Collections;

public class BotPlayer extends Player {
    private final GameBoard currentGameBoard;
    private final ArrayList<BotActions> botActions;
    private int currentAction;
    public BotPlayer(GameBoard gameBoard) {
        super("BotPlayer");
        this.currentGameBoard = gameBoard;
        this.currentAction=0;
        this.botActions=new ArrayList<>(7);
        init();
    }
public void init(){

        botActions.add(BotActions.DiscardPurple);
        botActions.add(BotActions.DiscardGreen);
        botActions.add(BotActions.DiscardYellow);
        botActions.add(BotActions.DiscardBlue);
        botActions.add(BotActions.Blackcross1Shuffle);
        botActions.add(BotActions.BlackCross2);
        botActions.add(BotActions.BlackCross2);
        Collections.shuffle(botActions);
}

    public void doAction() {
          switch (botActions.get(currentAction)){
              case BlackCross2:
                  currentGameBoard.movePlayerFaithPath(1,2);
                  break;
              case Blackcross1Shuffle:
                  currentGameBoard.movePlayerFaithPath(1,1);
                  Collections.shuffle(botActions);
                  currentAction=-1;
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
    private void discard(Colors c){
        int cont=0;
        int i=0;
        while (cont<2 && i<3){

                if(currentGameBoard.getDeck(c.ordinal(),i).DeckLength()>0) {
                    currentGameBoard.getDeck(c.ordinal(), i).popLastCard();
                    cont++;
                }
                else
                i++;

    }
}}
