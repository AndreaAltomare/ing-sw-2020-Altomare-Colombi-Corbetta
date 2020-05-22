package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.exceptions.NotFoundException;

import javax.swing.*;
import java.awt.*;

public class SubTurnPanel extends ImagePanel {
    
    JLabel name;
    PanelImageButton moveButtonPanel;
    PanelImageButton buildButtonPanel;
    PanelImageButton domeButtonPanel;
    PanelImageButton blockButtonPanel;
    ImagePanel miniGodPanel;

    public SubTurnPanel(){
        super(0.9, 0.9, 0.05, 0.05, "/img/background/turnPanel.png");

        /*name = new JLabel();
        name.setFont(new Font("Serif", Font.ITALIC,10));
        name.setForeground(Color.YELLOW);

        //name
        JPanel namePanel = new PanelComponent(0.3, 0.8, 0.15, 0, name);
        JPanel nameImgPanel = new ImagePanel(0.7, 0.2, 0.15, 0.1, "/img/trappings/god_name.png");
        nameImgPanel.add(namePanel);
        namePanel.setOpaque(false);
        name.setText("Select Worker");
        //add(nameImgPanel);

        //moveButton
        JButton moveButton = new JButton();
        moveButtonPanel = new PanelImageButton(0.4, 0.2, 0.15, 0.3, moveButton, "/img/trappings/move_button.png", "move" );
        //add(moveButtonPanel);

        add(new PanelImageButton(0.4, 0.2, 0.15, 0.3, moveButton, "/img/trappings/move_button.png", "Login" ));

        //buildButton
        JButton buildButton = new JButton();
        //buildButtonPanel = new PanelImageButton(0.4, 0.2, 0.5, 0.3, buildButton, "/img/trappings/build_button.png", "build");
        buildButtonPanel = new PanelImageButton(0.4, 0.2, 0.5, 0.3, moveButton, "/img/trappings/build_button.png", "build" );
        //add(buildButtonPanel);

        //buildDome
        JButton domeButton = new JButton();
        domeButtonPanel = new PanelImageButton(0.35, 0.15, 0.5, 0.55, domeButton, "/img/trappings/build_dome.png", "build dome");
        domeButton.setMinimumSize(new Dimension(0, 0));
        //add(domeButtonPanel);

        //godImage
        miniGodPanel = new ImagePanel(0.35, 0.35, 0.15, 0.5, "/img/godPodium/Default.png");
        //add(miniGodPanel);


        this.setOpaque(false);*/
    }

    /*void update(ViewPlayer player){
        name.setText(player.getName());
        if(ViewNickname.getMyNickname().equals(player.getName())){
            setBackgroundImg("");
        }else{
            setBackgroundImg("");
        }
        try {
            miniGodPanel.setBackgroundImg("/img/godPodium/" + player.getCard().getName() + ".png");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if(!isSetImg()){
            miniGodPanel.setBackgroundImg("/img/godPodium/Default.png");
        }
    }*/
}
