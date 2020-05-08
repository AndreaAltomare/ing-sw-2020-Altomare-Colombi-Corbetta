package it.polimi.ingsw.storage;

import com.google.gson.Gson;
import it.polimi.ingsw.connection.ConnectionSettings;
import it.polimi.ingsw.model.CardInfo;
import it.polimi.ingsw.model.GodPower;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class let resource retrieval operations
 * simpler since it encapsulates GSON logic
 * and instantiate directly the class needed
 * to perform a certain task.
 *
 * @author AndreaAltomare
 */
public class ResourceManager {

    /**
     * Get a Card's information and return its GodPower properties.
     *
     * @param cardName (Card's name)
     * @return GodPower's instance (Card's GodPower properties)
     */
    public static GodPower callGodPower(String cardName) {
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
        String filePath = "gods.config";
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
        return getConnectionSettingsFromPath("connection_settings/client_settings.config");
    }

    /**
     * Get connection settings for Server.
     *
     * @return ConnectionSettings' instance
     */
    public static ConnectionSettings serverConnectionSettings() {
        return getConnectionSettingsFromPath("connection_settings/server_settings.config");
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
}
