package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetNicknameExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
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

    @Override
    public boolean hasDirectFrameManipulation(){ return true; }

    @Override
    public void directFrameManipulation(){
        JFrame frame = guiViewer.getWindow();
        try {

            BufferedImage backgroundImg;
            backgroundImg = ImageIO.read(getClass().getResource("/img/background/Odyssey_UI_Backdrop.png"));
            frame.setContentPane(new JLabel(new ImageIcon(backgroundImg)));
            frame.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            JLabel nicknameText = new JLabel("Insert Nickname");
            nicknameText.setForeground(Color.RED);
            nicknameText.setFont(new Font("Serif", Font.PLAIN, 30));
            frame.add(nicknameText, gbc);

            TextField textField = new TextField("nickname", 25);
            textField.setFont(new Font("Serif", Font.PLAIN, 25));
            frame.add(textField, gbc);

            JButton button = new JButton("set Nickname");
            button.setBackground(Color.RED);
            button.setFont(new Font("Serif", Font.PLAIN, 25));
            button.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            String nickname = textField.getText();
                            SetNicknameExecuter executer = (SetNicknameExecuter)myStatusViewer.getMyExecuters().get("NickName");
                            System.out.println("Nickname impostato a " + nickname);
                            try {
                                executer.setNickname(nickname);
                                executer.doIt();
                            } catch (WrongParametersException | CannotSendEventException e) {
                                ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                                e.printStackTrace();
                            }
                        }
                    }
            );
            frame.add(button);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error();
        }
    }
}
