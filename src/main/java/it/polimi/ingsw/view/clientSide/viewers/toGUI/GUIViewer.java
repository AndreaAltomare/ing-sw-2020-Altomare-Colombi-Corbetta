package it.polimi.ingsw.view.clientSide.viewers.toGUI;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.specificGUISideClass.GUIMessageDisplayer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.specificGUISideClass.GUISelectCard;
import it.polimi.ingsw.view.exceptions.CheckQueueException;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class GUIViewer extends Viewer {

    JFrame window;
    GUIStatusViewer actualStatus;

    @Override
    public void refresh() {  }

    @Override
    protected void enqueue(ViewerQueuedEvent event){
        if(event.getType()== ViewerQueuedEvent.ViewerQueuedEventType.MESSAGE) {
            switch(((ViewMessage)event.getPayload()).getMessageType()){
                case FATAL_ERROR_MESSAGE:
                case EXECUTER_ERROR_MESSAGE:
                case FROM_SERVER_ERROR:
                case FROM_SERVER_MESSAGE:
                case WIN_MESSAGE:
                case LOOSE_MESSAGE:
                    GUIMessageDisplayer.displayErrorMessage(((ViewMessage)event.getPayload()).getPayload(), ((ViewMessage)event.getPayload()).getMessageType());
                    break;
            }
            if(View.debugging)
                System.out.println("[GUI MESSAGE]\t" + ((ViewMessage) event.getPayload()).getMessageType() + "\t" + ((ViewMessage) event.getPayload()).getPayload());
        } else {
            super.enqueue(event);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        executeQueuedEvent();
                    }catch (Exception e){
                        if(View.debugging){
                            System.err.println("In GUI Viewer Swinng thread");
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    private void executeQueuedEvent(){
        ViewerQueuedEvent queued;
        try {
            queued = getNextEvent();
        } catch (EmptyQueueException e) {
            return;
        }

        if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.EXIT) {
            window.setVisible(false);
            exit();
        }
        if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS) setStatus(queued);
        if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.CARDSELECTION)
            if (ViewStatus.getActual().equals(ViewStatus.GAME_PREPARATION)){
                GUISelectCard.attivate((CardSelection) queued.getPayload());
            }
        if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.REFRESH) {
            if(ViewBoard.getBoard() != null)
                ViewBoard.getBoard().toGUI();
            SwingUtilities.updateComponentTreeUI(window);
        }
        if(queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN){
            if(((SubTurnViewer)queued.getPayload()).toGUI()!=null)
                actualStatus.setSubTurn(((SubTurnViewer)queued.getPayload()).toGUI());
        }
    }


    public GUIViewer(){
        Viewer.registerViewer(this);
    }

    public void setJPanel(JPanel panel){
        if(panel == null){
            window.setVisible(false);
        }else{
            window.getContentPane().removeAll();
            window.setContentPane(panel);
            window.setVisible(true);
            window.repaint();
        }
    }

    public JFrame getWindow(){ return window; }

    private void setStatus(ViewerQueuedEvent queued){
        if(actualStatus != null)
            actualStatus.onClose();
        actualStatus = ((StatusViewer)queued.getPayload()).toGUI();

        if(actualStatus == null){
            //window.setVisible(false);
            return;
        }
        actualStatus.setMyGUIViewer(this);

        try {
            actualStatus.execute();
            if(actualStatus.setFrameTitle()){
                window.setTitle(actualStatus.getTitle());
            }
        } catch (CheckQueueException ignore) {}
    }

    @Override
    public void run() {
        init();
        /*bodyExecute();
        end();*/
    }

    private void bodyExecute(){
        ViewerQueuedEvent queued;
        while(true){
            waitNextEvent();
            executeQueuedEvent();
        }
    }

    private void end(){
        window.setVisible(false);
    }

    private void init(){
        window = new JFrame("SANTORINI");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { Viewer.exitAll(); }
        });
        window.setSize(1024, 512);
        try {
            window.setIconImage(ImageIO.read(getClass().getResource("/img/icon/title_islan.png")));
        } catch (IOException ignore) {  }
    }

    @Override
    public void waitNextEvent(){
        //Non usarla nella GUI!!
        throw new Error();
    }
    @Override
    public void waitNextEventType(ViewerQueuedEvent.ViewerQueuedEventType eventType){
        //Non usarla nella GUI!!
        throw new Error();
    }

    @Override
    public void waitTimeOut(long timeOut) throws CheckQueueException{
        //Non usarla nella GUI!!
        throw new Error();

    }
}
