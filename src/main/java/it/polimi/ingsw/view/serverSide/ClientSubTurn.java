package it.polimi.ingsw.view.serverSide;

import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.interfaces.Addressable;

/**
 * enumeration that represents all the types of substatus that there will be during one turn.
 *
 * @author giorgio
 */
//todo remove this class
public enum ClientSubTurn implements Addressable {
    SELECTWORKER("SELECTWORKER"),
    BUILD("BUILD"),
    MOVE("MOVE"),
    BUILDORMOVE("BUILDORMOVE"),
    CANBUILD("CANBUILD"),
    CANMOVE("CANMOVE");

    private String subTurn;

    /**
     * Constructor.
     *
     * @param name (the name of this).
     */
    ClientSubTurn(String name){
        subTurn = name;
    }

    /**
     * Method returning a String identifying the Class.
     *
     * @return (a String identifying the Class).
     */
    public static String getClassId(){ return "[SubTurn]"; }

    /**
     * Method returning a String identifying the Class.
     *
     * @return (a String identifying the Class).
     */
    @Override
    public String getMyClassId() { return getClassId(); }

    /**
     * Method returning a String identifying this inside the Class.
     *
     * @return (a String identifying this inside the Class).
     */
    @Override
    public String getId() { return subTurn; }

    /**
     * Method returning a unique String representing the Object univocally.
     *
     * @return (a unique String representing the Object univocally).
     */
    @Override
    public String toString(){ return getClassId() + "\t" + getId(); }


    /**
     * Method checking if the given Addressable is the same of this.
     *
     * @param pl (the Addressable to be checked).
     * @return  (true iif this == pl).
     */
    @Override
    public boolean equals(Addressable pl) { return isThis(pl.toString()); }

    /**
     * Method to check if the given String is the same representing this.
     *
     * @param st (String that will possibly represent this)
     * @return  (true iif st represents this).
     */
    @Override
    public boolean isThis(String st) { return this.toString().equals(st); }

    /**
     * Method searching the ClientStatus with the given String representation.
     *
     * @param st (the String representation of the searched Object).
     * @return   (the searched Object).
     * @throws NotFoundException    (iif the Object searched doesn't exists).
     */
    public static ClientSubTurn search(String st) throws NotFoundException {
        for (ClientSubTurn i: ClientSubTurn.values() ) {
            if(i.isThis(st))
                return i;
        }
        throw new NotFoundException();
    }
}
