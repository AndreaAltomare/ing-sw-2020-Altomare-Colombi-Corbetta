package it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;

import javax.swing.*;

/**
 * Class that implements the <code>SpecificSubTurnViewer</code> fot the GUI.
 */
public abstract class GUISubTurnViewer implements SpecificSubTurnViewer {

    protected ViewSubTurn viewSubTurn;
    protected SubTurnViewer parent;

    /**
     * constructor.
     *
     * @param parent (the <code>SubTurnViewer</code> to which this refers to).
     */
    protected GUISubTurnViewer(SubTurnViewer parent){
        this.parent = parent;
        if(parent != null)
            viewSubTurn = parent.getMySubTurn();
        else
            viewSubTurn = null;
    }

    /**
     * Method that returns the subTurnPanel relatives to the subTurn to which this refers to.
     *
     * @return (the code>JPanel</code> relative to the subTurn to which this refers to).
     */
    public JPanel getSubTurnPanel(){
        return new ImagePanel(0.9, 0.9, 0.05, 0.05, "/img/background/subTurnPanel/noActionPanel.png");
    }

    /**
     * Method that returns the BoardSubTurn
     *
     * @return (the <code>getBoardSubTurn</code> that refers to the subTurn to which this refers to).
     */
    public BoardSubTurn getBoardSubTurn(){
        return new BoardSubTurn(this);
    }

    /**
     * Method retrieving the <code>Executer</code> entailed into the subTurn.
     *
     * @return (the <code>Executer</code> of the current subTurn).
     */
    public Executer getMyExecuter(){
        return viewSubTurn.getExecuter();
    }

    /**
     * Method that returns the name of the player of the current subTurn.
     *
     * @return (the name of the player of which this turn is).
     */
    public String getPlayer(){
        if(viewSubTurn==null)
            return null;
        return viewSubTurn.getPlayer();
    }

    /*public String getGodName(){
        ViewPlayer player = null;
        if (getPlayer()==null){
            return "";
        }
        try {
            player = ViewPlayer.searchByName(getPlayer());
        } catch (NotFoundException e) {
            return "";
        }
        try {
            return player.getCard().getName();
        } catch (NotFoundException e) {
            return "";
        }
    }*/

    /**
     * Method that is called on the loading of the subTurn.
     */
    public void onLoad(){  }
}
