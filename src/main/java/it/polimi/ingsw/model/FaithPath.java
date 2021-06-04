package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.network.messages.FaithPathUpdateMessage;
import it.polimi.ingsw.observer.Observable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents the faith path.
 * cards is an array list of cards containing the 3 Pope's favor tiles.
 * playerPositions contains each player's position
 * playerPV contains all of the victory points earned in the faith path
 * cardState contains the state of each card for each player.
 * report represents how many vatican reports occurred
 */
public class FaithPath extends Observable implements Serializable {
    private final ArrayList<Card> cards;
    private int[] playerPositions;
    private int[] playerPV;
    private boolean[][] cardState;
    private int report;

    /**
     * Build a new faith path with n players.
     */
    public FaithPath() {
        report = 0;
        cards = CardParser.parseFaithCards();
    }

    /**
     * Initialize the faith path with n players.
     *
     * @param n number of players
     */
    public void init(int n) {
        playerPositions = new int[n];
        playerPV = new int[n];
        cardState = new boolean[n][3];
    }

    /**
     * Move the player of n positions.
     *
     * @param n      number of positions.
     * @param player moving player
     */
    public void movePlayer(int player, int n) {
        playerPositions[player] += n;
        checkReport(player);
        notifyObserver(new FaithPathUpdateMessage(this));
    }


    /**
     * Check the position of each player and when a Faith Marker reaches (or goes beyond) a Pope
     * space, make a Vatican Report
     *
     * @param player that moved
     */
    private void checkReport(int player) {
        int[] rep = {5, 8, 12, 16, 19, 24};

        ArrayList<Integer> playerReported = new ArrayList<>();
        //saving number of player
        for (int i = 0; i < playerPositions.length; i++)
            playerReported.add(i);

        while (playerReported.size() != 0 && report < 3) {
            int w = playerReported.get((player) % playerReported.size());
            if (playerPositions[w] >= rep[report * 2 + 1]) {
                //check if the players are in a papal report
                for (int count = playerReported.size() - 1; count >= 0; count--) {
                    if (playerPositions[count] >= rep[report * 2]) {
                        playerPV[count] += cards.get(report).getVP();
                        cards.get(report).flipCard();
                        cardState[count][report] = true;
                    } else playerReported.remove((Integer) count);
                }
                report++;
            } else playerReported.remove((Integer) w);
        }

        if (playerPositions[player] >= 25) {
            throw new WinnerException();
        }

    }

    /**
     * Gets the quantity of victory points earned by the player in faith path.
     *
     * @param player player
     * @return number of victory points earned by the player.
     */
    public int get_PV(int player) {
        if (playerPositions[player] >= 24)
            playerPV[player] += 20;
        else if (playerPositions[player] >= 21)
            playerPV[player] += 16;
        else if (playerPositions[player] >= 18)
            playerPV[player] += 12;
        else if (playerPositions[player] >= 15)
            playerPV[player] += 9;
        else if (playerPositions[player] >= 12)
            playerPV[player] += 6;
        else if (playerPositions[player] >= 9)
            playerPV[player] += 4;
        else if (playerPositions[player] >= 6)
            playerPV[player] += 2;
        else if (playerPositions[player] >= 3)
            playerPV[player] += 1;
        return playerPV[player];
    }

    /**
     * Gets the card state of the card number  for the player.
     *
     * @param player player
     * @param number Pope's tile
     * @return the state of the card number for the player.
     */
    public boolean getCardsState(int number, int player) {
        return cards.get(number).getUncovered() && cardState[player][number];
    }

    /**
     * Gets the faith card in position pos.
     *
     * @param pos position of the card
     * @return the faith card in position pos.
     */
    public Card getCard(int pos) {
        return cards.get(pos);
    }

    /**
     * Gets the position of the player in the faith path.
     *
     * @param player player
     * @return the position of the player in the faith path.
     */
    public int getPosition(int player) {
        return playerPositions[player];
    }

    @Override
    public String toString() {
        final int[] rep = new int[]{8, 16, 24};
        return "FaithPath: " +
                " playerPositions=" + Arrays.toString(playerPositions) +
                ", report=" + rep[report];
    }
}
