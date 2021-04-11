package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.enums.Colors;

import java.util.Stack;

public class SpaceProd {
    private Stack<DevelopmentCard> spaceProd;

    public SpaceProd(DevelopmentCard c){
        spaceProd=new Stack<>();
     this.spaceProd.push(c);

    }

    public int getVictoryPoints() {
        int cont=0;
        for(DevelopmentCard c:spaceProd)
            cont+=c.getVP();
        return cont;
    }

    public void addCard(DevelopmentCard card){
        spaceProd.push(card);
    }

    public int checkColor(Colors c){
        int cont=0;
        for(DevelopmentCard card:spaceProd){

            if(card.getColor().equals(c)) {

                cont += 1;
            }
        }
                return cont;
    }
    public DevelopmentCard getTop(){
        return spaceProd.peek();
    }
    public int getNumbCard(){
        return spaceProd.size();
    }
    @Override
    public String toString() {
        return "SpaceProd"
                 + spaceProd
                ;
    }
}
