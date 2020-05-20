package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetNicknameExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponentFixedScale;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUILoginStatus extends GUIStatusViewer {
    StatusViewer myStatusViewer;
    public GUILoginStatus(StatusViewer statusViewer){ myStatusViewer = statusViewer; }

    public boolean hasJPanel(){ return true; }

    public JPanel getJPanel(){
        //Creo la schermata principale
        JPanel ret = new BackgroundPanel("/img/background/Odyssey_UI_Backdrop.png");
        new TitlePanel(ret);

        //creo la parte della pergamena
        JPanel backPergamena = new ImagePanel(0.25, 0.25, 0.375, 0.6,"/img/background/golden_background.jpg");
        JPanel pergamena = new ImagePanel(0.95, 0.9, 0.025, 0.05, "/img/background/pergamena.png");
        backPergamena.add(pergamena);
        ret.add(backPergamena);

        //aggiungo le 2 figure a lato
        pergamena.add(new ImagePanel(0.3, 0.9, 0, 0.3, "/img/trappings/title_poseidon.png"));
        pergamena.add(new ImagePanel(0.3, 0.9, 0.75, 0.3, "/img/trappings/title_aphrodite.png"));

        //Creo la parte di Input

        //Creo il JPanel per l'input
        JTextField nicknameTextField = new JTextField("nickname");
        nicknameTextField.setOpaque(false);
        nicknameTextField.setBorder(null);
        nicknameTextField.setForeground(Color.YELLOW);
        nicknameTextField.setHorizontalAlignment(JTextField.CENTER);
        nicknameTextField.setFont(new Font(
                "Serif",
                Font.BOLD,
                25
        ));
        JPanel nickname = new PanelComponent(0.7, 0.3, 0.15, 0, nicknameTextField);
        nickname.setOpaque(false);
        pergamena.add(nickname);

        //Creo il JPanel per il bottone
        JButton button = new JButton();
        PanelImageButton buttonPanel = new PanelImageButton(0.5, 0.7, 0.275, 0.3, button, "/img/trappings/play_button.png", "Login" );

        //imposto l'azione del bottone
        button.addActionListener(
                actionEvent -> {
                    String nickname1 = nicknameTextField.getText();
                    SetNicknameExecuter executer = (SetNicknameExecuter)myStatusViewer.getMyExecuters().get("NickName");
                    if(View.debugging)
                        System.out.println("Nickname impostato a " + nickname1);
                    try {
                        executer.setNickname(nickname1);
                        executer.doIt();
                    } catch (WrongParametersException | CannotSendEventException e) {
                        ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
        );

        pergamena.add(buttonPanel);

        return ret;
    }
}
