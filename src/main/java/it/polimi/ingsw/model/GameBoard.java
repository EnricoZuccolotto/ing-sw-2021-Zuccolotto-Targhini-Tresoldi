package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.BotPlayer;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.observer.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
public class GameBoard extends Observable {
    private Market market;
    private  Deck[][] decks;
    private ArrayList<HumanPlayer> players;
    private FaithPath faithPath;
    private Optional <BotPlayer> Bot;


    public GameBoard(){
        this.decks= CardParser.parseDevCards();
        this.players=new ArrayList<>();
        this.market= new Market();
    }

    public void init(GameBoard gameBoard) {
       if (players.size()==1) {
           this.faithPath = new FaithPath(2);
           Bot= Optional.of(new BotPlayer(gameBoard));
       }
       else  this.faithPath = new FaithPath(players.size());
    }
    public BotPlayer getBot(){
        return Bot.orElse(null);
    }

    public ArrayList<HumanPlayer> getPlayers(){
        return players;
    }
    public ArrayList<Resources> pushColumnMarket(int i){
        return market.pushColumn(i);
    }
    public ArrayList<Resources> pushRowMarket(int i){
        return market.pushRow(i);
    }
    public void movePlayerFaithPath(int player,int n){faithPath.movePlayer(player,n);}
    public int getPlayerFaithPathPosition(int player){return faithPath.getPosition(player);}
    public int get_PV(int player){return faithPath.get_PV(player);}

    public Deck getDeck(int color,int level) {
       return decks[color][level] ;
    }


    public void addPlayer(HumanPlayer player) {
     players.add(player);
    }



    public void end() {}

    public Market getMarket(){
        return market;
    }

    public FaithPath getFaithPath(){
        return faithPath;
    }

    @Override
    public String toString() {
        return
                 Arrays.toString(decks) +
                "" + faithPath
                ;
    }
}
