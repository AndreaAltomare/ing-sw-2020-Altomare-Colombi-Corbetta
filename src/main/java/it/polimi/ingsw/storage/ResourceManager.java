package it.polimi.ingsw.storage;

import com.google.gson.Gson;
import it.polimi.ingsw.connection.ConnectionSettings;
import it.polimi.ingsw.model.GodPower;

import java.io.FileNotFoundException;

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
