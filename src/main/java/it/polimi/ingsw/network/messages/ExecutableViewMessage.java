package it.polimi.ingsw.network.messages;


import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;

/**
 * This class represents an executable message on the view.
 */
public interface ExecutableViewMessage {
    /**
     * Execute the action associated with this message on the cli/gui.
     *
     * @param view      View used to execute this message.
     * @param taskQueue Thread used to perform this action.
     */
    void executeOnView(View view, ExecutorService taskQueue);
}
