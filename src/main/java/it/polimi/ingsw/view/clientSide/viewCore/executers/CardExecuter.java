package it.polimi.ingsw.view.clientSide.viewCore.executers;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

public interface CardExecuter {

    /**
     * Method that returns the number of cards left to be choosen.
     *
     * @return (the number of cards left to be choosen).
     */
    public int getRemaining();

    /**
     * Method to retrieve the total number of cards to choose.
     *
     * @return (the total number of cards to choose).
     */
    public int getTotal();



    /**
     * Method to add (select) a card.
     *
     * @param card (<code>ViewCard</code> to be choosen).
     * @throws WrongParametersException (iif there is an error loading the card)
     */
    public void add(ViewCard card) throws WrongParametersException;

    /**
     * Method to add (select) a card.
     *
     * @param card (name of the card to be choosen).
     * @throws WrongParametersException (iif there is an error loading the card)
     */
    public void add(String card) throws WrongParametersException;


    /**
     * Method that removes the given <code>ViewCard</code> from the selected ones.
     *
     * @param card (the <code>ViewCard</code> to be unseleccted).
     */
    public void remove(ViewCard card);

    /**
     * Method that removes the given Card from the selected ones.
     *
     * @param card (the Card's name of the Card to be unselected).
     */
    public void remove(String card);


    /**
     * Method to check weather the given <code>ViewCard</code> has been selected or not.
     *
     * @param card (the <code>ViewCard</code> to be checked).
     * @return  (true iif card has been selected).
     */
    public boolean isSelected(ViewCard card);

    /**
     * Method to check weather the given Card has been selected or not.
     *
     * @param card (the Card to be checked).
     * @return  (true iif card has been selected).
     */
    public boolean isSelected(String card);
}
