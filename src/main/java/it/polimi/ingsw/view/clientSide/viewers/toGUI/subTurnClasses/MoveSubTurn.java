package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.MoveBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.MoveSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

/**
 * Class extending <code>GUISubTurnViewer</code> representing the MOVE <code>ViewSubTurn</code>.
 */
public class MoveSubTurn extends GUISubTurnViewer {

    /**
     * constructor.
     *
     * @param parent (the <code>SubTurnViewer</code> to which this refers to).
     */
    public MoveSubTurn(SubTurnViewer parent) {
        super(parent);
    }

    /**
     * Method that returns the subTurnPanel relatives to the MOVE subTurn.
     *
     * @return (the <code>JPanel</code> relative to the MOVE subTurn).
     */
    @Override
    public JPanel getSubTurnPanel() {
        return new MoveSubTurnPanel();
    }

    /**
     * Method that returns the BoardSubTurn referring to MOVE.
     *
     * @return (the <code>getBoardSubTurn</code> that refers to the MOVE subTurn).
     */
    @Override
    public BoardSubTurn getBoardSubTurn() {
        return new MoveBoardSubTurn(this);
    }
}
