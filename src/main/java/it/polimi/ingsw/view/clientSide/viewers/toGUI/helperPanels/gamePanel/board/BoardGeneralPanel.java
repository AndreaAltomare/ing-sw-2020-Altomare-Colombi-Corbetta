package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCell;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class intended to represent the Board and give all the functionality needed to represent it to the GUI.
 */
public class BoardGeneralPanel extends ImagePanel {

    private SubPanel lowerPanel;
    private SubPanel upperPanel;

    private int xDim;
    private int yDim;

    private double xLen;
    private double yLen;

    private BoardSubTurn mySubTurn;

    private ViewCell selectedCell;
    private ImagePanel selectPanel = new ImagePanel(1, 1, 0, 0, "/img/board/cells/selectedCell.png");

    private ViewCell selectedWorker;
    private ImagePanel selectedWorkerPanel = new ImagePanel(1, 1, 0, 0, "/img/board/cells/selectedWorker.png");

    /**
     * Class to represent the status of the buildings on the board.
     */
    private static class MyBuildingRepresentation{

        /**
         * Matrix of SubPanel containing all the SubPanels representing the various Cell.
         */
        private SubPanel[][] cellMatrix;

        /**
         * Matrix of String containing the String representation of each cell.
         */
        private String[][] cellStatusMatrix;


        private BoardGeneralPanel parent;

        /**
         * constructor.
         *
         * @param parent (The BoardGeneralPanel that entails this).
         */
        MyBuildingRepresentation(BoardGeneralPanel parent){
            this.parent = parent;
            cellMatrix = new SubPanel[parent.xDim][parent.yDim];
            cellStatusMatrix = new String[parent.xDim][parent.yDim];
        }

        /**
         * Method to retrieve the String representation of the status that is being representing of the Cell in a given position.
         *
         * @param x (the x position of the Cell).
         * @param y (the y position of the Cell).
         * @return  (The String representation of the status of the Cell that is being actually showing).
         */
        String getStatusAt(int x, int y){
            return cellStatusMatrix[x][y];
        }

        /**
         * Method to retrieve the String representation of the status that is being representing of the Cell.
         *
         * @param cell  (the <code>ViewCell</code> in the position of the Cell which is looked for).
         * @return      (The String representation of the status of the Cell that is being actually showing).
         */
        String getStatusAt(ViewCell cell){
            return getStatusAt(cell.getX(), cell.getY());
        }

        /**
         * Method that sets the String representation of the status that is being representing of the Cell in a given position.
         *
         * @param x (the x position of the Cell).
         * @param y (the y position of the Cell).
         * @param status    (The String representation of the status of the Cell that is being actually showing).
         */
        void setStatusAt(int x, int y, String status){
            cellStatusMatrix[x][y] = status;
        }

        /**
         * Method to set the String representation of the status that is being representing of the Cell.
         * It retrieves from the <code>ViewCell</code> either the position and the String representing the actual status.
         *
         * @param cell  (The <code>ViewCell</code> that is being going to represent).
         */
        void setStatusAt(ViewCell cell){
            setStatusAt(
                    cell.getX(),
                    cell.getY(),
                    getCellStatusEncoded(cell)
            );
        }

        /**
         * Method that builds the String representation of a Cell within its status.
         *
         * @param cell  (The <code>ViewCell</code> that is being represented.
         * @return      (String representing the <code>ViewCell</code> and it's status.
         */
        String getCellStatusEncoded(ViewCell cell){
            return "l"+cell.getLevel()+"D"+(cell.isDoomed()?"t":"f") + (cell.isThereWorker()?("W" + cell.getWorkerString()):"");
        }

        /**
         * Method to retrieve th Panel representing the Cell in the given position.
         *
         * @param x (the x position of the Cell).
         * @param y (the y position of the Cell).
         * @return  (the SubPanel representing the searched Cell).
         */
        SubPanel getPanelAt(int x, int y){
            return cellMatrix[x][y];
        }

        /**
         * Method to retrieve th Panel representing the <code>ViewCell</code>.
         *
         * @param cell  (the searched <code>ViewCell</code>).
         * @return      (the SubPanel representing it).
         */
        SubPanel getPanelAt(ViewCell cell){
            return getPanelAt(cell.getX(), cell.getY());
        }

        /**
         * Method to set the SubPanel representing the Cell in the given position.
         *
         * @param x (the x position of the Cell).
         * @param y (the y position of the Cell).
         * @param panel (the <code>SubPanel</code> representing the selected Cell).
         */
        void setPanelAt(int x, int y, SubPanel panel){
            cellMatrix[x][y] = panel;
        }

        /**
         * Method to set the SubPanel representing the given <code>ViewCell</code>.
         * It retrieves the SubPanel from the method <code>ViewCell.toGUI</code>.
         *
         * @param cell  (the <code>ViewCell</code> to be represented).
         */
        void setPanelAt(ViewCell cell){
            setPanelAt(cell.getX(), cell.getY(), cell.toGUI());
        }

        /**
         * Method that sets the representation of the given Cell (either the String and SubPaneel representation).
         *
         * @param cell  (the <code>ViewCell</code> to be represented
         */
        void setCell(ViewCell cell){
            setPanelAt(cell);
            setStatusAt(cell);
        }

