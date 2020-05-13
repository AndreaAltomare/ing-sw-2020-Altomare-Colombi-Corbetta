package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetPlayerNumberExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
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

public class GUINumberPlayerStatus extends GUIStatusViewer {
    StatusViewer myStatusViewer;
    public GUINumberPlayerStatus(StatusViewer statusViewer){ myStatusViewer = statusViewer; }

    @Override
    public boolean hasPopup(){ return true;}

    @Override
    public void doPopUp(){
        JDialog dialog = new JDialog();
        try {
            BufferedImage backgroundImg;
            backgroundImg = ImageIO.read(getClass().getResource("/img/background/scilla.png"));
            dialog.setContentPane(new JLabel(new ImageIcon(backgroundImg)));
            dialog.setSize(256, 512);

            dialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            JLabel titleText = new JLabel("Insert number of players");
            titleText.setForeground(Color.GREEN);
            titleText.setFont(new Font("Serif", Font.PLAIN, 20));
            dialog.add(titleText, gbc);

            TextField textField = new TextField("0", 15);
            textField.setFont(new Font("Serif", Font.PLAIN, 10));
            dialog.add(textField, gbc);

            JButton button = new JButton("set");
            button.setBackground(Color.RED);
            button.setFont(new Font("Serif", Font.PLAIN, 10));
            button.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            int numberPlayer;
                            try{
                                numberPlayer = Integer.parseInt(textField.getText());
                            }catch(Exception e) {
                                textField.setText("");
                                titleText.setForeground(Color.RED);
                                return;
                            }
                            SetPlayerNumberExecuter executer = (SetPlayerNumberExecuter)myStatusViewer.getMyExecuters().get("NumberPlayers");
                            System.out.println("Numero giocatori impostato a " + numberPlayer);
                            try {
                                executer.setNumberOfPlayers(numberPlayer);
                                executer.doIt();
                                dialog.setVisible(false);
                            } catch (WrongParametersException | CannotSendEventException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
            dialog.add(button);

            dialog.setVisible(true);
            guiViewer.getWindow().setEnabled(false);
            guiViewer.waitNextEvent();
            dialog.setVisible(false);
            guiViewer.getWindow().setEnabled(true);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
