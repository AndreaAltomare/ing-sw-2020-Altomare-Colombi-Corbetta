package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.executers.CardExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.CardsChoosingEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Class to execute a Cards Choosing.
 *
 * @see Executer
 */
public class CardsChoosingExecuter extends Executer implements CardExecuter {

    private final int numberOfCards;
    private List<String> cardList;


    /**
     * constructor.
     *
     * @param numberOfCards (number of cards to be selected).
     */
    public CardsChoosingExecuter(int numberOfCards){
        this.numberOfCards = numberOfCards;
        clear();
    }

    /**
     * Method that returns the number of cards left to be choosen.
     *
     * @return (the number of cards left to be choosen).
     */
    @Override
    public int getRemaining() {
        return numberOfCards - cardList.size();
    }

    /**
     * Method to retrieve the total number of cards to choose.
     *
     * @return (the total number of cards to choose).
     */
    @Override
    public int getTotal() {
        return numberOfCards;
    }

    /**
     * Method to add (select) a card.
     *
     * @param card (<code>ViewCard</code> to be choosen).
     * @throws WrongParametersException (iif there is an error loading the card)
     */
    @Override
    public void add(ViewCard card) throws WrongParametersException {
        if(card == null) throw new WrongParametersException("No card selected");
        if (cardList.size()>= numberOfCards) throw new WrongParametersException("Too many cards selected");
        cardList.add(card.getId());
    }

    /**
     * Method to add (select) a card.
     *
     * @param card (name of the card to be choosen).
     * @throws WrongParametersException (iif there is an error loading the card)
     */
    @Override
    public void add(String card) throws WrongParametersException {
        if(cardList.contains(card)){
            throw new WrongParametersException("Already selected");
        }
        if (cardList.size()>= numberOfCards) throw new WrongParametersException("Too many cards selected");
        try {
            cardList.add(ViewCard.search(card).getId());
        } catch (NotFoundException | WrongViewObjectException e) {
            throw new WrongParametersException("Cannot find the card");
        }
    }

    /**
     * Method that removes the given <code>ViewCard</code> from the selected ones.
     *
     * @param card (the <code>ViewCard</code> to be unseleccted).
     */
    @Override
    public void remove(ViewCard card) {
        cardList.remove(card.getId());
    }

    /**
     * Method that removes the given Card from the selected ones.
     *
     * @param card (the Card's name of the Card to be unselected).
     */
    @Override
    public void remove(String card) {
        cardList.remove(card);

    }

    /**
     * Method to check weather the given <code>ViewCard</code> has been selected or not.
     *
     * @param card (the <code>ViewCard</code> to be checked).
     * @return  (true iif card has been selected).
     */
    @Override
    public boolean isSelected(ViewCard card) {
        return cardList.contains(card.getId());
    }

    /**
     * Method to check weather the given Card has been selected or not.
     *
     * @param card (the Card to be checked).
     * @return  (true iif card has been selected).
     */
    @Override
    public boolean isSelected(String card) {
        return cardList.contains(card);
    }

    /**
     * Method that reset the executer with initial values.
     */
    @Override
    public void clear() {
        cardList = new ArrayList<String>(numberOfCards);
    }

    /**
     * Static method that returns a univoque string for each type of Exeuter (in this specific case "[Executer]\tCardChoosing").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public static String myType(){ return Executer.myType() + "\tCardChoosing"; }

    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    @Override
    public EventObject getMyEvent()throws CannotSendEventException {
        if(cardList.size()<numberOfCards) throw new CannotSendEventException("Too few arguments");
        return new CardsChoosingEvent(cardList);
    }

   /* public void send(EventObject event) throws NullPointerException{
        if(event == null) throw new NullPointerException();
        getSender().send((CardsChoosingEvent)event);
    }*/
}
