package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.tools.CardParser;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    private static Game instance = null;

    private Market market;
    private  Deck[][] decks;
    private int numPlayers;
    private ArrayList<Player> players;
    private FaithPath faithPath;


    public Game(int numPlayers){
        this.decks=new Deck[4][3];
        this.numPlayers=numPlayers;
        this.players=new ArrayList<>();
        this.market= new Market();
        this.faithPath= new FaithPath(numPlayers);
    }

    public void init() {
        this.decks= CardParser.parseDevCards();

    }
    public void movePlayerFaithPath(int player,int n){faithPath.movePlayer(player,n);}
    public int get_PV(int player){return faithPath.get_PV(player);}

    public Deck getDeck(int color,int level) {
       return decks[color][level] ;
    }

    public void addPlayer(Player player) {
     players.add(player);
    }

    public void end() {}

    @Override
    public String toString() {
        return
                 Arrays.toString(decks) +
                "" + faithPath
                ;
    }
}
