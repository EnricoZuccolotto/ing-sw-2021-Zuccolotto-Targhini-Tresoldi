package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.utils.GameSaver;
import org.junit.Test;
import static org.junit.Assert.*;

public class SaveGameTest {
    @Test
    public void saveTest(){
        GameController gameController = new GameController(false);
        gameController.setGameState(GameState.IDOL);

        GameSaver.saveGame(gameController);

        GameController loadedGameController = GameSaver.loadGame();
        assertNotNull(loadedGameController);
        assertEquals(loadedGameController.getGameState(), GameState.IDOL);
    }

    @Test
    public void deleteTest(){
        GameController gameController = new GameController(false);
        GameSaver.saveGame(gameController);
        assertNotNull(GameSaver.loadGame());

        GameSaver.deleteSavedGame();
        assertNull(GameSaver.loadGame());
    }
}
