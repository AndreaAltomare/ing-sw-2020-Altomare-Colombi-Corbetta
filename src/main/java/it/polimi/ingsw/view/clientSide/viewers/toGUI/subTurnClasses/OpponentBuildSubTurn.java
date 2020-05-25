package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.ForbiddenBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.OpponentBuildSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

public class OpponentBuildSubTurn extends GUISubTurnViewer {
    public OpponentBuildSubTurn(SubTurnViewer parent) {
        super(parent);
    }

    @Override
    public JPanel getSubTurnPanel() {
        return new OpponentBuildSubTurnPanel(parent.getMySubTurn().getPlayer());
    }

    @Override
    public BoardSubTurn getBoardSubTurn() {
        return new ForbiddenBoardSubTurn(this);
    }
}

