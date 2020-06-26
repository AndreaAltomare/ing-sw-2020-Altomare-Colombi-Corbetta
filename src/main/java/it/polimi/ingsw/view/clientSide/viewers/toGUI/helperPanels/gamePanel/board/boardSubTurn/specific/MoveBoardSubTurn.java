package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.MoveWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.awt.*;

/**
 * Class extending BoardSubTurn and referring to the moving sub turn of the current player.
 *
 * @see BoardSubTurn
 */
public class MoveBoardSubTurn extends BoardSubTurn {

    /**
     * Constructor
     *
     * @param guiSubTurnViewer (movement's GUISubTurnViewer).
     */
    public MoveBoardSubTurn(GUISubTurnViewer guiSubTurnViewer)  {
        super(guiSubTurnViewer);
    }

    /**
     * Method to be set the cursor image when the cursor enters the board.
     *
     * @return (the movement <code>Cursor</code>).
     */
    @Override
    public Cursor getOnEnterCursor(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource("/img/cursor/move.gif"));
        Point hotspot = new Point(16,16);
        return toolkit.createCustomCursor(image, hotspot, "moveWorker");
    }

    /**
     * Method called when the mouse clicks on one cell of the board on which it the worker should move, and does the action to move it.
     *
     * @param x (the x position of the board (referring to the <code>ViewBoard</code> representation)).
     * @param y (the y position of the board (referring to the <code>ViewBoard</code> representation)).
     */
    @Override
    public void onCellSelected(int x, int y){
        super.onCellSelected(x, y);
        MoveWorkerExecuter myExecuter = (MoveWorkerExecuter) guiSubTurnViewer.getMyExecuter();
        try {
            myExecuter.setCell(x, y);
            myExecuter.doIt();
            SoundEffect.playSound("/actions/move.wav");
        } catch (WrongParametersException | CannotSendEventException e) {
            ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
        }

    }
}
