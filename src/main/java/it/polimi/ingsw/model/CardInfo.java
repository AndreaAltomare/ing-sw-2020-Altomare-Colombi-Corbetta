package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class encapsulate the Card's information which a Player
 * needs in order to make his/her choice.
 *
 * @author AndreaAltomare
 */
public class CardInfo implements Serializable {
    private String name;
    private String epithet;
    private String description;

    public CardInfo(String name, String epithet, String description) {
        this.name = name;
        this.epithet = epithet;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEpithet() {
        return epithet;
    }

    public void setEpithet(String epithet) {
        this.epithet = epithet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
