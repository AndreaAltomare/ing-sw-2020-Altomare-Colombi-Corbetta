package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.PlayerMessageExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.PlayerMessages;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ChatPanel extends ImagePanel {

    private JLabel label;
    private JTextField inputMessage;
    private ArrayList<String> messages = new ArrayList<String>(10);

    public ChatPanel() {
        super(0.9, 0.9, 0.05, 0.05,"/img/background/blocked_background.png");

        label = new JLabel("");
        //label.setText("<html>" +  +"</html>");
        JPanel textPanel = new PanelComponent(0.9, 0.7, 0.05, 0.05, label);
        textPanel.setOpaque(false);
        add(textPanel);


        inputMessage = new JTextField("");
        inputMessage.setOpaque(false);
        inputMessage.setBorder(null);
        inputMessage.setHorizontalAlignment(JTextField.CENTER);
        JPanel inputMessagePanel = new PanelComponent(0.7, 0.1, 0.05, 0.85, inputMessage);
        inputMessagePanel.setOpaque(false);
        add(inputMessagePanel);

        JButton sendButton = new JButton();
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inputMessage.getText().equals(""))
                    return;
                PlayerMessageExecuter messageExecuter = new PlayerMessageExecuter();
                messageExecuter.setMessage(inputMessage.getText());
                inputMessage.setText("");
                try {
                    messageExecuter.doIt();
                } catch (CannotSendEventException ignore) {
                }

            }
        });
        add( new PanelImageButton(0.1, 0.1, 0.85, 0.85, sendButton, "/img/trappings/forward_button.png", "next"));

        addMessage("TESTING<br/>new line...");

        PlayerMessages.setChatPanel(this);
    }

    public void reprint(){
        String ret = "<html>";
        for(String m: messages){
            ret += "<br>" + m;
        }
        ret += "</html>";

        label.setText(ret);
    }

    public void addMessage(String msg){
        messages.add(msg);
        if(messages.size()>=12)
            messages.remove(0);
        reprint();
        Viewer.setAllRefresh();
    }


}
