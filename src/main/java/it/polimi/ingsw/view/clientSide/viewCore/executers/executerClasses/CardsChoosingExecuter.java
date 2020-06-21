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

public class CardsChoosingExecuter extends Executer implements CardExecuter {

    private final int numberOfCards;
    private List<String> cardList;

    public CardsChoosingExecuter(int numberOfCards){
        this.numberOfCards = numberOfCards;
        clear();
    }

    @Override
    public int getRemaining() {
        return numberOfCards - cardList.size();
    }

    @Override
    public int getTotal() {
        return numberOfCards;
    }

    @Override
    public void add(ViewCard card) throws WrongParametersException {
        if(card == null) throw new WrongParametersException("No card selected");
        if (cardList.size()>= numberOfCards) throw new WrongParametersException("Too many cards selected");
        cardList.add(card.getId());
    }

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

    @Override
    public void remove(ViewCard card) {
        cardList.remove(card.getId());
    }

    @Override
    public void remove(String card) {
        cardList.remove(card);

    }

    @Override
    public boolean isSelected(ViewCard card) {
        return cardList.contains(card.getId());
    }

    @Override
    public boolean isSelected(String card) {
        return cardList.contains(card);
    }

    @Override
    public void clear() {
        cardList = new ArrayList<String>(numberOfCards);
    }

    public static String myType(){ return Executer.myType() + "\tCardChoosing"; }

    public EventObject getMyEvent()throws CannotSendEventException {
        if(cardList.size()<numberOfCards) throw new CannotSendEventException("Too few arguments");
        return new CardsChoosingEvent(cardList);
    }

   /* public void send(EventObject event) throws NullPointerException{
        if(event == null) throw new NullPointerException();
        getSender().send((CardsChoosingEvent)event);
    }*/
}
