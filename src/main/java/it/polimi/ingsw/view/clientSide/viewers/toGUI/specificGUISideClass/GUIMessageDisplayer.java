package it.polimi.ingsw.view.clientSide.viewers.toGUI.specificGUISideClass;

import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIMessageDisplayer {
    public static void displayErrorMessage(String message, ViewMessage.MessageType type){
        String buttonImage;

        JFrame errorPopup = new JFrame();

        BackgroundPanel backPanel = new BackgroundPanel("/img/background/background_error.png");
        //JPanel textPanel = new SubPanel(0.39, 0.2344, 0.29, 0.4);
        JLabel label = new JLabel("");
        label.setFont(new Font("Serif", Font.BOLD,15));
        label.setForeground(Color.RED);
        label.setText("<html>"+ message +"</html>");
        JPanel textPanel = new PanelComponent(0.39, 0.2344, 0.29, 0.3, label);
        textPanel.setOpaque(false);
        //textPanel.add(label);
        backPanel.add(textPanel);

        JButton closeButton = new JButton();
        if(type == ViewMessage.MessageType.FATAL_ERROR_MESSAGE){
            errorPopup.setTitle("FATAL ERROR");
            errorPopup.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            buttonImage = "/img/trappings/close_button.png";
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    System.exit(0);
                }
            });
        }else if((type == ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE)||(type == ViewMessage.MessageType.FROM_SERVER_ERROR)){
            errorPopup.setTitle("ERROR");
            errorPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            buttonImage = "/img/trappings/redButton.png";
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    errorPopup.dispose();
                }
            });
        }else if(type == ViewMessage.MessageType.WIN_MESSAGE){
            errorPopup.setTitle("VICTORY");
            label.setForeground(Color.GREEN);
            backPanel.setBackgroundImg("/img/background/background_win.png");
            errorPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            buttonImage = "/img/trappings/blueButton.png";
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    errorPopup.dispose();
                }
            });
        }else if(type == ViewMessage.MessageType.LOOSE_MESSAGE){
            errorPopup.setTitle("LOOSE");
            label.setForeground(Color.GREEN);
            backPanel.setBackgroundImg("/img/background/background_loose.png");
            errorPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            buttonImage = "/img/trappings/blueButton.png";
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    errorPopup.dispose();
                }
            });
        }else{
            errorPopup.setTitle("Message");
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

}
