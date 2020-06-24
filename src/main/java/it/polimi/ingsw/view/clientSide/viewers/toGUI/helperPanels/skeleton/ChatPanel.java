package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.PlayerMessageExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.PlayerMessages;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Panel that shows and send the players'messages.
 */
public class ChatPanel extends ImagePanel {

    /**
     * constant that indicates the maximum number of messages displayed.
     */
    static private final int maxMessageNumber = 12;

    private JLabel label;
    private JTextField inputMessage;
    private ArrayList<String> messages = new ArrayList<>(10);

    /**
     * constructor that sets the background image and prepare the Panel to be shown.
     */
    public ChatPanel() {
        super(0.9, 0.9, 0.05, 0.05,"/img/background/blocked_background.png");

        label = new JLabel("");
        JPanel textPanel = new PanelComponent(0.9, 0.7, 0.05, 0.05, label);
        textPanel.setOpaque(false);
        add(textPanel);


        inputMessage = new JTextField("");
        inputMessage.setOpaque(false);
        inputMessage.setBorder(null);
        inputMessage.setHorizontalAlignment(JTextField.CENTER);
        JPanel inputMessagePanel = new PanelComponent(0.7, 0.1, 0.05, 0.89, inputMessage);
        inputMessagePanel.setOpaque(false);
        add(inputMessagePanel);

        JButton sendButton = new JButton();
        sendButton.addActionListener(e -> {
            if(inputMessage.getText().equals(""))
                return;
            PlayerMessageExecuter messageExecuter = new PlayerMessageExecuter();
            messageExecuter.setMessage(inputMessage.getText());
            inputMessage.setText("");
            try {
                messageExecuter.doIt();
            } catch (CannotSendEventException ignore) {
            }

        });
        add( new PanelImageButton(0.1, 0.1, 0.85, 0.89, sendButton, "/img/trappings/forward_button.png", "next"));

        PlayerMessages.setChatPanel(this);
    }


    /**
     * Method to refresh the messages shown.
     */
    private void reprint(){
        StringBuilder ret = new StringBuilder("<html>");
        for(String m: messages){
            ret.append("<br>").append(m);
        }
        ret.append("</html>");

        label.setText(ret.toString());
    }

    /**
     * Method to add a message.
     * due to makee it readable, it can contains a finite number of messages,, and so it'll drop older messages whe new ones arrives.
     *
     * @param msg (the message to be added -and shown-).
     */
    public void addMessage(String msg){
        messages.add(msg);
        if(messages.size()>=maxMessageNumber)
            messages.remove(0);
        reprint();
        Viewer.setAllRefresh();
    }


}
