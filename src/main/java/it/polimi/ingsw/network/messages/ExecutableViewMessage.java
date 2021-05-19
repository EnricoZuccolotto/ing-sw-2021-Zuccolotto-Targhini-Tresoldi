package it.polimi.ingsw.network.messages;


import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;

public interface ExecutableViewMessage {
    void executeOnView(View view, ExecutorService taskQueue);
}
