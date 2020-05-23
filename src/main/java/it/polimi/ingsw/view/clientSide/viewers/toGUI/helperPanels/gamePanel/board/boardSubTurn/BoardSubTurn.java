package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;

import java.awt.*;

public class BoardSubTurn {

    public Cursor getOnEnterCursor(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource("/img/cursor/build_curso.gif"));
        Point hotspot = new Point(16,16);
        return toolkit.createCustomCursor(image, hotspot, "noAction");
    }

    public Cursor getOnExitCursor(){
        return new Cursor(Cursor.DEFAULT_CURSOR);
    }

    public void onCellSelected(int x, int y){
        ViewBoard.getBoard().setSelectedCell(x, y);
        ViewBoard.getBoard().toGUI();
        Viewer.setAllRefresh();
    }

}
