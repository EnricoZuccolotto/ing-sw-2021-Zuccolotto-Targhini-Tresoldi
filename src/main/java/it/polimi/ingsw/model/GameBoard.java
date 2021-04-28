package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Communication.Communication;
import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.enums.Resources;
import it.polimi.ingsw.model.player.BotPlayer;
import it.polimi.ingsw.model.player.HumanPlayer;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.observer.Observable;

import java.util.ArrayList;
import java.util.Optional;

/**
 * This class represents the game board.
 * market represents the market.
 * decks represents the 12 deck of 4 cards each one of 1 color(Blue,Yellow,Purple,Green) and 1 level(1,2,3)
 * players represents the players that are playing
 * faithPath represents the faith path.
 * Bot represents the bot player, if there is one.
 */
public class GameBoard extends Observable {
    private final Market market;
    private final Deck[][] decks;
    private final ArrayList<HumanPlayer> players;
    private final FaithPath faithPath;
    private Optional<BotPlayer> Bot;
    private final Communication publicCommunication;


    /**
     * Build a new Game board.
     */
    public GameBoard() {
        publicCommunication = new Communication();
        this.decks = CardParser.parseDevCards();
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
            this.faithPath.init(2);
            Bot = Optional.of(new BotPlayer(gameBoard));
        } else this.faithPath.init(players.size());
    }

    /**
     * Gets the bot player.
     *
     * @return the bot player
     */
    public BotPlayer getBot() {
        return Bot.orElse(null);
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
        level--;
        return decks[color.ordinal()][level];
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
    }

    public HumanPlayer getPlayer(String name) {
        for (HumanPlayer player : players)
            if (player.getName().equals(name))
                return player;
        return null;
    }
}
