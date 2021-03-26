package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.player.Player;

public class Game {
    private static Game instance = null;

    private Market market;
    private Deck[][] decks;
    private int numPlayers;
    private Player[] players;
    private FaithPath faithPath;

    private Game(){}
    public static Game getInstance(){
        if(instance == null){
            instance = new Game();
        }
        return instance;
    }

    public void init() {

    }

    public void addPlayer(Player player) {

    }

    public void end() {}
}
