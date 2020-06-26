package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.NextTurnExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.TurnStatusChangeExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class to represent the Panel at the side of the board when the sub turn of the current player.
 */
class SubTurnPlayingPanel extends PlayerSubTurnPanel {

    /**
     * constructor with all the parameters needed for the creation of the panel.
     *
     * @param playerName  (name of the player of which it's the turn of).
     * @param move        (true iif the player can switch into MOVE SubTurn).
     * @param build       (true iif the player can switch into CONSTRUCTION subTurn).
     * @param buildBlock  (true iif the player can switch into BUILD_BLOCK subTurn).
     * @param buildDome   (true iif the player can switch into BUILD_DOME subTurn).
     */
    SubTurnPlayingPanel(String playerName, boolean move, boolean build, boolean buildBlock, boolean buildDome){
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

        ImagePanel selectPanel = new ImagePanel(1, 1, 0, 0, "/img/trappings/select_button.png");
        if(move){
            if(View.debugging)
                System.out.println("MOVE");
            selectPanel.setMyRapp(0.5, 1, 0, 0);
            upperPanel.add(selectPanel);
        }else if (mine) {
            moveButton.addActionListener(actionEvent -> {
                TurnStatusChangeExecuter myExec = new TurnStatusChangeExecuter();
                try {
                    myExec.setStatusId(ViewSubTurn.MOVE);
                    myExec.doIt();
                } catch (WrongParametersException | CannotSendEventException e) {
                    ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                }
            });
        }



        //Build Button
        JButton buildButton = new JButton();

        if(build) {
            if(View.debugging)
                System.out.println("BUILD");
            selectPanel.setMyRapp(0.5, 1, 0.5, 0);
            upperPanel.add(new ImagePanel(0.5, 1, 0.5, 0,"/img/trappings/select_button.png" ));
        }else if(mine){
            buildButton.addActionListener(actionEvent -> {
                TurnStatusChangeExecuter myExec = new TurnStatusChangeExecuter();

                try {
                    myExec.setStatusId(ViewSubTurn.BUILD);
                    myExec.doIt();
                } catch (WrongParametersException | CannotSendEventException e) {
                    ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                }
            });
        }

        upperPanel.add(new PanelImageButton(0.5, 1, 0, 0, moveButton, "/img/trappings/move_button.png", "move"));
        upperPanel.add(new PanelImageButton(0.5, 1, 0.5, 0, buildButton, "/img/trappings/build_button.png", "build"));

        //GodImage

        /*try{
            lowerPanel.add(new PanelImageButton(0.5, 1, 0, 0, nextTurnButton, "/img/godPodium/" + ViewPlayer.searchByName(playerName).getCard().getName() + ".png", "next turn"));
            //.add(new PanelImageButton(.9, 1, 0.05, 0, nextTurnButton, "/img/godPodium/" + ViewPlayer.searchByName(playerName).getCard().getName() + ".png", "next turn"));
        }catch(Exception e){
            lowerPanel.add(new PanelImageButton(0.5, 1, 0, 0, nextTurnButton, "/img/godPodium/Default.png", "next turn"));
            //lowerLeftPanel.add(new PanelImageButton(.9, 1, 0.05, 0, nextTurnButton, "/img/godPodium/Default.png", "next turn"));
        }*/


        try {
            ImagePanel miniGod = new ImagePanel(0.5, 1, 0, 0, "/img/godPodium/" + ViewPlayer.searchByName(playerName).getCard().getName() + ".png");

            miniGod.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        new NextTurnExecuter().doIt();
                    } catch (CannotSendEventException ex) {
                        if(View.debugging)
                            ex.printStackTrace();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {}

                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {}

                @Override
                public void mouseExited(MouseEvent e) {}
            });

            lowerPanel.add(miniGod);

            //lowerPanel.add(new PanelImageButton(0.45, .9, 0, 0, nextTurnButton, "/img/godPodium/" + ViewPlayer.searchByName(playerName).getCard().getName() + ".png", "next turn"));
        }catch(Exception e){
            lowerPanel.add(new ImagePanel(0.5, 1, 0, 0, "/img/godPodium/Default.png"));
        }



        //BuildBlockButton
        JButton buildBlockButton = new JButton();

        if(buildBlock) {
            if(View.debugging)
                System.out.println("BUILD_BLOCK");
            selectPanel.setMyRapp(0.4, 0.4, 0.55, 0.05);
            lowerPanel.add(selectPanel);
            //lowerRightPanel.add(selectPanel);
        }else if(mine){
            buildBlockButton.addActionListener(actionEvent -> {
                if(build || buildDome) {
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
            });
        }



        //BuildDomeButton
        JButton buildDomeButton = new JButton();

        if(buildDome) {
            if(View.debugging)
                System.out.println("BUILD_DOME");
            selectPanel.setMyRapp(0.4, 0.4, 0.55, 0.5);
            lowerPanel.add(selectPanel);
            //lowerRightPanel.add(selectPanel);
        }else if(mine){
            buildDomeButton.addActionListener(actionEvent -> {
                if(build || buildBlock) {
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
            });
        }

        lowerPanel.add(new PanelImageButton(0.4, 0.4, 0.55, 0.05, buildBlockButton, "/img/trappings/build_block.png", "build Block" ));
        lowerPanel.add(new PanelImageButton(0.4, 0.4, 0.55, 0.5, buildDomeButton, "/img/trappings/build_dome.png", "buildDome" ));

        //lowerPanel.add(lowerRightPanel);

        //lowerRightPanel.add(new PanelImageButton(0.6, 0.2, 0.1, 0.5, buildDomeButton, "/img/trappings/build_dome.png", "buildDome" ));
        //lowerRightPanel.add(new PanelImageButton(0.6, 0.2, 0.1, 0.05, buildBlockButton, "/img/trappings/build_block.png", "build Block" ));

        //lowerPanel.add(lowerRightPanel);

        //lowerRightPanel.add(new PanelImageButton(0.8, 0.3, 0.1, 0.45, buildDomeButton, "/img/trappings/build_block.png", "build Block" ));



        //lowerPanel.add(lowerLeftPanel);

        contentPanel.add(upperPanel);
        contentPanel.add(lowerPanel);


        add(contentPanel);
    }
}
