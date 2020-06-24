package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.BodyPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.ChatPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;

/**
 * Class to represent the panel for the PLAYING subTurn.
 * It contains the <code>TitlePanel</code>, the <code>BoardGeneralPanel</code> the <code>SubTurnPanel</code> and the <code>chatPanel</code>.
 */
public class GamePanel extends BackgroundPanel {
    private static GamePanel last;

    private BodyPanel bodyPanel;

    /**
     * constructor.
     */
    public GamePanel(){
        super("/img/background/sized_waiting_background.png");
        new TitlePanel(this);
        bodyPanel = new BodyPanel();
        add(bodyPanel);

        JPanel playerPanel = bodyPanel.getPlayerPanel();
        ChatPanel chatPanel = new ChatPanel();
        playerPanel.add(chatPanel);


        last = this;
    }

    /**
     * Method to be called to set the <code>GUISubTurnViewer</code> of the current SubTurn.
     * It updates the <code>SubTurnPanel</code> and the <code>subTurnPanel</code>.
     *
     * @param subTurnViewer (the GUISubTurnViewer to be set).
     */
    public void setSubTurn(GUISubTurnViewer subTurnViewer){
        JPanel subTurnContainer = bodyPanel.getSubTurnPanel();
        subTurnContainer.removeAll();
        subTurnContainer.add(subTurnViewer.getSubTurnPanel());

        ViewBoard.getBoard().toGUI().setMySubTurn(subTurnViewer.getBoardSubTurn());
        ViewBoard.getBoard().toGUI().setVisible(true);

        subTurnViewer.onLoad();

        Viewer.setAllRefresh();


    }

    /**
     * static method that returns the last GamePanel instanced.
     *
     * @return (the last GamePanel instanced).
     */
    public static GamePanel getLast(){
        return last;
    }
}
