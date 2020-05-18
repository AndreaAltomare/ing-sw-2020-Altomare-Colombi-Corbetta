package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIReadyStatus extends GUIStatusViewer {

    StatusViewer myStatusViewer;
    public GUIReadyStatus(StatusViewer statusViewer){ myStatusViewer = statusViewer; }

    @Override
    public boolean hasDirectFrameManipulation(){ return true; }

    @Override
    public void directFrameManipulation(){
        JFrame frame = guiViewer.getWindow();
        try {

            BufferedImage backgroundImg;
            backgroundImg = ImageIO.read(getClass().getResource("/img/background/Odyssey-Cyclops.png"));
            frame.setContentPane(new JLabel(new ImageIcon(backgroundImg)));
            frame.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            // Add stuff...
            /*frame.add(new JLabel("Hello world"), gbc);
            frame.add(new JLabel("I'm on top"), gbc);
            frame.add(new JButton("Clickity-clackity"), gbc);*/

            //frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error();
        }
    }
}
