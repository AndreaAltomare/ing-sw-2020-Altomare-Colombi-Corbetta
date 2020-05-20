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

import java.util.List;

public class CardSelection {

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
    public void add(ViewCard card) throws WrongParametersException, CannotSendEventException {
        cardExecuter.add(card);
        if(cardExecuter.getRemaining()==0){
            ((Executer)cardExecuter).doIt();
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
    public int getRemaiing() {
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
     * Method that returns the next card.(and sets it to be the currnt one)
     *
     *
     * @return (ViewCard: next card)
     */
    public ViewCard getNext(){
        iterator++;
        return getCurrent();
    }

    /**
     * Method that returns the previous card.(and sets it to be the currnt one)
     *
     *
     * @return (ViewCard: previous card)
     */
    public ViewCard getPrec(){
        iterator --;
        return getCurrent();
    }
}
