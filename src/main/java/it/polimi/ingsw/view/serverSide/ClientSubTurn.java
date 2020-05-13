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

    ClientSubTurn(String name){
        subTurn = name;
    }

    public static String getClassId(){ return "[SubTurn]"; }

    @Override
    public String getMyClassId() { return getClassId(); }

    @Override
    public String getId() { return subTurn; }

    @Override
    public String toString(){ return getClassId() + "\t" + getId(); }

    @Override
    public boolean equals(Addressable pl) { return isThis(pl.toString()); }

    @Override
    public boolean isThis(String st) { return this.toString().equals(st); }

    public static ClientSubTurn search(String st) throws NotFoundException {
        for (ClientSubTurn i: ClientSubTurn.values() ) {
            if(i.isThis(st))
                return i;
        }
        throw new NotFoundException();
    }
}
