package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.ForbiddenBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.PlaceWorkerBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.NoActionSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.PlaceWorkerSubTurnPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

public class PlaceWorkerSubTurn extends GUISubTurnViewer {

    public PlaceWorkerSubTurn(SubTurnViewer parent){
        super(parent);
    }

    @Override
    public JPanel getSubTurnPanel(){
        if(!isMyTurn())
            return new NoActionSubTurnPanel();
        else
            return new PlaceWorkerSubTurnPanel();
    }

    @Override
    public BoardSubTurn getBoardSubTurn(){
        if(!isMyTurn())
            return new ForbiddenBoardSubTurn(this);
        else
            return new PlaceWorkerBoardSubTurn(this);
    }
}
