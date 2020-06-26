package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.ForbiddenBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.OpponentMoveSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

/**
 * Class extending <code>GUISubTurnViewer</code> representing the OPPONENT_MOVE <code>ViewSubTurn</code>.
 */
public class OpponentMoveSubTurn extends GUISubTurnViewer {

    /**
     * constructor.
     *
     * @param parent (the <code>SubTurnViewer</code> to which this refers to).
     */
    public OpponentMoveSubTurn(SubTurnViewer parent) {
        super(parent);
    }

    /**
     * Method that returns the subTurnPanel relatives to the OPPONENT_MOVE subTurn.
     *
     * @return (the <code>JPanel</code> relative to the OPPONENT_MOVE subTurn).
     */
    @Override
    public JPanel getSubTurnPanel() {
        return new OpponentMoveSubTurnPanel(parent.getMySubTurn().getPlayer());
    }

    /**
     * Method that returns the BoardSubTurn referring to OPPONENT_MOVE.
     *
     * @return (the <code>getBoardSubTurn</code> that refers to the OPPONENT_MOVE subTurn).
     */
    @Override
    public BoardSubTurn getBoardSubTurn() {
        return new ForbiddenBoardSubTurn(this);
    }
}
