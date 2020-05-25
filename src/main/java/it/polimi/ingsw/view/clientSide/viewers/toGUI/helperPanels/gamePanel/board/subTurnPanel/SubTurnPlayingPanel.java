package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;

import javax.swing.*;

public class SubTurnPlayingPanel extends PlayerSubTurnPanel {

    public SubTurnPlayingPanel(String playerName){
        super((ViewNickname.getMyNickname().equals(playerName)? "/img/background/subTurnPanel/canAction.png" :"/img/background/subTurnPanel/noActionPanel.png"), playerName);

        SubPanel contentPanel = new SubPanel(1, 1, 0, 0);
        contentPanel.setOpaque(false);

        SubPanel lowerPanel = new SubPanel(0.7, 0.36, 0.15, 0.54);
        lowerPanel.setOpaque(false);


        SubPanel upperPanel = new SubPanel(0.7, 0.24, 0.15, 0.3);
        upperPanel.setOpaque(false);


        JButton moveButton = new JButton();
        upperPanel.add(new PanelImageButton(0.5, 1, 0, 0, moveButton, "/img/trappings/move_button.png", "move"));

        JButton buildButton = new JButton();
        upperPanel.add(new PanelImageButton(0.5, 1, 0.5, 0, buildButton, "/img/trappings/build_button.png", "build"));

        lowerPanel.add(new ImagePanel(0.5, 1, 0, 0, "/img/godPodium/Default.png"));

        JButton buildBlock = new JButton();
        PanelImageButton buildBlockButtonPanel = new PanelImageButton(0.4, 0.4, 0.55, 0.05, buildBlock, "/img/trappings/build_block.png", "build Block" );
        lowerPanel.add(buildBlockButtonPanel);


        JButton buildDomeButton = new JButton();
        PanelImageButton buildDomeButtonPanel = new PanelImageButton(0.4, 0.4, 0.55, 0.5, buildDomeButton, "/img/trappings/build_dome.png", "buildDome" );
        lowerPanel.add(buildDomeButtonPanel);

        contentPanel.add(upperPanel);
        contentPanel.add(lowerPanel);

        add(contentPanel);







        /*JPanel rightDownPanel = new SubPanel(1, 0.6, 0, 0.4);
        //rightPanel.add(rightDownPanel);

        JButton buildBlockButton = new JButton();
        //PanelImageButton buildBlockButtonPanel =
        rightPanel.add(new PanelImageButton(1, 0.5, 0, 0, buildBlockButton, "/img/trappings/move_button.png", "Login" ));


        */



    }
}
