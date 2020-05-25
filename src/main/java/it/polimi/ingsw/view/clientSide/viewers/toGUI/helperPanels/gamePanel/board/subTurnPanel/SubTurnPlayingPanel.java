package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.TurnStatusChangeExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubTurnPlayingPanel extends PlayerSubTurnPanel {

    ImagePanel selectPanel = new ImagePanel(1, 1, 0, 0, "/img/trappings/select_button.png");

    public SubTurnPlayingPanel(String playerName, boolean move, boolean build, boolean buildBlock, boolean buildDome){
        super((ViewNickname.getMyNickname().equals(playerName)? "/img/background/subTurnPanel/canAction.png" :"/img/background/subTurnPanel/noActionPanel.png"), playerName);

        boolean mine = ViewNickname.getMyNickname().equals(playerName);

        SubPanel contentPanel = new SubPanel(1, 1, 0, 0);
        contentPanel.setOpaque(false);

        SubPanel lowerPanel = new SubPanel(0.7, 0.36, 0.15, 0.54);
        lowerPanel.setOpaque(false);


        SubPanel upperPanel = new SubPanel(0.7, 0.24, 0.15, 0.3);
        upperPanel.setOpaque(false);



        //Move Button
        JButton moveButton = new JButton();

        if(move){
            System.out.println("MOVE");
            selectPanel.setMyRapp(0.5, 1, 0, 0);
            upperPanel.add(selectPanel);
        }else if (mine) {
            moveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    TurnStatusChangeExecuter myExec = new TurnStatusChangeExecuter();
                    try {
                        myExec.setStatusId(ViewSubTurn.MOVE);
                        myExec.doIt();
                    } catch (WrongParametersException | CannotSendEventException e) {
                        ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                    }
                }
            });
        }



        //Build Button
        JButton buildButton = new JButton();

        if(build) {
            System.out.println("BUILD");
            selectPanel.setMyRapp(0.5, 1, 0.5, 0);
            upperPanel.add(new ImagePanel(0.5, 1, 0.5, 0,"/img/trappings/select_button.png" ));
        }else if(mine){
            buildButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    TurnStatusChangeExecuter myExec = new TurnStatusChangeExecuter();
                    try {
                        myExec.setStatusId(ViewSubTurn.BUILD);
                        myExec.doIt();
                    } catch (WrongParametersException | CannotSendEventException e) {
                        ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                    }
                }
            });
        }



        upperPanel.add(new PanelImageButton(0.5, 1, 0, 0, moveButton, "/img/trappings/move_button.png", "move"));
        upperPanel.add(new PanelImageButton(0.5, 1, 0.5, 0, buildButton, "/img/trappings/build_button.png", "build"));



        //GodImage
        try {
            lowerPanel.add(new ImagePanel(0.5, 1, 0, 0, "/img/godPodium/" + ViewPlayer.searchByName(playerName).getCard().getName() + ".png"));
        }catch(Exception e){
            lowerPanel.add(new ImagePanel(0.5, 1, 0, 0, "/img/godPodium/Default.png"));
        }



        //BuildBlockButton
        JButton buildBlockButton = new JButton();

        if(buildBlock) {
            System.out.println("BUILD_BLOCK");
            selectPanel.setMyRapp(0.4, 0.4, 0.55, 0.05);
            lowerPanel.add(selectPanel);
        }else if(mine){
            buildBlockButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if(build || buildDome || buildBlock) {
                        TurnStatusChangeExecuter myExec = new TurnStatusChangeExecuter();
                        try {
                            myExec.setStatusId(ViewSubTurn.BUILD_BLOCK);
                            myExec.doIt();
                        } catch (WrongParametersException | CannotSendEventException e) {
                            ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                        }
                    }else{
                        ViewMessage.populateAndSend("You have to go on building before this", ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                    }
                }
            });
        }



        //BuildDomeButton
        JButton buildDomeButton = new JButton();

        if(buildDome) {
            System.out.println("BUILD_DOME");
            selectPanel.setMyRapp(0.4, 0.4, 0.55, 0.5);
            lowerPanel.add(selectPanel);
        }else if(mine){
            buildDomeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if(build || buildDome || buildBlock) {
                        TurnStatusChangeExecuter myExec = new TurnStatusChangeExecuter();

                        try {
                            myExec.setStatusId(ViewSubTurn.BUILD_DOME);
                            myExec.doIt();
                        } catch (WrongParametersException | CannotSendEventException e) {
                            ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                        }
                    }else{
                        ViewMessage.populateAndSend("You have to go on building before this", ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                    }
                }
            });
        }

        lowerPanel.add(new PanelImageButton(0.4, 0.4, 0.55, 0.05, buildBlockButton, "/img/trappings/build_block.png", "build Block" ));
        lowerPanel.add(new PanelImageButton(0.4, 0.4, 0.55, 0.5, buildDomeButton, "/img/trappings/build_dome.png", "buildDome" ));

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
