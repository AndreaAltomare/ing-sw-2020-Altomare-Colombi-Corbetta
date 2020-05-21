package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCell;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import javax.swing.*;
import java.awt.*;

/**
 * Class intended to represent the Board and give all the functionalities needed to represent it to the GUI.
 *
 * @author giorgio
 */
public class BoardGeneralPanel extends ImagePanel {

    private int xDim;
    private int yDim;

    private double xLen;
    private double yLen;


    private ViewCell selectedCell;
    private ImagePanel selectPanel = new ImagePanel(1, 1, 0, 0, "/img/board/cells/selectedCell.png");

    /**
     * Class to represent the status of the buildings on the board
     */
    private static class MyBuildingRepresentation{

        private SubPanel[][] cellMatrix;
        private String[][] cellStatusMatrix;
        private BoardGeneralPanel parent;

        MyBuildingRepresentation(BoardGeneralPanel parent){
            this.parent = parent;
            cellMatrix = new SubPanel[parent.xDim][parent.yDim];
            cellStatusMatrix = new String[parent.xDim][parent.yDim];
        }

        String getStatusAt(int x, int y){
            return cellStatusMatrix[x][y];
        }

        String getStatusAt(ViewCell cell){
            return getStatusAt(cell.getX(), cell.getY());
        }

        void setStatusAt(int x, int y, String status){
            cellStatusMatrix[x][y] = status;
        }

        void setStatusAt(ViewCell cell){
            setStatusAt(
                    cell.getX(),
                    cell.getY(),
                    getCellStatusEncoded(cell)
            );
        }

        String getCellStatusEncoded(ViewCell cell){
            return "l"+cell.getLevel()+"D"+(cell.isDoomed()?"t":"f");
        }

        SubPanel getPanelAt(int x, int y){
            return cellMatrix[x][y];
        }

        SubPanel getPanelAt(ViewCell cell){
            return getPanelAt(cell.getX(), cell.getY());
        }

        void setPanelAt(int x, int y, SubPanel panel){
            cellMatrix[x][y] = panel;
        }

        void setPanelAt(ViewCell cell){
            setPanelAt(cell.getX(), cell.getY(), cell.toGUI());
        }

        void setCell(ViewCell cell){
            setPanelAt(cell);
            setStatusAt(cell);
        }

        boolean isDifferent(ViewCell cell){
            return !getCellStatusEncoded(cell).equals(getStatusAt(cell));
        }

        void updateCell(ViewCell cell){
            if (isDifferent(cell)){
                System.out.println(cell.toString() + "Is different");
                System.out.println(getStatusAt(cell));
                parent.remove(getPanelAt(cell));
                setCell(cell);
                parent.addComponentToCell(cell, getPanelAt(cell));
                System.out.println(getStatusAt(cell));
            }
        }

    }

    private MyBuildingRepresentation myBuildingRepresentation;

    /**
     * Method to update the status of a cell
     *
     * @param cell (ViewCell to be updated)
     */
    public void updateCell(ViewCell cell){
        myBuildingRepresentation.updateCell(cell);
    }


    /**
     * Method to add a component to the selected Cell.
     *
     * @param x (int the x pos of the cell)
     * @param y (int the y pos of the cell)
     * @param panel (the SubPanel to add)
     */
    private void addComponentToCell(int x, int y, SubPanel panel){
        if(panel == null) return;
        add(panel);
        panel.setMyRapp(xLen, yLen, x*xLen, y*yLen);
    }

    /**
     * Method to add a component to the selected Cell.
     *
     * @param cell (ViewCell to which the component has to be added)
     * @param panel (SubPanel to be added)
     */
    private void addComponentToCell(ViewCell cell, SubPanel panel){
        addComponentToCell(cell.getX(), cell.getY(), panel);
    }


    /**
     * constructor
     *
     * @param fileName (String the name of the board image file)
     */
    private BoardGeneralPanel(String fileName) {
        super(1, 1, 0, 0, fileName);
    }

    /**
     * Method to set the x and y dimension of the board.
     *
     * @param x (the x dimension of the board)
     * @param y (the y dimension of the board)
     */
    private void setXY(int x, int y){
        xDim = x;
        yDim = y;

        xLen = ((double)1)/xDim;
        yLen = ((double)1)/yDim;
    }

    /**
     * Factory method.
     *
     * @param xDim (the x dimension of the board)
     * @param yDim (the y dimension of the board)
     * @return (the new panel created)
     * @throws WrongParametersException (iif the dimensions are incorrect)
     */
    public static BoardGeneralPanel buildBoard(int xDim, int yDim) throws WrongParametersException {
        if (xDim!=5 || yDim != 5)
            throw new WrongParametersException("Wrong board size!!");
        BoardGeneralPanel ret = new BoardGeneralPanel("/img/board/boardScalata.png");
        ret.setXY(xDim, yDim);
        ret.myBuildingRepresentation = new MyBuildingRepresentation(ret);
        return ret;
    }

    /*public void setSelectCell(int x, int y){
        if(selectedCell!= null){
            remove(selectPanel);
        }
        try {
            selectedCell = ViewBoard.getBoard().getCellAt(x, y);
            addComponentToCell(selectedCell, selectPanel);
        } catch (NotFoundException e) {
            selectedCell = null;
        }
    }*/

    public void setSelectCell(ViewCell cell){
        if(selectedCell!=null){
            JPanel back = myBuildingRepresentation.getPanelAt(selectedCell);
            if(back == null)
                remove(selectPanel);
            else
                back.remove(selectPanel);
        }

        selectedCell = cell;
        if(selectedCell!=null){
            JPanel back = myBuildingRepresentation.getPanelAt(cell);
            if(back == null)
                addComponentToCell(selectedCell, selectPanel);
            else
                back.add(selectPanel);
        }

    }

    @Override
    public Component add(Component c){
        if(c==null) return null;
        return super.add(c);
    }

    @Override
    public void remove(Component c){
        if(c==null) return;
        super.remove(c);
    }


}
