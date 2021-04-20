package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.Collections;

// Indici 0, 1, 2
public class Deck {
    private  final ArrayList<DevelopmentCard> deck;

    public Deck(){
     deck=new ArrayList<>();
    }

    public ArrayList<DevelopmentCard> getDeck(){
        return deck;
    }
    public void addCard(DevelopmentCard c){
        if (c == null) throw new NullPointerException("DevelopmentCard cannot be null");
        deck.add(c);
    }
    public int DeckLength(){
        return deck.size();
    }

    public  DevelopmentCard getFirstCard(){

        return (DevelopmentCard) deck.toArray()[0];
    }

    public DevelopmentCard popFirstCard(){
        try {
            return deck.remove(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    public void popLastCard(){
            deck.remove(deck.size()-1);
    }

    public void shuffle(){
        Collections.shuffle(deck);
    }

}
