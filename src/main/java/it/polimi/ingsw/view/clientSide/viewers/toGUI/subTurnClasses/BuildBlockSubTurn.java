package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.BuildBlockBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.BuildBlockSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

/**
 * Class extending <code>GUISubTurnViewer</code> representing the BUILD_BLOCK <code>ViewSubTurn</code>.
 */
public class BuildBlockSubTurn extends GUISubTurnViewer {

    /**
     * constructor.
     *
     * @param parent (the <code>SubTurnViewer</code> to which this refers to).
     */
    public BuildBlockSubTurn(SubTurnViewer parent) {
        super(parent);
    }

    /**
     * Method that returns the subTurnPanel relatives to the BUILD_BLOCK subTurn.
     *
     * @return (the <code>JPanel</code> relative to the BUILD_BLOCK subTurn).
     */
    @Override
    public JPanel getSubTurnPanel() {
        return new BuildBlockSubTurnPanel();
    }

    /**
     * Method that returns the BoardSubTurn referring to BUILD_BLOCK.
     *
     * @return (the <code>getBoardSubTurn</code> that refers to the BUILD_BLOCK subTurn).
     */
    @Override
    public BoardSubTurn getBoardSubTurn() {

        return new BuildBlockBoardSubTurn(this);
    }
}

