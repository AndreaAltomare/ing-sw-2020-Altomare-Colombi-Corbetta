package it.polimi.ingsw.storage;

import com.google.gson.Gson;
import it.polimi.ingsw.connection.ConnectionSettings;
import it.polimi.ingsw.model.CardInfo;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.GodPower;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class let resource retrieval operations
 * simpler since it encapsulates GSON logic
 * and instantiate directly the class needed
 * to perform a certain task.
 *
 * @author AndreaAltomare
 */
public class ResourceManager {
    private static ExecutorService executor = Executors.newFixedThreadPool(10); // for async heavy I/O interaction
    private static final String CONNECTION_RESOURCES_PATH = "connection_settings/";
    private static final String GAME_RESOURCES_PATH = "game_resources/";
    private static final String CARDS_PATH = "cards/";
    private static final String SAVES_PATH = "game_saves/";

    /**
     * Get a Card's information and return its GodPower properties.
     *
     * @param cardName (Card's name)
     * @return GodPower's instance (Card's GodPower properties)
     */
    public static GodPower callGodPower(String cardName) {
        String cardPath = GAME_RESOURCES_PATH + CARDS_PATH + cardName;
        FileManager fileManager = FileManager.getIstance();
        String json = fileManager.getCard(cardName);

        /* JSON DESERIALIZATION WITH GSON */
        Gson gson = new Gson();
        return gson.fromJson(json, GodPower.class);
    }

    /**
     * Get a list with all Cards' information
     *
     * @return (List fo CardInfo objects)
     */
    public static List<CardInfo> getCardsInformation() {
        /* 1- Get what cards are in the Game */
        List<CardInfo> cards = new ArrayList<>();
        List<String> cardNames;
        String filePath = GAME_RESOURCES_PATH + CARDS_PATH + "gods.config";
        GodPower god;

        FileManager fileManager = FileManager.getIstance();
        try {
            cardNames = fileManager.getFileRecords(filePath);
        }
        catch (FileNotFoundException ex) {
            cardNames = new ArrayList<>(); // empty List
        }

        /* 2- Get all Card's information*/
        for(String cardName : cardNames) {
            // todo code (maybe write a lambda ex)
            god = callGodPower(cardName);
            cards.add(new CardInfo(god.getName(), god.getEpithet(), god.getDescription()));
        }

        return cards;
    }

    /**
     * Get connection settings for Client.
     *
     * @return ConnectionSettings' instance
     */
    public static ConnectionSettings clientConnectionSettings() {
        return getConnectionSettingsFromPath(CONNECTION_RESOURCES_PATH + "client_settings.config");
    }

    /**
     * Get connection settings for Server.
     *
     * @return ConnectionSettings' instance
     */
    public static ConnectionSettings serverConnectionSettings() {
        return getConnectionSettingsFromPath(CONNECTION_RESOURCES_PATH + "server_settings.config");
    }

    /**
     * Get connection settings from a specified file path.
     *
     * @param filePath (Specified file path)
     * @return ConnectionSettings' instance
     */
    private static ConnectionSettings getConnectionSettingsFromPath(String filePath) {
        String json = "";

        FileManager fileManager = FileManager.getIstance();
        try {
            json = fileManager.getFileContent(filePath);
        }
        catch (FileNotFoundException ex) {
            return null;
        }

        /* JSON DESERIALIZATION WITH GSON */
        Gson gson = new Gson();
        return gson.fromJson(json, ConnectionSettings.class);
    }

    /**
     * Saves a Game.
     *
     * @param gameState State of the game
     */
    public static void saveGameState(GameState gameState) {
        Runnable saveTask = new Runnable() {
            @Override
            public void run() {
                String fileName = GAME_RESOURCES_PATH + SAVES_PATH + "saving.dat";
                Gson gson;
                String json;
                FileManager fileManager = FileManager.getIstance();

                /* JSON SERIALIZATION WITH GSON */
                gson = new Gson();
                json = gson.toJson(gameState);

                /* FILE SAVING */
                synchronized (fileManager) {
                    fileManager.saveFileContent(fileName, json);
                }
            }
        };

        executor.submit(saveTask);
    }

    /**
     * Loads a Game.
     *
     * @return State of the game
     */
    public static GameState loadGameState() {
        String fileName = GAME_RESOURCES_PATH + SAVES_PATH + "saving.dat";
        Gson gson;
        String json;
        FileManager fileManager = FileManager.getIstance();

        synchronized (fileManager) {
            try {
                json = fileManager.getFileContent(fileName);
            } catch (FileNotFoundException ex) {
                return null;
            }
        }

        /* JSON DESERIALIZATION WITH GSON */
        gson = new Gson();
        return gson.fromJson(json, GameState.class);
    }
}
