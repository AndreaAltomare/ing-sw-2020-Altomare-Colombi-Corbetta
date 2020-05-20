package it.polimi.ingsw.view.clientSide.viewers.cardSelection;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.CardExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.CardSelectionExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.CardsChoosingExecuter;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.List;

public class CardSelection {

    private List<ViewCard> cardList;
    private CardExecuter cardExecuter;
    private int iterator = 0;

    public CardSelection(List<ViewCard> cardList, boolean isChallenger){
        if(isChallenger){
            cardExecuter = new CardsChoosingExecuter(ViewPlayer.getNumberOfPlayers());
        }else{
            cardExecuter = new CardSelectionExecuter();
        }
        this.cardList = cardList;
    }

    public Executer getExecuter(){
        return (Executer)cardExecuter;
    }

    public void add(String card) throws WrongParametersException, CannotSendEventException {
        cardExecuter.add(card);
        if(cardExecuter.getRemaining()==0){
            ((Executer)cardExecuter).doIt();
        }
    }
    public void add(ViewCard card) throws WrongParametersException, CannotSendEventException {
        cardExecuter.add(card);
        if(cardExecuter.getRemaining()==0){
            ((Executer)cardExecuter).doIt();
        }
    }

    public void remove(String card) {cardExecuter.remove(card);}
    public void remove(ViewCard card) {cardExecuter.remove(card);}

    public boolean isSelected(String card){ return cardExecuter.isSelected(card); }
    public boolean isSelected(ViewCard card){ return cardExecuter.isSelected(card); }

    public int getNumberToChoose() {
        return cardExecuter.getTotal();
    }

    public List<ViewCard> getCardList() {
        return cardList;
    }

    public ViewCard getCurrent(){
        if(iterator <0){
            iterator = cardList.size()-1;
        }else if(iterator>=cardList.size()){
            iterator = 0;
        }
        return cardList.get(iterator);
    }

    public ViewCard getNext(){
        iterator++;
        return getCurrent();
    }

    public ViewCard getPrec(){
        iterator --;
        return getCurrent();
    }
}
