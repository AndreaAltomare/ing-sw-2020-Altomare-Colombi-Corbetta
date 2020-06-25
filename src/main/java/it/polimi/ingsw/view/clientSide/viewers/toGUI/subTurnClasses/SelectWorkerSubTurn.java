package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.SelectWorkerBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.SelectWorkerSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

/**
 * Class extending <code>GUISubTurnViewer</code> representing the SELECTWORKER <code>ViewSubTurn</code>.
 */
public class SelectWorkerSubTurn extends GUISubTurnViewer {

    /**
     * constructor.
     *
     * @param parent (the <code>SubTurnViewer</code> to which this refers to).
     */
    public SelectWorkerSubTurn(SubTurnViewer parent) {
        super(parent);
    }

    /**
     * Method that returns the subTurnPanel relatives to the SELECTWORKER subTurn.
     *
     * @return (the <code>JPanel</code> relative to the SELECTWORKER subTurn).
     */
    @Override
    public JPanel getSubTurnPanel() {
        return new SelectWorkerSubTurnPanel();
    }

    /**
     * Method that returns the BoardSubTurn referring to SELECTWORKER.
     *
     * @return (the <code>getBoardSubTurn</code> that refers to the SELECTWORKER subTurn).
     */
    @Override
    public BoardSubTurn getBoardSubTurn() {
        return new SelectWorkerBoardSubTurn(this);
    }
}
