package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.NextTurnExecuter;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponentFixedScale;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * Class to represent the bottom-panel of the playing window. It contains 1+#players button:
 * - the change turn button;
 * - #players button showing the name and the workers' color. when pressed opens aa frame with the player's card.
 */
public class PlayingPlayersButton extends SubPanel {

    /**
     *
     */
    public PlayingPlayersButton(){
        super(1, 0.1, 0, 0.9);

        int numButt = 1;

        List<ViewPlayer> playerList = ViewPlayer.getPlayerList();

        for(ViewPlayer player: playerList){
            if(player.getOneWorker()!=null){
                numButt ++;
            }
        }

        this.setOpaque(false);


        //NextTurn Button
        ImagePanel nextTurn = new ImagePanel(1.00/((double)numButt), 1, 0, 0, "/img/trappings/redButton.png");

        JLabel nextTurnLabel = new JLabel("Next Turn");
        nextTurnLabel.setFont(new Font("Serif", Font.ITALIC,20));
        nextTurnLabel.setForeground(Color.YELLOW);
        PanelComponent nextTurnComponent = new PanelComponent( 1, 0.65, 0.1, 0, nextTurnLabel);
        nextTurnComponent.setOpaque(false);
        nextTurn.add(nextTurnComponent);

        nextTurn.addMouseListener(new MouseListener() {
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

        this.add(nextTurn);

        int i = 1;
        for(ViewPlayer player: playerList){
            if(player.getOneWorker()!=null){
                //NextTurn Button
                ImagePanel playerButton;

                if(player.getName().equals(ViewNickname.getMyNickname())){
                    playerButton = new ImagePanel(1.00/((double)numButt), 1, i*(1.00/((double)numButt)), 0, "/img/trappings/greenButton.png");
                }else{
                    playerButton = new ImagePanel(1.00/((double)numButt), 1, i*(1.00/((double)numButt)), 0, "/img/trappings/blueButton.png");
                }

                playerButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            JFrame frame = new JFrame(player.getName());
                            frame.add(new BackgroundPanel("/godCards/" + player.getCard().getName() + ".png"));
                            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                            frame.setSize(210, 353);
                            frame.setResizable(false);
                            frame.setVisible(true);
                        } catch (NotFoundException ignore) {
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

                //this.add(playerButton);

                JLabel playerNane = new JLabel(player.getName());
                playerNane.setFont(new Font("Serif", Font.ITALIC,20));
                playerNane.setForeground(Color.YELLOW);
                PanelComponent panelComponent = new PanelComponent( 1, 0.65, 0.1, 0, playerNane);
                panelComponent.setOpaque(false);
                playerButton.add(panelComponent);

//                playerButton.setOpaque(false);
                //playerNane.setOpaque(false);

                PanelComponentFixedScale workerFixedScale = new PanelComponentFixedScale(0.25, 1, 0.75, 0, player.getOneWorker().toGUI(), 1);
                workerFixedScale.setOpaque(false);

                playerButton.add(workerFixedScale);
                //playerButton.add(playerNane);

                this.add(playerButton);

                i++;
            }
        }

    }
}
