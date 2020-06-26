package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;

import java.awt.*;

/**
 * Class extending BoardSubTurn and referring to the opponent's sub turns or for subturns with no action permitted on the board.
 *
 * @see BoardSubTurn
 */
public class ForbiddenBoardSubTurn extends BoardSubTurn {

    /**
     * Constructor
     *
     * @param guiSubTurnViewer (GUISubTurnViewer relative to the current sub turn).
     */
    public ForbiddenBoardSubTurn(GUISubTurnViewer guiSubTurnViewer) {
        super(guiSubTurnViewer);
    }

    /**
     * Method to be set the cursor image when the cursor enters the board.
     *
     * @return (the forbidden <code>Cursor</code>).
     */
    @Override
    public Cursor getOnEnterCursor(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource("/img/cursor/forbidden.gif"));
        Point hotspot = new Point(16,16);
        return toolkit.createCustomCursor(image, hotspot, "noAction");
    }

    /**
     * Method called when the mouse clicks on one cell of the board and does nothing.
     *
     * @param x (the x position of the board (referring to the <code>ViewBoard</code> representation)).
     * @param y (the y position of the board (referring to the <code>ViewBoard</code> representation)).
     */
    @Override
    public void onCellSelected(int x, int y){
        SoundEffect.playSound("/actions/forbidden.wav");
    }
}
