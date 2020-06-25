package it.polimi.ingsw.view.clientSide.viewers.toGUI;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.specificGUISideClass.GUIMessageDisplayer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.specificGUISideClass.GUISelectCard;
import it.polimi.ingsw.view.exceptions.CheckQueueException;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * <code>Viewer</code> referring to the GUI and managing it (and its' working).
 *
 * @see Viewer
 */
public class GUIViewer extends Viewer {

    private JFrame window;
    private GUIStatusViewer actualStatus;


    /**
     * Method forcing a refreshing.
     * Not used because the GUI uses the REFRESH Event.
     */
    @Override
    public void refresh() {  }

    /**
     * Method to add a <code>ViewerQueuedEvent</code> to the queue of Events to be served.
     * After adding it to the queue, it'll also notify the Swing's thread to serve a new element from the queue.
     *
     * @param event (a new <code>ViewerQueuedEvent</code> to be served later).
     */
    @Override
    protected void enqueue(ViewerQueuedEvent event){
        if(event.getType()== ViewerQueuedEvent.ViewerQueuedEventType.MESSAGE) {
            switch(((ViewMessage)event.getPayload()).getMessageType()){
                case FATAL_ERROR_MESSAGE:
                case EXECUTER_ERROR_MESSAGE:
                case FROM_SERVER_ERROR:
                case FROM_SERVER_MESSAGE:
                case WIN_MESSAGE:
                case LOOSE_MESSAGE:
                    GUIMessageDisplayer.displayMessage(((ViewMessage)event.getPayload()).getPayload(), ((ViewMessage)event.getPayload()).getMessageType());
                    break;
            }
            if(View.debugging)
                System.out.println("[GUI MESSAGE]\t" + ((ViewMessage) event.getPayload()).getMessageType() + "\t" + ((ViewMessage) event.getPayload()).getPayload());
        } else {
            super.enqueue(event);
            SwingUtilities.invokeLater(() -> {
                try {
                    executeQueuedEvent();
                }catch (Exception e){
                    if(View.debugging){
                        System.err.println("In GUI Viewer Swinng thread");
                        e.printStackTrace();
                    }

                }
            });
        }
    }


    /**
     * Method that is called to serve an Event from the queue.
     */
    private void executeQueuedEvent(){
        ViewerQueuedEvent queued;
        try {
            queued = getNextEvent();
        } catch (EmptyQueueException e) {
            return;
        }

        if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.EXIT) {
            window.setVisible(false);
            exit();
        }
        if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS) setStatus(queued);
        if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.CARDSELECTION)
            if (ViewStatus.getActual().equals(ViewStatus.GAME_PREPARATION)){
                GUISelectCard.attivate((CardSelection) queued.getPayload());
            }
        if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.REFRESH) {
            if(ViewBoard.getBoard() != null)
                ViewBoard.getBoard().toGUI();
            SwingUtilities.updateComponentTreeUI(window);
        }
        if(queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.SET_SUBTURN){
            if(((SubTurnViewer)queued.getPayload()).toGUI()!=null)
                actualStatus.setSubTurn(((SubTurnViewer)queued.getPayload()).toGUI());
        }
    }


    /**
     * Constructor.
     */
    public GUIViewer(){
        Viewer.registerViewer(this);
    }

    public void setJPanel(JPanel panel){
        if(panel == null){
            window.setVisible(false);
        }else{
            window.getContentPane().removeAll();
            window.setContentPane(panel);
            window.setVisible(true);
            window.repaint();
        }
    }

    /**
     * Method that will be called to set a new <code>StatusViewer</code> entailed into the queued Event passed.
     * It should be called only after retrieving the queued element from the queue.
     *
     * @param queued (<code>ViewerQueuedEvent</code> that is of type SET_STATUS and has as payload the new ViewStatus to be set).
     */
    private void setStatus(ViewerQueuedEvent queued){
        if(actualStatus != null)
            actualStatus.onClose();
        actualStatus = ((StatusViewer)queued.getPayload()).toGUI();

        if(actualStatus == null){
            //window.setVisible(false);
            return;
        }
        actualStatus.setMyGUIViewer(this);


        actualStatus.execute();
        if(actualStatus.setFrameTitle()){
            window.setTitle(actualStatus.getTitle());
        }
    }

    /**
     * Method called if we want to launch the GUIViewer on a separate thread.
     */
    @Override
    public void run() {
        init();
        /*bodyExecute();
        end();*/
    }

    /**
     * Method that will iterate forever into a while loop serving all the events available on the queue and waiting for new when it has finished.
     */
    private void bodyExecute(){
        while(true){
            waitNextEvent();
            executeQueuedEvent();
        }
    }

    /**
     * Method that ends the execution of the GUIViewer.
     * It's used for the thread-like GUIViewer, otherwise there is no need to specify a particular method, it's just a matter to call <code>Viewer.exitAll();</code> and the program will terminate correctly.
     */
    private void end(){
        window.setVisible(false);
        Viewer.exitAll();
    }


    /**
     * Method to initialise the GUIViewer and set visible the JFrame.
     */
    private void init(){
        window = new JFrame("SANTORINI");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { Viewer.exitAll(); }
        });
        window.setSize(1024, 512);
        try {
            window.setIconImage(ImageIO.read(getClass().getResource("/img/icon/title_islan.png")));
        } catch (IOException ignore) {  }
    }
}
