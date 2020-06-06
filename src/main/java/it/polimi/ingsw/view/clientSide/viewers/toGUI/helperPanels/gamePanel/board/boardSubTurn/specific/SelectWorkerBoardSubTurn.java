package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SelectWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.awt.*;

public class SelectWorkerBoardSubTurn extends BoardSubTurn {

    public SelectWorkerBoardSubTurn(GUISubTurnViewer guiSubTurnViewer)  {
        super(guiSubTurnViewer);
    }

    @Override
    public Cursor getOnEnterCursor(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource("/img/cursor/select.gif"));
        Point hotspot = new Point(16,16);
        return toolkit.createCustomCursor(image, hotspot, "selectWorker");
    }

    @Override
    public void onCellSelected(int x, int y){
        super.onCellSelected(x, y);
        SelectWorkerExecuter myExecuter = (SelectWorkerExecuter)guiSubTurnViewer.getMyExecuter();
        try {
            myExecuter.setCell(x, y);
            myExecuter.doIt();
            SoundEffect.playSound("/actions/Decapitation-SoundBible.com-800292304.wav");
        } catch (WrongParametersException | CannotSendEventException e) {
            ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
        }

    }
}
