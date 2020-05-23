package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;

import java.awt.*;

public class ForbiddenBoardSubTurn extends BoardSubTurn {

    @Override
    public Cursor getOnEnterCursor(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource("/img/cursor/forbidden.gif"));
        Point hotspot = new Point(16,16);
        return toolkit.createCustomCursor(image, hotspot, "noAction");
    }

    @Override
    public void onCellSelected(int x, int y){
        ;
    }
}
