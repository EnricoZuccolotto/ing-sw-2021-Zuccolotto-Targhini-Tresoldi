package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.tools.CardParser;

public class Game {
    private static Game instance = null;

    private Market market;
    private  Deck[][] decks;
    private int numPlayers;
    private Player[] players;
    private FaithPath faithPath;

    public Game(int numPlayers){
        this.decks=new Deck[4][3];
        this.numPlayers=numPlayers;
        this.players=new Player[numPlayers];
        this.market= new Market();
        this.faithPath= new FaithPath(numPlayers);
    }

    public void init() {

    }
    public Deck getDeck(int i,int j) {
       return decks[i][j] ;
    }
    public void starting_game() {
        initializeDecks();
    }
    public void addPlayer(Player player) {

    }



    public void end() {}
    private void initializeDecks(){

        this.decks= CardParser.parseDevCards();
    }
}
