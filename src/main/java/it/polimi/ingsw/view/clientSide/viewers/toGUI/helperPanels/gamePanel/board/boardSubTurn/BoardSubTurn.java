package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import java.awt.*;


/**
 * Class that is responsible for the GUI representation of the board for the current SubTurn.
 * We here set either the new cursor image and the action to perform on the click of one point of the board.
 */
public class BoardSubTurn {

    protected GUISubTurnViewer guiSubTurnViewer;

    /**
     * Constructor
     *
     * @param guiSubTurnViewer ((GUISubTurnViewer) of the relative SubTurn -to retrive the <code>Executer</code>).
     */
    public BoardSubTurn(GUISubTurnViewer guiSubTurnViewer){
        this.guiSubTurnViewer = guiSubTurnViewer;
    }

    /**
     * Method to be set the cursor image when the cursor enters the board.
     *
     * @return (the correct <code>Cursor</code> image).
     */
    public Cursor getOnEnterCursor(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource("/img/cursor/forbidden.gif"));
        Point hotspot = new Point(16,16);
        return toolkit.createCustomCursor(image, hotspot, "noAction");
    }

    /**
     * Method to reset the Cursor when exiting the Board.
     *
     * @return (the default <code>Cursor</code> image).
     */
    public Cursor getOnExitCursor(){
        return new Cursor(Cursor.DEFAULT_CURSOR);
    }

    /**
     * Method called when the mouse clicks on one cell of the board (with the cell specified by coordinates in the parameters).
     *
     * @param x (the x position of the board (referring to the <code>ViewBoard</code> representation)).
     * @param y (the y position of the board (referring to the <code>ViewBoard</code> representation)).
     */
    public void onCellSelected(int x, int y){
        ViewBoard.getBoard().setSelectedCell(x, y);
        ViewBoard.getBoard().toGUI();
        Viewer.setAllRefresh();
    }

}
