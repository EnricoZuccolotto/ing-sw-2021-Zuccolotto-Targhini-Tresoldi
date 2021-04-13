package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.playerboard.WinnerException;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.model.cards.Card;

import java.util.ArrayList;
import java.util.Arrays;

public class FaithPath {
    private final ArrayList<Card> cards;
    private final int[] playerPositions;
    private final int[] playerPV;
    private final boolean[][] cardstate;
    private int report;
    public FaithPath(int n){
        playerPositions=new int [n];

        playerPV=new int [n];
        cardstate= new boolean[n][3];
        report=0;
        cards=CardParser.parseFaithCards();

    }

     public void movePlayer(int player,int n){
        playerPositions[player]+=n;
        checkReport(player);
        // TODO: notifyObserver()
     }
    private void checkReport(int player){
        int [] rep={5,8,12,16,19,24};
        int k=0;
        if (playerPositions[k] >= 25) {
            throw new WinnerException();
        }

        ArrayList<Integer> playerReported=new ArrayList<>();
        //saving number of player
        for(int i=0;i<playerPositions.length;i++)
            playerReported.add(i);

            while( k<playerReported.size() && playerReported.size()!=0 && report<3) {
                     int w=playerReported.get((k+player)%playerReported.size());
                    if (playerPositions[w] == rep[report * 2 + 1]) {
                         //check if the players are in a papal report
                        for( int count:playerReported) {
                            if (playerPositions[count] >= rep[report * 2]) {
                                playerPV[count] += cards.get(report).getVP();
                                cards.get(report).flipCard();
                                cardstate[count][report] = true;
                            }
                        }

                        report++;
                        // remove the player that already reported because he can't be in another papal report space at the same time
                        playerReported.remove((Integer) w);
                        k=-1;
                    }
                    else if (playerPositions[w] < rep[report * 2 + 1])
                        playerReported.remove((Integer) w);

               k++;
                }

            }
    public int get_PV(int player){
        if(playerPositions[player]>=24)
            playerPV[player]+=20;
        else if(playerPositions[player]>=21)
            playerPV[player]+=16;
        else if(playerPositions[player]>=18)
            playerPV[player]+=12;
        else if(playerPositions[player]>=15)
            playerPV[player]+=9;
        else if(playerPositions[player]>=12)
            playerPV[player]+=6;
        else if(playerPositions[player]>=9)
            playerPV[player]+=4;
        else if(playerPositions[player]>=6)
            playerPV[player]+=2;
        else if(playerPositions[player]>=3)
            playerPV[player]+=1;
        return playerPV[player];
    }
    public boolean getCardsState(int number, int player){
        return cards.get(number).getUncovered() && cardstate[player][number];
    }
    public int getPosition(int player){
        return playerPositions[player];
    }

    @Override
    public String toString() {
        return "FaithPath" +
                ", playerPositions=" + Arrays.toString(playerPositions) +
                ", playerPV=" + Arrays.toString(playerPV) +
                ", report=" + report;
    }
}
