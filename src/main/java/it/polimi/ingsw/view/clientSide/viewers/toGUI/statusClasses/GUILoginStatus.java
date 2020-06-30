package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetNicknameExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import javax.swing.*;
import java.awt.*;

/**
 * Class to represent the <code>GUIStatusViewer</code> for the ViewStatus LOGIN .
 */
public class GUILoginStatus extends GUIStatusViewer {

    StatusViewer myStatusViewer;
    /**
     * constructor.
     *
     * @param statusViewer (the <code>StatusViewer</code> to which this refers).
     */
    public GUILoginStatus(StatusViewer statusViewer){ myStatusViewer = statusViewer; }

    /**
     * Method that says this has a <code>JPanel</code> to be shown.
     *
     * @return (true).
     * @see GUIStatusViewer
     */
    @Override
    public boolean hasJPanel(){ return true; }

    /**
     * method that returns the <code>Jpanel</code> referring to this that needs to be shown.
     *
     * @return (the <code>JPanel</code> that represents this)
     * @see GUIStatusViewer
     */
    @Override
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
                        if(View.debugging)
                            e.printStackTrace();
                    }
                }
        );

        pergamena.add(buttonPanel);

        return ret;
    }

    /**
     * method that is executed on the loading of the Status.
     * It starts playing the music relative to this status.
     *
     * @see GUIStatusViewer
     */
    @Override
    public void onLoad(){
        SoundEffect.startLoopMusic("/statusSounds/login.wav");
    }

    /**
     * method that is executed on the closing of the Status.
     * It stops playing the music relative to this status.
     *
     * @see GUIStatusViewer
     */
    @Override
    public void onClose(){
        SoundEffect.stopLoopingMusic();
    }

}
