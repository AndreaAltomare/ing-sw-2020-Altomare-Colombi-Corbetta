package it.polimi.ingsw.model.card;

import java.io.Serializable;

/**
 * This class encapsulates the Card's information which a Player
 * needs in order to make his/her choice.
 *
 * @author AndreaAltomare
 */
public class CardInfo implements Serializable {
    private String name;
    private String epithet;
    private String description;

    /**
     * Constructs a CardInfo objects which holds
     * descriptive information about a Card.
     *
     * @param name Card's name
     * @param epithet Card's epithet
     * @param description Card's description
     */
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
