package it.polimi.ingsw.test;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.utils.GameSaver;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class is used to test the Persistence advanced functionality.
 */
public class SaveGameTest {
    /**
     * Saves the game, reloads it and checks it.
     */
    @Test
    public void saveTest(){
        GameController gameController = new GameController(false);
        gameController.setGameState(GameState.IDOL);

        GameSaver.saveGame(gameController);

        GameController loadedGameController = GameSaver.loadGame();
        assertNotNull(loadedGameController);
        assertEquals(loadedGameController.getGameState(), GameState.IDOL);
    }

    /**
     * Tries to delete a game and checks if it has correctly deleted.
     */
    @Test
    public void deleteTest(){
        GameController gameController = new GameController(false);
        GameSaver.saveGame(gameController);
        assertNotNull(GameSaver.loadGame());

        GameSaver.deleteSavedGame();
        assertNull(GameSaver.loadGame());
    }
}
