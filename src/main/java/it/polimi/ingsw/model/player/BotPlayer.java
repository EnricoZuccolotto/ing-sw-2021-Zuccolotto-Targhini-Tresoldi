package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.BotActions;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;

import java.util.ArrayList;
import java.util.Collections;

public class BotPlayer extends Player {
    private Game currentGame;
    private ArrayList<BotActions> botActions;
    private int currentAction;
    public BotPlayer(Game game) {
        super("BotPlayer");
        this.currentGame=game;
        this.currentAction=0;
        this.botActions=new ArrayList<>(8);
    }
public void init(){
        botActions.add(BotActions.DiscardPurple);
    botActions.add(BotActions.DiscardGreen);
    botActions.add(BotActions.DiscardYellow);
    botActions.add(BotActions.DiscardBlue);
    botActions.add(BotActions.Blackcross1Shuffle);
    for(int i=0;i<3;i++)
        botActions.add(BotActions.BlackCross2);
    Collections.shuffle(botActions);
}

    public void doAction() {
          switch (botActions.get(currentAction)){
              case BlackCross2:
                  currentGame.movePlayerFaithPath(1,2);
                  break;
              case Blackcross1Shuffle:
                  currentGame.movePlayerFaithPath(1,1);
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

                if(currentGame.getDeck(c.ordinal(),i).DeckLength()>0) {
                    currentGame.getDeck(c.ordinal(), i).popLastCard();
                    cont++;
                }
                else
                i++;

    }
}}
