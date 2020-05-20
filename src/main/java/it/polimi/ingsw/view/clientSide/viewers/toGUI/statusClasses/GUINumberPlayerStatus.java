package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetPlayerNumberExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUINumberPlayerStatus extends GUIStatusViewer {

    private Object myWaiting = new Object();

    StatusViewer myStatusViewer;
    public GUINumberPlayerStatus(StatusViewer statusViewer){ myStatusViewer = statusViewer; }

    public boolean hasJPanel(){ return true; }

    public JPanel getJPanel(){
        JPanel panel = new BackgroundPanel("/img/background/blocked_background.png");
        new TitlePanel(panel);
        return panel;
    }

    @Override
    public boolean hasPopup(){ return true;}

    private static final Integer defaultNumber = 2;



    @Override
    public void doPopUp(){
        JFrame frame = new JFrame("Number of Players");

        JPanel panel = new BackgroundPanel("/img/background/doubleShelf.png");


        JButton button2 = new JButton();
        PanelImageButton buttonPanel2 = new PanelImageButton(0.45, 0.87, 0.05, 0.05, button2, "/img/trappings/2playersbutton.png", "2Players" );

        JButton button3 = new JButton();
        PanelImageButton buttonPanel3 = new PanelImageButton(0.45, 0.87, 0.525, 0.05, button3, "/img/trappings/3playersbutton.png", "3Players" );

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SetPlayerNumberExecuter executer = (SetPlayerNumberExecuter)myStatusViewer.getMyExecuters().get("NumberPlayers");
                System.out.println("Numero giocatori impostato a " + 2);
                try {
                    executer.setNumberOfPlayers(2);
                    executer.doIt();
                    frame.dispose();
                } catch (CannotSendEventException e) {
                    ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                    if (View.debugging)
                        e.printStackTrace();
                } catch (WrongParametersException e) {
                    ViewMessage.populateAndSend("Wrong parameter!!", ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                    if (View.debugging)
                        e.printStackTrace();
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SetPlayerNumberExecuter executer = (SetPlayerNumberExecuter)myStatusViewer.getMyExecuters().get("NumberPlayers");
                System.out.println("Numero giocatori impostato a " + 3);
                try {
                    executer.setNumberOfPlayers(3);
                    executer.doIt();
                    frame.setVisible(false);
                } catch (CannotSendEventException e) {
                    ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                    if (View.debugging)
                        e.printStackTrace();
                } catch (WrongParametersException e) {
                    ViewMessage.populateAndSend("Wrong parameter!!", ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                    if (View.debugging)
                        e.printStackTrace();
                }
            }
        });

        panel.add(buttonPanel3);
        panel.add(buttonPanel2);


        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.repaint();

    }
}
