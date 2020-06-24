package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SelectWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.awt.*;

/**
 * Class extending BoardSubTurn and referring to the worker selection sub turn of the current player.
 *
 * @see BoardSubTurn
 */
public class SelectWorkerBoardSubTurn extends BoardSubTurn {

    /**
     * Constructor
     *
     * @param guiSubTurnViewer (worker selection's GUISubTurnViewer).
     */
    public SelectWorkerBoardSubTurn(GUISubTurnViewer guiSubTurnViewer)  {
        super(guiSubTurnViewer);
    }

    /**
     * Method to be set the cursor image when the cursor enters the board.
     *
     * @return (the worker selection <code>Cursor</code>).
     */
    @Override
    public Cursor getOnEnterCursor(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource("/img/cursor/select.gif"));
        Point hotspot = new Point(16,16);
        return toolkit.createCustomCursor(image, hotspot, "selectWorker");
    }

    /**
     * Method called when the mouse clicks on one cell of the board on which there is the worker to be chosen, and does the action to choose it.
     *
     * @param x (the x position of the board (referring to the <code>ViewBoard</code> representation)).
     * @param y (the y position of the board (referring to the <code>ViewBoard</code> representation)).
     */
    @Override
    public void onCellSelected(int x, int y){
        super.onCellSelected(x, y);
        SelectWorkerExecuter myExecuter = (SelectWorkerExecuter)guiSubTurnViewer.getMyExecuter();
        try {
            myExecuter.setCell(x, y);
            myExecuter.doIt();
            SoundEffect.playSound("/actions/selectWorker.wav");
        } catch (WrongParametersException | CannotSendEventException e) {
            ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
        }

    }
}
