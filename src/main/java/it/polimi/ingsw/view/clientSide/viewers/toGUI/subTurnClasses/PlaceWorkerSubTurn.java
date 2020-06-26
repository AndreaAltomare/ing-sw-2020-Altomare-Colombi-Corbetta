package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.PlaceWorkerBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.PlaceWorkerSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

/**
 * Class extending <code>GUISubTurnViewer</code> representing the PLACEWORKER <code>ViewSubTurn</code>.
 */
public class PlaceWorkerSubTurn extends GUISubTurnViewer {

    /**
     * constructor.
     *
     * @param parent (the <code>SubTurnViewer</code> to which this refers to).
     */
    public PlaceWorkerSubTurn(SubTurnViewer parent){
        super(parent);
    }

    /**
     * Method that returns the subTurnPanel relatives to the PLACEWORKER subTurn.
     *
     * @return (the <code>JPanel</code> relative to the PLACEWORKER subTurn).
     */
    @Override
    public JPanel getSubTurnPanel(){
        return new PlaceWorkerSubTurnPanel();
    }

    /**
     * Method that returns the BoardSubTurn referring to PLACEWORKER.
     *
     * @return (the <code>getBoardSubTurn</code> that refers to the PLACEWORKER subTurn).
     */
    @Override
    public BoardSubTurn getBoardSubTurn(){
        return new PlaceWorkerBoardSubTurn(this);
    }
}
