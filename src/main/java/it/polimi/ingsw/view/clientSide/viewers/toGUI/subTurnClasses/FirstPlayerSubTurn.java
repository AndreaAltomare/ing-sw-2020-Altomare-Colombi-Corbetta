package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.FirstPlayerExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.ForbiddenBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.SelectCardPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class FirstPlayerSubTurn extends GUISubTurnViewer {

    public FirstPlayerSubTurn(SubTurnViewer parent){
        super(parent);
    }

    @Override
    public JPanel getSubTurnPanel(){
        return new SelectCardPanel();
    }

    @Override
    public BoardSubTurn getBoardSubTurn(){
        return new ForbiddenBoardSubTurn(this);
    }

    public void onLoad(){
        FirstPlayerExecuter executer = (FirstPlayerExecuter)parent.getMySubTurn().getExecuter();
        List<String> list = executer.getPlayerList();
        double h = 0;

        JFrame frame = new JFrame("Select the first player");

        BackgroundPanel backPanel = new BackgroundPanel("/img/background/background_error.png");

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);



        for(String pl: list){
            /*JButton button = new JButton(pl);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    executer.set(pl);
                    try {
                        executer.doIt();
                        frame.dispose();
                    } catch (CannotSendEventException ignore) {
                    }
                }
            });
*/
            JLabel player = new JLabel(pl);
            player.setFont(new Font("Serif", Font.ITALIC,20));
            player.setForeground(Color.YELLOW);

            //JPanel playerPanel = new PanelComponent(1, 1, 1, 1, player);
            JPanel playerImgPanel = new ImagePanel(.8, 1.0/((double)list.size()) - 0.2, 0.1, h + 0.1, "/img/trappings/blueButton.png");

            //playerImgPanel.add(playerPanel);
            //playerPanel.setOpaque(false);
//            backPanel.add(playerImgPanel);

            JPanel playerPanel = new PanelComponent(.8, 1.0/((double)list.size()) - 0.2, 0.1, h + 0.1, player);
            //playerPanel.setBackground(Color.BLUE);
            playerPanel.setOpaque(false);

            playerPanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    executer.set(pl);
                    try {
                        executer.doIt();
                        frame.dispose();
                    } catch (CannotSendEventException ignore) {
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });

            backPanel.add(playerPanel);
            backPanel.add(playerImgPanel);

            //backPanel.add(new PanelImageButton(.8, 1.0/((double)list.size()) - 0.2, 0.1, h + 0.1, button, "/img/trappings/blueButton.png", pl));

            h = h + 1.0/((double)list.size());

        }

        frame.setContentPane(backPanel);



        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    executer.setRandom();
                    executer.doIt();
                } catch (CannotSendEventException | NullPointerException ignore) {
                }
            }
        });
        frame.setSize(320, 440);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.repaint();
    }
}
