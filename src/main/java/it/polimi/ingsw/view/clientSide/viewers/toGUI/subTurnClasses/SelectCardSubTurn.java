package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.ForbiddenBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.SelectCardSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

public class SelectCardSubTurn extends GUISubTurnViewer {

    public SelectCardSubTurn(SubTurnViewer parent){
        super(parent);
    }

    @Override
    public JPanel getSubTurnPanel(){
        return new SelectCardSubTurnPanel();
    }

    @Override
    public BoardSubTurn getBoardSubTurn(){
        return new ForbiddenBoardSubTurn();
    }
}
