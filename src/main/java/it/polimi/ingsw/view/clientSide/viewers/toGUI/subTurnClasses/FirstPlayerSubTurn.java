package it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.FirstPlayerExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.specific.ForbiddenBoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel.SelectCardPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Class extending <code>GUISubTurnViewer</code> representing the CHOOSE_FIRST_PLAYER <code>ViewSubTurn</code>.
 */
public class FirstPlayerSubTurn extends GUISubTurnViewer {

    /**
     * constructor.
     *
     * @param parent (the <code>SubTurnViewer</code> to which this refers to).
     */
    public FirstPlayerSubTurn(SubTurnViewer parent){
        super(parent);
    }

    /**
     * Method that returns the subTurnPanel relatives to the CHOOSE_FIRST_PLAYER subTurn.
     *
     * @return (the <code>JPanel</code> relative to the CHOOSE_FIRST_PLAYER subTurn).
     */
    @Override
    public JPanel getSubTurnPanel(){
        return new SelectCardPanel();
    }

    /**
     * Method that returns the BoardSubTurn referring to CHOOSE_FIRST_PLAYER.
     *
     * @return (the <code>getBoardSubTurn</code> that refers to the CHOOSE_FIRST_PLAYER subTurn).
     */
    @Override
    public BoardSubTurn getBoardSubTurn(){
        return new ForbiddenBoardSubTurn(this);
    }

    /**
     * Method that is called on the loading of the subTurn.
     * this method shows the new JFrame and initializes it.
     */
    @Override
    public void onLoad(){
        FirstPlayerExecuter executer = (FirstPlayerExecuter)parent.getMySubTurn().getExecuter();
        List<String> list = executer.getPlayerList();
        double h = 0;

        JFrame frame = new JFrame("Select the first player");

        BackgroundPanel backPanel = new BackgroundPanel("/img/background/background_error.png");

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);



        for(String pl: list){

            JLabel player = new JLabel(pl);
            player.setFont(new Font("Serif", Font.ITALIC,20));
            player.setForeground(Color.YELLOW);

            //JPanel playerPanel = new PanelComponent(1, 1, 1, 1, player);
            JPanel playerImgPanel = new ImagePanel(.8, 1.0/((double)list.size()) - 0.2, 0.1, h + 0.1, "/img/trappings/blueButton.png");

            //playerImgPanel.add(playerPanel);
            //playerPanel.setOpaque(false);
            //backPanel.add(playerImgPanel);

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
