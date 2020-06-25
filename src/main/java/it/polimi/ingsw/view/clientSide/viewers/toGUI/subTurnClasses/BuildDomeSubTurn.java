package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.BuildDomeBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.BuildDomeSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

/**
 * Class extending <code>GUISubTurnViewer</code> representing the BUILD_DOME <code>ViewSubTurn</code>.
 */
public class BuildDomeSubTurn extends GUISubTurnViewer {

    /**
     * constructor.
     *
     * @param parent (the <code>SubTurnViewer</code> to which this refers to).
     */
    public BuildDomeSubTurn(SubTurnViewer parent) {
        super(parent);
    }

    /**
     * Method that returns the subTurnPanel relatives to the BUILD_DOME subTurn.
     *
     * @return (the <code>JPanel</code> relative to the BUILD_DOME subTurn).
     */
    @Override
    public JPanel getSubTurnPanel() {
        return new BuildDomeSubTurnPanel();
    }

    /**
     * Method that returns the BoardSubTurn referring to BUILD_DOME.
     *
     * @return (the <code>getBoardSubTurn</code> that refers to the BUILD_DOME subTurn).
     */
    @Override
    public BoardSubTurn getBoardSubTurn(){

        return new BuildDomeBoardSubTurn(this);
    }
}
