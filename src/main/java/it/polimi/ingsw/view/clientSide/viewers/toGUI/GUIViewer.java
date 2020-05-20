package it.polimi.ingsw.view.clientSide.viewers.toGUI;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;
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
                    displayErrorMessage(((ViewMessage)event.getPayload()).getPayload(), ((ViewMessage)event.getPayload()).getMessageType());
                    break;
            }
            System.out.println("[GUI MESSAGE]\t" + ((ViewMessage) event.getPayload()).getMessageType() + "\t" + ((ViewMessage) event.getPayload()).getPayload());
        } else {
            super.enqueue(event);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ViewerQueuedEvent queued;
                    try {
                        queued = getNextEvent();
                    } catch (EmptyQueueException e) {
                        return;
                    }

                    if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.EXIT) return;
                    if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS) setStatus(queued);
                    if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.CARDSELECTION)
                        if (ViewStatus.getActual().equals(ViewStatus.GAME_PREPARATION)){
                            GUISelectCard.attivate((CardSelection) queued.getPayload());
                        }
                }
            });
        }
    }


    public void displayErrorMessage(String message, ViewMessage.MessageType type){
        String buttonImage;

        JFrame errorPopup = new JFrame("ERROR");

        JPanel backPanel = new BackgroundPanel("/img/background/background_error.png");
        JPanel textPanel = new SubPanel(0.39, 0.2344, 0.29, 0.4);
        textPanel.setOpaque(false);
        JLabel label = new JLabel();
        label.setFont(new Font("Serif", Font.BOLD,10));
        label.setForeground(Color.RED);
        label.setText(message);
        textPanel.add(label);
        backPanel.add(textPanel);

        JButton closeButton = new JButton();
        if(type == ViewMessage.MessageType.FATAL_ERROR_MESSAGE){
            errorPopup.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            buttonImage = "/img/trappings/close_button.png";
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    System.exit(0);
                }
            });
        }else if((type == ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE)||(type == ViewMessage.MessageType.FROM_SERVER_ERROR)){
            errorPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            buttonImage = "/img/trappings/redButton.png";
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    errorPopup.dispose();
                }
            });
        }else{
            errorPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            buttonImage = "/img/trappings/blueButton.png";
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    errorPopup.dispose();
                }
            });
        }


        JPanel closePanel = new PanelImageButton(1, 0.15, 0, 0.7, closeButton, buttonImage, "quit");
        backPanel.add(closePanel);


        errorPopup.add(backPanel);

        errorPopup.setSize(320, 440);
        errorPopup.setResizable(false);
        errorPopup.setVisible(true);
        errorPopup.repaint();
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
        System.out.println("[GUI]\t"+ ViewStatus.getActual().toString());
        if(((StatusViewer)queued.getPayload()).toGUI() == null){
            //window.setVisible(false);
            return;
        }
        ((StatusViewer)queued.getPayload()).toGUI().setMyGUIViewer(this);

        try {
            ((StatusViewer)queued.getPayload()).toGUI().execute();
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
            try {
                queued = getNextEvent();
            } catch (EmptyQueueException e) {
                break;
            }

            if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.EXIT) return;
            if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS) setStatus(queued);
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
