package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.BuildBlockEvent;
import it.polimi.ingsw.view.events.CardSelectionEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

import java.util.EventObject;

public class CardSelectionExecuter extends Executer {
    private String nameCard;

    /**
     * Method that reset the executer with initial values.
     */
    public void clear(){
        nameCard = null;
    }

    /**
     * constructor
     */
    public CardSelectionExecuter(){
        this.clear();
    }

    /**
     * Method to set hte name of the card.
     *
     * @param nameCard (String: the name of the card)
     * @throws WrongParametersException (if nameCard is not the name of any card).
     */
    public void setNameCard(String nameCard) throws WrongParametersException{
        try {
            this.nameCard = ViewCard.search(nameCard).getId();
        } catch (NotFoundException | WrongViewObjectException e) {
            throw new WrongParametersException();
        }
    }

    /**
     * Method to set hte name of the card.
     *
     * @param card (ViewCard selected)
     * @throws WrongParametersException (if card == null).
     */
    public void setNameCard(ViewCard card) throws WrongParametersException{
        if(card == null) throw new WrongParametersException();
        this.nameCard = card.getId();
    }

    /**
     * Static method that returns a univoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public static String myType(){ return Executer.myType() + "\tCardSelection"; }

    @Override
    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    public EventObject getMyEvent()throws CannotSendEventException {
        if(nameCard == null) throw new CannotSendEventException("You haven't choose any card so far");
        return new CardSelectionEvent(nameCard);
    }
}
