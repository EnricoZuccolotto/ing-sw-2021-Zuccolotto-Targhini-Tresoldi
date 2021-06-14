package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Communication.Communication;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.BotPlayer;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.observer.Observable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the game board.
 * market represents the market.
 * decks represents the 12 deck of 4 cards each one of 1 color(Blue,Yellow,Purple,Green) and 1 level(1,2,3)
 * players represents the players that are playing
 * faithPath represents the faith path.
 * bot represents the bot player, if there is one.
 */
public class GameBoard extends Observable implements Serializable {
    private final Market market;
    private final Decks decks;
    private final ArrayList<HumanPlayer> players;
    private final FaithPath faithPath;
    private BotPlayer bot;
    private final Communication publicCommunication;


    /**
     * Build a new Game board.
     */
    public GameBoard() {
        this.decks = new Decks();
        this.publicCommunication = new Communication();
        this.players = new ArrayList<>();
        this.market = new Market();
        this.faithPath = new FaithPath();
    }

    /**
     * Initialize the game board and if there is only 1 player add the bot player.
     *
     * @param gameBoard game board used to start the bot player.
     */
    public void init(GameBoard gameBoard) {
        if (players.size() == 1) {
            this.faithPath.init(2, true);
            bot = new BotPlayer(gameBoard);
        } else this.faithPath.init(players.size(), false);
        notifyObserver(new DecksUpdateMessage(decks));
        notifyObserver(new MarketUpdateMessage("", market));
        notifyObserver(new FaithPathUpdateMessage(faithPath));
    }

    /**
     * Sends all the possible information about the game to all players.
     */
    public void sendGameUpdateToAllPlayers(){
        notifyObserver(new DecksUpdateMessage(decks));
        notifyObserver(new MarketUpdateMessage("", market));
        notifyObserver(new FaithPathUpdateMessage(faithPath));
        for(HumanPlayer player : players){
            player.sendUpdateToPlayer();
        }
    }

    /**
     * Gets the bot player.
     *
     * @return the bot player
     */
    public BotPlayer getBot() {
        return bot;
    }

    /**
     * Gets the human players.
     *
     * @return the human players.
     */
    public ArrayList<HumanPlayer> getPlayers() {
        return players;
    }

    /**
     * Gets the column i of resources from the market.
     *
     * @param i number of the column
     * @return the column of resources from the market.
     */
    public ArrayList<Resources> pushColumnMarket(int i) {
        return market.pushColumn(i);
    }

    /**
     * Gets the row i of resources from the market.
     *
     * @param i number of the row
     * @return the row of resources from the market.
     */
    public ArrayList<Resources> pushRowMarket(int i) {
        return market.pushRow(i);
    }

    /**
     * Move the player in the faith path of n positions.
     *
     * @param n      number of positions.
     * @param player moving player
     */
    public void movePlayerFaithPath(int player, int n) {
        faithPath.movePlayer(player, n);
    }

    /**
     * Gets the position of the player in the faith path.
     *
     * @param player player
     * @return the position of the player in the faith path.
     */
    public int getPlayerFaithPathPosition(int player) {
        return faithPath.getPosition(player);
    }

    /**
     * Gets the quantity of victory points earned by the player in faith path.
     *
     * @param player player
     * @return number of victory points earned by the player.
     */
    public int get_PV(int player) {
        return faithPath.get_PV(player);
    }

    /**
     * Gets the deck of color and level specified.
     *
     * @param color color of the deck(Blue,Yellow,Purple,Green)
     * @param level level of the deck(1,2,3)
     * @return the deck of color and level specified.
     */
    public Deck getDeck(Colors color, int level) {
        return decks.getDeck(color,level);
    }
    /**
     * Gets the decks.
     *
     * @return the decks.
     */
    public Decks getDecks() {
        return decks;
    }


    /**
     * Adds a human player
     */

    public void addPlayer(HumanPlayer player) {
        players.add(player);
    }

    /**
     * Gets the market.
     *
     * @return the market
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Gets the faith path.
     *
     * @return the faith path.
     */
    public FaithPath getFaithPath() {
        return faithPath;
    }

    public Communication getPublicCommunication() {
        return publicCommunication;
    }

    public void setPublicCommunication(String communication, CommunicationMessage communicationMessage) {
        this.publicCommunication.setCommunicationMessage(communicationMessage);
        this.publicCommunication.setMessage(communication);
        notifyObserver(new CommunicationMex("", communication, communicationMessage));
    }

    public HumanPlayer getPlayer(String name) {
        for (HumanPlayer player : players)
            if (player.getName().equals(name))
                return player;
        return null;
    }

    public List<String> getPlayersNicknames(){
        ArrayList<String> playersList = new ArrayList<>();
        for(HumanPlayer player : players){
            playersList.add(player.getName());
        }
        return playersList;
    }
}
