package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.PlaceWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.awt.*;

/**
 * Class extending BoardSubTurn and referring to the worker placing sub turn of the current player.
 *
 * @see BoardSubTurn
 */
public class PlaceWorkerBoardSubTurn extends BoardSubTurn {

    /**
     * Constructor
     *
     * @param guiSubTurnViewer (worker placement's GUISubTurnViewer).
     */
    public PlaceWorkerBoardSubTurn(GUISubTurnViewer guiSubTurnViewer)  {
        super(guiSubTurnViewer);
    }

    /**
     * Method to be set the cursor image when the cursor enters the board.
     *
     * @return (the worker placement <code>Cursor</code>).
     */
    @Override
    public Cursor getOnEnterCursor(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource("/img/cursor/place.gif"));
        Point hotspot = new Point(16,16);
        return toolkit.createCustomCursor(image, hotspot, "placeWorker");
    }

    /**
     * Method called when the mouse clicks on one cell of the board on which it should be placed a worker, and does the action to place it.
     *
     * @param x (the x position of the board (referring to the <code>ViewBoard</code> representation)).
     * @param y (the y position of the board (referring to the <code>ViewBoard</code> representation)).
     */
    @Override
    public void onCellSelected(int x, int y){
        super.onCellSelected(x, y);
        PlaceWorkerExecuter myExecuter = (PlaceWorkerExecuter)guiSubTurnViewer.getMyExecuter();
        try {
            myExecuter.setCell(x, y);
            myExecuter.doIt();
            SoundEffect.playSound("/actions/placeWorker.wav");
        } catch (WrongParametersException | CannotSendEventException e) {
            ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
        }

    }
}
