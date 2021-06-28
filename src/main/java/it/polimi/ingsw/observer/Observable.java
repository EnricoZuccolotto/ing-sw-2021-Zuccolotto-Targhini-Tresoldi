package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.tools.MORLogger;
import it.polimi.ingsw.network.Client.SocketClient;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements the Observer pattern for model elements.
 */
public class Observable {
    private final List<Observer> observerList = new ArrayList<>();

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
            try {
                o.update(message);
            } catch (NullPointerException ex) {
                // This could happen if a client disconnects during an update message. Do nothing, maybe log something
                MORLogger.LOGGER.info("A client has disconnected");
            }
        }
    }
}
