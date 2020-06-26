package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.ForbiddenBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.NoActionSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

/**
 * Class extending <code>GUISubTurnViewer</code> representing all the <code>ViewSubTurn</code> on which there is no action permitted.
 */
public class NoActionSubTurn extends GUISubTurnViewer {

    /**
     * constructor.
     *
     * @param parent (the <code>SubTurnViewer</code> to which this refers to).
     */
    public NoActionSubTurn(SubTurnViewer parent){
        super(parent);
    }

    /**
     * Method that returns the subTurnPanel relatives to all the <code>ViewSubTurn</code> on which there is no action permitted.
     *
     * @return (the <code>JPanel</code> relative to all the <code>ViewSubTurn</code> on which there is no action permitted).
     */
    @Override
    public JPanel getSubTurnPanel(){
        return new NoActionSubTurnPanel();
    }

    /**
     * Method that returns the BoardSubTurn referring to all the <code>ViewSubTurn</code> on which there is no action permitted.
     *
     * @return (the <code>getBoardSubTurn</code> that refers to all the <code>ViewSubTurn</code> on which there is no action permitted).
     */
    @Override
    public BoardSubTurn getBoardSubTurn(){
        return new ForbiddenBoardSubTurn(this);
    }
}
