package it.polimi.ingsw.utils;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.tools.MORLogger;

import java.io.*;
import java.nio.file.Files;

/**
 * This class implements utility methods for the Persistence advanced feature.
 */
public class GameSaver {

    /**
     * Saves the current state of the game to a file.
     * @param gameController The gameController to save to disk.
     */
    public static void saveGame(GameController gameController){
        GameToSave gameToSave = new GameToSave(gameController);

        try {
            FileOutputStream saveStream = new FileOutputStream("savedgame.mor");
            ObjectOutputStream objectWriter = new ObjectOutputStream(saveStream);
            objectWriter.writeObject(gameToSave);
            objectWriter.close();
            saveStream.close();
        } catch(IOException ex){
            MORLogger.LOGGER.severe(ex.getMessage());
        }
    }

    /**
     * Deletes the currently saved game.
     */
    public static void deleteSavedGame(){
        File file = new File("savedgame.mor");
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException ex){
            MORLogger.LOGGER.severe(ex.getMessage());
        }
    }

    /**
     * Loads the game from disk.
     * @return The restored GameController.
     */
    public static GameController loadGame(){
        GameToSave restoredGame;
        try{
            FileInputStream loadStream = new FileInputStream("savedgame.mor");
            ObjectInputStream objectReader = new ObjectInputStream(loadStream);
            restoredGame = (GameToSave) objectReader.readObject();
            objectReader.close();
            loadStream.close();
            return restoredGame.getGameController();
        } catch (IOException ex){
            MORLogger.LOGGER.severe("Error loading from file!");
        } catch (ClassNotFoundException ex) {
            MORLogger.LOGGER.severe(ex.getMessage());
        }
        return null;
    }
}
