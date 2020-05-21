package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.BodyPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends BackgroundPanel {
    public GamePanel(){
        super("/img/background/sized_waiting_background.png");
        new TitlePanel(this);
        add(new BodyPanel());
    }
}
