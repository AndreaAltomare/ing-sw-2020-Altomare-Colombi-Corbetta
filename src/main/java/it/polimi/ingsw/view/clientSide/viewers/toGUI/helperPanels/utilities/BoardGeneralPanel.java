package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCell;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.awt.*;

public class BoardGeneralPanel extends ImagePanel {

    private int xDim;
    private int yDim;

    private double xLen;
    private double yLen;

    private static class MyBuildingRepresentation{

        private SubPanel[][] cellMatrix;
        private String[][] cellStatusMatrix;
        BoardGeneralPanel parent;

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
                parent.addComponent(cell.getX(), cell.getY(), getPanelAt(cell));
                System.out.println(getStatusAt(cell));
            }
        }

    }

    private MyBuildingRepresentation myBuildingRepresentation;

    public void updateCell(ViewCell cell){
        myBuildingRepresentation.updateCell(cell);
    }

    public void addComponent(int x, int y, SubPanel panel){
        if(panel == null) return;
        add(panel);
        panel.setMyRapp(xLen, yLen, x*xLen, y*yLen);
    }


    private BoardGeneralPanel(String fileName) {
        super(1, 1, 0, 0, fileName);
    }

    private void setXY(int x, int y){
        xDim = x;
        yDim = y;

        xLen = ((double)1)/xDim;
        yLen = ((double)1)/yDim;
    }

    public static BoardGeneralPanel buildBoard(int xDim, int yDim) throws WrongParametersException {
        if (xDim!=5 || yDim != 5)
            throw new WrongParametersException("Wrong board size!!");
        BoardGeneralPanel ret = new BoardGeneralPanel("/img/board/boardScalata.png");
        ret.setXY(xDim, yDim);
        ret.myBuildingRepresentation = new MyBuildingRepresentation(ret);
        return ret;
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
