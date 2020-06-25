package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.ResumingExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.exceptions.AlreadySetException;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class to represent the <code>GUIStatusViewer</code> for the ViewStatus REQUEST_RESUMING .
 */
public class GUIResumeRequest extends GUIStatusViewer {

    ResumingExecuter resumingExecuter;

    /**
     * constructor.
     *
     * @param statusViewer (the <code>StatusViewer</code> to which this refers).
     */
    public GUIResumeRequest(StatusViewer statusViewer){
        resumingExecuter = (ResumingExecuter) statusViewer.getMyExecuters().get("Resume");
    }

    /**
     * Method that says this has a PopUp to be shown.
     *
     * @return (true).
     * @see GUIStatusViewer
     */
    @Override
    public boolean hasPopup(){
        return true;
    }

    /**
     * Method that shows the PopUp.
     *
     * @see GUIStatusViewer
     */
    @Override
    public void doPopUp(){

        JFrame frame = new JFrame("Game Resume?");

        BackgroundPanel backPanel = new BackgroundPanel("/img/background/background_error.png");

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("");
        label.setFont(new Font("Serif", Font.BOLD,15));
        label.setForeground(Color.GREEN);
        label.setText("<html>There is an old game.<br/>Shall we resume?</html>");
        JPanel textPanel = new PanelComponent(0.39, 0.3, 0.29, 0.3, label);
        textPanel.setOpaque(false);
        //textPanel.add(label);
        backPanel.add(textPanel);

        JButton yesButton = new JButton();
        yesButton.addActionListener(actionEvent -> {
            try {
                resumingExecuter.setResume(true);
                resumingExecuter.doIt();
            } catch (CannotSendEventException | AlreadySetException ignore) {
            }

            frame.dispose();

        });

        JPanel yesPanel = new PanelImageButton(.5, 0.15, 0, 0.7, yesButton, "/img/trappings/blueButton.png", "yes");

        JButton noButton = new JButton();
        noButton.addActionListener(actionEvent -> {
            try {
                resumingExecuter.setResume(false);
                resumingExecuter.doIt();
            } catch (CannotSendEventException | AlreadySetException ignore) {
            }

            frame.dispose();

        });

        JPanel noPanel = new PanelImageButton(.5, 0.15, 0.5, 0.7, noButton, "/img/trappings/redButton.png", "no");

        backPanel.add(yesPanel);
        backPanel.add(noPanel);

        frame.setContentPane(backPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    resumingExecuter.setResume(false);
                    resumingExecuter.doIt();
                } catch (CannotSendEventException | NullPointerException | AlreadySetException ignore) {
                }
            }
        });
        frame.setSize(320, 440);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.repaint();

    }
}
