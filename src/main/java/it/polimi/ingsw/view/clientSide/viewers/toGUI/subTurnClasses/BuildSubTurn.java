package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.BuildBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.BuildSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

/**
 * Class extending <code>GUISubTurnViewer</code> representing the BUILD <code>ViewSubTurn</code>.
 */
public class BuildSubTurn extends GUISubTurnViewer {

    /**
     * constructor.
     *
     * @param parent (the <code>SubTurnViewer</code> to which this refers to).
     */
    public BuildSubTurn(SubTurnViewer parent) {
        super(parent);
    }

    /**
     * Method that returns the subTurnPanel relatives to the BUILD subTurn.
     *
     * @return (the <code>JPanel</code> relative to the BUILD subTurn).
     */
    @Override
    public JPanel getSubTurnPanel() {
        return new BuildSubTurnPanel();
    }

    /**
     * Method that returns the BoardSubTurn referring to BUILD.
     *
     * @return (the <code>getBoardSubTurn</code> that refers to the BUILD subTurn).
     */
    @Override
    public BoardSubTurn getBoardSubTurn(){

        return new BuildBoardSubTurn(this);
    }
}
