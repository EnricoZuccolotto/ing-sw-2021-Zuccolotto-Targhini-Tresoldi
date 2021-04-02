package it.polimi.ingsw.model;
import it.polimi.ingsw.model.tools.CardParser;
import it.polimi.ingsw.model.cards.Card;

import java.util.ArrayList;
import java.util.Arrays;

public class FaithPath {
    private ArrayList<Card> cards;
    private int[] playerPositions;
    private int[] playerPV;
    private int report;
    public FaithPath(int n){
        playerPositions=new int [n];
        for(int i=0;i<n;i++) {
            playerPositions[i]=0;
        }
        playerPV=new int [n];
        for(int i=0;i<n;i++) {
            playerPV[i]=0;
        }
        report=0;
        cards=CardParser.parseFaithCards();

    }

     public void movePlayer(int player,int n){
        playerPositions[player]+=n;
        checkReport(player);
     }
    private void checkReport(int player){
        int [] rep={5,8,12,16,19,24,25};
        if(report<3){
            for(int k=0;k<=playerPositions.length;k++){
                if(playerPositions[(player+k)%playerPositions.length]==rep[report*2+1])
                {
                    System.out.println((player+k)%playerPositions.length);
                    for(int i=0;i<playerPositions.length;i++)
                        if(playerPositions[i]>=rep[report*2]) {
                            playerPV[i] += cards.get(report).getVP();
                        }
                    report++;
                }
        }}
        else
            if(playerPositions[player]>=25)
            System.out.println("End game");
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

    @Override
    public String toString() {
        return "FaithPath" +
                "cards=" + cards +
                ", playerPositions=" + Arrays.toString(playerPositions) +
                ", playerPV=" + Arrays.toString(playerPV) +
                ", report=" + report;
    }
}