        /**
         * Method that checks weather the actual representation of the gven <code>ViewCell</code> is updated.
         * It checks the String representation.
         *
         * @param cell (the <code>ViewCell</code> that is being checked)
         * @return     (true iif the representation of the Cell is different from the Cell given).
         */
        boolean isDifferent(ViewCell cell){
            return !getCellStatusEncoded(cell).equals(getStatusAt(cell));
        }

        /**
         * Method that grants the representation of the Cell is updated.
         * It checks weather the representation has to be changed and if so updates the representation.
         *
         * @param cell  (<code>ViewCell</code> that has to be updated).
         */
        void updateCell(ViewCell cell){
            if (isDifferent(cell)){
                try {
                    parent.remove(getPanelAt(cell));
                }catch(NullPointerException ignore){}
                setCell(cell);
                try {
                    parent.addComponentToCell(cell, getPanelAt(cell));
                }catch(NullPointerException ignore){}
            }
        }

    }

    private MyBuildingRepresentation myBuildingRepresentation;

    /**
     * Method to update the status of a cell
     *
     * @param cell (<code>ViewCell</code> to be updated)
     */
    public void updateCell(ViewCell cell){
        myBuildingRepresentation.updateCell(cell);
    }


    /**
     * Method to add a <code>SubPanel</code> to the selected Cell.
     *
     * @param x (int the x pos of the cell)
     * @param y (int the y pos of the cell)
     * @param panel (the <code>SubPanel</code> to be added)
     */
    private void addComponentToCell(int x, int y, SubPanel panel){
        double myXLen = xLen;
        if(panel == null) return;
        if(x<3){
            upperPanel.add(panel);
            myXLen = myXLen * 5 / 3;
        }else{
            lowerPanel.add(panel);
            x -= 3;
            myXLen = myXLen * 5/2;
        }

        //Parametri invertiti perchè la rappresentazione in Swing differisce dalla rappresentazine nella logica del gioco
        //Parametrs swithched because Swing's representation is different from the game's logic representation, where x is the row and  y the column
        panel.setMyRapp(yLen, myXLen, y*yLen, x*myXLen);
    }

    /**
     * Method to add a <code>SubPanel</code> to the selected Cell.
     *
     * @param cell (ViewCell to which the component has to be added)
     * @param panel (SubPanel to be added)
     */
    private void addComponentToCell(ViewCell cell, SubPanel panel){
        addComponentToCell(cell.getX(), cell.getY(), panel);
    }


    /**
     * constructor.
     *
     * @param fileName (String the name of the board image file)
     */
    private BoardGeneralPanel(String fileName) {
        super(1, 1, 0, 0, fileName);

        mySubTurn = new BoardSubTurn(null);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int y=mouseEvent.getX();
                int x=mouseEvent.getY();
                mySubTurn.onCellSelected((int)(x/(getSize().getHeight()*yLen)), (int)(y/(getSize().getWidth()*xLen )));
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {  }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {  }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                setCursor(mySubTurn.getOnEnterCursor());
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                setCursor(mySubTurn.getOnExitCursor());
            }
        });

        //lowerPanel = new SubPanel(1, 2*xLen, 0, 3*xLen);
        lowerPanel = new SubPanel(1, .4, 0, .6);
        lowerPanel.setOpaque(false);
        add(lowerPanel);

        //upperPanel = new SubPanel(1, 3*xLen, 0, 0);
        upperPanel = new SubPanel(1, .6, 0, 0);
        upperPanel.setOpaque(false);
        add(upperPanel);

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

    /**
     * Method that set the Cell that is to be represented like selected.
     *
     * @param cell  (<code>ViewCell</code> selected).
     */
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
            if(back == null){
                addComponentToCell(selectedCell, selectPanel);
            }

            else{
                selectPanel.setMyRapp(1,1,0,0);
                back.add(selectPanel);
            }
        }

    }

    /**
     * Method that set the Worker that is to be represented like selected.
     *
     * @param cell  (<code>ViewCell</code> on which is the selected Worker).
     */
    public void setSelectedWorker(ViewCell cell){
        if(selectedWorker!=null){
            JPanel back = myBuildingRepresentation.getPanelAt(selectedWorker);
            if(back == null)
                remove(selectedWorkerPanel);
            else
                back.remove(selectedWorkerPanel);
        }

        selectedWorker = cell;
        if(selectedWorker!=null){
            JPanel back = myBuildingRepresentation.getPanelAt(cell);
            if(back == null){
                addComponentToCell(selectedWorker, selectedWorkerPanel);
            }

            else{
                selectedWorkerPanel.setMyRapp(1,1,0,0);
                back.add(selectedWorkerPanel);
            }
        }

    }

    /**
     * Method that set the <code>BoardSubTurn</code> needed in this moment
     *
     * @param boardSubTurn  (<code>BoardSubTurn</code> that represents the actual SubTurn).
     */
    public void setMySubTurn(BoardSubTurn boardSubTurn){
        mySubTurn = boardSubTurn;
    }

    /*@Override
    public Component add(Component c){
        System.out.println(c);
        if(c==null) return null;
        Component ret = super.add(c);
        c.setVisible(true);
        return ret;
    }*/

    //Needed for this' internal function, but not differs from the overridden method
    @Override
    public void remove(Component c){
        if(c==null) return;
        super.remove(c);
        lowerPanel.remove(c);
        upperPanel.remove(c);
    }


}
