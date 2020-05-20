package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIGamePreparation extends GUIStatusViewer {
    StatusViewer myStatusViewer;
    public GUIGamePreparation(StatusViewer statusViewer){ myStatusViewer = statusViewer; }

    private JFrame frame;
    private GridBagConstraints constraints;
    private GridBagLayout layout;

    @Override
    public boolean hasDirectFrameManipulation(){ return true; }

    private void addComponent( Component component, int row, int column, int width, int height){
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        layout.setConstraints(component, constraints);
        frame.add(component);
    }

    @Override
    public void directFrameManipulation(){
        frame = guiViewer.getWindow();
        try {

            BufferedImage backgroundImg;
            backgroundImg = ImageIO.read(getClass().getResource("/img/background/mainBeckground.png"));
            frame.setContentPane(new JLabel(new ImageIcon(backgroundImg)));
            layout = new GridBagLayout();
            frame.setLayout(layout);
            constraints = new GridBagConstraints();

            Icon titleLogo = new ImageIcon(getClass().getResource("/img/logo/Santorini.png"));
            JLabel titleLabel = new JLabel("", titleLogo, SwingConstants.CENTER);
            addComponent(titleLabel, 0, 0, 3, 1);

            JTextArea messageArea = new JTextArea("Message area");
            addComponent(messageArea, 5, 0, 3, 1);

            Icon boardImage = new ImageIcon(getClass().getResource("/img/board/boardScalata.png"));
            JLabel boardLabel = new JLabel("", boardImage, SwingConstants.CENTER);
            addComponent(boardLabel, 2, 0, 2, 3);

            JTextArea playersArea = new JTextArea("Players area");
            addComponent(playersArea, 2, 2, 1, 3);


            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error();
        }
    }
}
