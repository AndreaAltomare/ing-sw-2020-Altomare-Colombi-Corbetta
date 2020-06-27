package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.executers.CardExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.CardSelectionEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

import java.util.EventObject;

/**
 * Class to execute a Card Selection
 *
 * @see Executer
 */
public class CardSelectionExecuter extends Executer implements CardExecuter {
    private String nameCard;

    /**
     * Method that reset the executer with initial values.
     */
    @Override
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


    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    @Override
    public EventObject getMyEvent()throws CannotSendEventException {
        if(nameCard == null) throw new CannotSendEventException("You haven't choose any card so far");
        return new CardSelectionEvent(nameCard);
    }

    /**
     * Method that returns the number of cards left to be choosen.
     *
     * @return (the number of cards left to be choosen).
     */
    @Override
    public int getRemaining() {
        if(nameCard != null)
            return 0;
        return 1;
    }

    /**
     * Method to retrieve the total number of cards to choose.
     *
     * @return (the total number of cards to choose).
     */
    @Override
    public int getTotal() {
        return 1;
    }

    /**
     * Method to add (select) a card.
     *
     * @param card (<code>ViewCard</code> to be choosen).
     * @throws WrongParametersException (iif there is an error loading the card)
     */
    @Override
    public void add(ViewCard card) throws WrongParametersException {
        setNameCard(card);
    }

    /**
     * Method to add (select) a card.
     *
     * @param card (name of the card to be choosen).
     * @throws WrongParametersException (iif there is an error loading the card)
     */
    @Override
    public void add(String card) throws WrongParametersException {
        setNameCard(card);
    }

    /**
     * Method that removes the given <code>ViewCard</code> from the selected ones.
     *
     * @param card (the <code>ViewCard</code> to be unseleccted).
     */
    @Override
    public void remove(ViewCard card) {
        remove(card.getId());
    }

    /**
     * Method that removes the given Card from the selected ones.
     *
     * @param card (the Card's name of the Card to be unselected).
     */
    @Override
    public void remove(String card) {
        if(nameCard.equals(card))
            nameCard = null;
    }

    /**
     * Method to check weather the given <code>ViewCard</code> has been selected or not.
     *
     * @param card (the <code>ViewCard</code> to be checked).
     * @return  (true iif card has been selected).
     */
    @Override
    public boolean isSelected(ViewCard card) {
        return card.getId().equals(nameCard);
    }

    /**
     * Method to check weather the given Card has been selected or not.
     *
     * @param card (the Card to be checked).
     * @return  (true iif card has been selected).
     */
    @Override
    public boolean isSelected(String card) {
        return nameCard.equals(card);
    }

    /*public void send(EventObject event) throws NullPointerException{
        if(event == null) throw new NullPointerException();
        getSender().send((CardSelectionEvent)event);
    }*/
}
