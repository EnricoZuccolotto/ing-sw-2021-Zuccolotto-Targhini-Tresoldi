package it.polimi.ingsw.observer;

import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements the Observer pattern for model elements.
 */
public class Observable {
    private List<Observer> observerList = new ArrayList<>();

    /**
     * Adds an observer to the list. In this particular case every part of the model that needs to be sent to the interface
     * should use an Observer at some point or another.
     * @param observer Observer to be added
     */
    public void addObserver(Observer observer){
        observerList.add(observer);
    }

    /**
     * Removes the observer from the update list.
     * @param observer Observer to be removed.
     */
    public void removeObserver(Observer observer){
        observerList.remove(observer);
    }

    /**
     * Method that informs the view on a particular model change. It sends a message to the view, either via network or directly.
     * @param message The message the model wants to send.
     */
    protected void notifyObserver(Message message){
        for(Observer o : observerList){
            o.update(message);
        }
    }
}