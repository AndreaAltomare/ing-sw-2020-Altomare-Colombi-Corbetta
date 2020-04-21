package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.GodPower;

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
}
