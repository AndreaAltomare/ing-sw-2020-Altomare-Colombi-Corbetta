package it.polimi.ingsw.view.interfaces;

/**
 * Interface to represent all the Objects that will be addressed by the connection layer (so they'll have a similar interface and will be find more easily.
 *
 * @author giorgio
 */
public interface Addressable {
    /**
     * Method returning a unique string for each object inside the Class.
     *
     * @return (unique String identifying the object)
     */
    public String getId();


    /**
     * Method returning a unique String for each class.
     *
     * @return (unique string for each class)
     */
    public String getMyClassId();

    /**
     * Method returning a unique String for each Object composed as: getMyClassId()+"\t"+getId().
     *
     * @return (Unique string for each object)
     */
    public String toString();

    /**
     * Compares this with pl. return true iif represent the same Object.
     *
     * @param pl (the Addressable to be checked)
     * @return (true iif this == pl)
     */
    public boolean equals(Addressable pl);


    /**
     * Method checking weather the given string is identifying this.
     *
     * @param st (String that will possibly represent this)
     * @return (true iif st==this.toString())
     */
    public boolean isThis (String st);
}
