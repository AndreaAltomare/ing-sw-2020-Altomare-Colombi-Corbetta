package it.polimi.ingsw.view.clientSide.viewers.cardSelection;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.CardExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.CardSelectionExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.CardsChoosingExecuter;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.EndCardSelectionException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.Iterator;
import java.util.List;

/**
 * Class helping to choose the Card(s).
 * It provides methods to iterate on the Cards as if they were
 * in a circular list, and to select/deselect them.
 *
 * It also provides method to retrieve the List of available cards,
 * the number of cards to be chosen, the Executer and also methods
 * to set all those parameters.
 *
 * The settters are used in the ViewCore to populate it, and the
 * getter -and the selecter/deselecter- are intended to be used by
 * the various Viewers.
 *
 * @author giorgio
 */
public class CardSelection implements Iterator<ViewCard> {

    private List<ViewCard> cardList;
    private CardExecuter cardExecuter;
    private int iterator = 0;

    /**
     * Constructor
     *
     * @param cardList (list of all the ViewCards that can be chosen)
     * @param isChallenger (boolean to indicate if the selection is relative to the challenger)
     */
    public CardSelection(List<ViewCard> cardList, boolean isChallenger){
        if(isChallenger){
            cardExecuter = new CardsChoosingExecuter(ViewPlayer.getNumberOfPlayers());
        }else{
            cardExecuter = new CardSelectionExecuter();
        }
        this.cardList = cardList;
    }

    /**
     * Method that returns the executer associatedto it
     *
     * @return (Executer associated to the object)
     */
    public Executer getExecuter(){
        return (Executer)cardExecuter;
    }


    /**
     * Method to add a card to the selection and send the executer if all the cards have been added.
     *
     * @param card (String identifying the card).
     * @throws WrongParametersException (iif card is not valid)
     * @throws CannotSendEventException (iif theexecuter cannot be sent)
     * @throws EndCardSelectionException (iif the executor was succesfully sent anc so the cards have been chosen)
     */
    public void add(String card) throws WrongParametersException, CannotSendEventException, EndCardSelectionException {
        cardExecuter.add(card);
        if(cardExecuter.getRemaining()==0){
            ((Executer)cardExecuter).doIt();
            throw new EndCardSelectionException();
        }
    }
    /**
     * Method to add a card to the selection and send the executer if all the cards have been added.
     *
     * @param card (ViewCard identifying the card).
     * @throws WrongParametersException (iif card is not valid)
     * @throws CannotSendEventException (iif theexecuter cannot be sent)
     * @throws EndCardSelectionException (iif the executor was succesfully sent anc so the cards have been chosen)
     */
    public void add(ViewCard card) throws WrongParametersException, CannotSendEventException, EndCardSelectionException {
        cardExecuter.add(card);
        if(cardExecuter.getRemaining()==0){
            ((Executer)cardExecuter).doIt();
            throw new EndCardSelectionException();
        }
    }

    /**
     * remove card from the selection (if card was selected)
     *
     * @param card (String identifying the card)
     */
    public void remove(String card) {cardExecuter.remove(card);}

    /**
     * remove card from the selection (if card was selected)
     *
     * @param card (ViewCard identifying the card)
     */
    public void remove(ViewCard card) {cardExecuter.remove(card);}

    /**
     * Method that returns true iif the card has been selected
     *
     * @param card (String identifying the card)
     * @return (boolean true iif the card is not selected)
     */
    public boolean isSelected(String card){ return cardExecuter.isSelected(card); }

    /**
     * Method that returns true iif the card has been selected
     *
     * @param card (ViewCard identifying the card)
     * @return (boolean true iif the card is not selected)
     */
    public boolean isSelected(ViewCard card){ return cardExecuter.isSelected(card); }


    /**
     * Method that returns the number of cards left to be chosen
     *
     * @return (int: the number of cards to be chosen)
     */
    public int getRemaining() {
        return cardExecuter.getRemaining();
    }

    /**
     * Method that returns the list of cards available to be chosen.
     *
     * @return (list of cards available to be chosen)
     */
    public List<ViewCard> getCardList() {
        return cardList;
    }

    /**
     * Method that returns the current card.
     *
     *
     * @return (ViewCard: currentCard)
     */
    public ViewCard getCurrent(){
        if(iterator <0){
            iterator = cardList.size()-1;
        }else if(iterator>=cardList.size()){
            iterator = 0;
        }
        return cardList.get(iterator);
    }

    /**
     * Method inherited by Iterator.
     *
     * @return (true if cardList.size()>0).
     */
    @Override
    public boolean hasNext() {
        return cardList.size()>0;
    }

    /**
     * Method that returns the next card.(and sets it to be the currnt one)
     *
     *
     * @return (ViewCard: next card)
     */
    @Override
    public ViewCard next() {
        iterator++;
        return getCurrent();
    }

    /**
     * Method that returns the previous card.(and sets it to be the currnt one)
     *
     *
     * @return (ViewCard: previous card)
     */
    public ViewCard prev() {
        iterator--;
        return getCurrent();
    }
}
