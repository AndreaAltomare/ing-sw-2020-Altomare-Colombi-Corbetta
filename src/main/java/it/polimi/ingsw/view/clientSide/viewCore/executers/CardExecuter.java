package it.polimi.ingsw.view.clientSide.viewCore.executers;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

public interface CardExecuter {

    public int getRemaining();
    public int getTotal();

    public void add(ViewCard card) throws WrongParametersException;
    public void add(String card) throws WrongParametersException;

    public void remove(ViewCard card);
    public void remove(String card);

    public boolean isSelected(ViewCard card);
    public boolean isSelected(String card);
}
