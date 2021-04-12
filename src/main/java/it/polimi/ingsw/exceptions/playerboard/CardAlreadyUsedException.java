package it.polimi.ingsw.exceptions.playerboard;

public class CardAlreadyUsedException extends RuntimeException {
    public CardAlreadyUsedException(){
        super("Card Already Used");
    }
}
