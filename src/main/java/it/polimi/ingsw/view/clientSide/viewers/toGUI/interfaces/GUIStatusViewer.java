package it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.GUIViewer;

import javax.swing.*;

/**
 * Abstract class implemented by classes representing thee various <code>ViewStatus</code>.
 */
public abstract class GUIStatusViewer implements SpecificStatusViewer {

    private GUIViewer guiViewer;

    /**
     * Method that set the <code>GUIViewer</code> to which this refers.
     *
     * @param viewer (the <code>GUIViewer</code> to which this refers).
     */
    public void setMyGUIViewer (GUIViewer viewer){
        guiViewer = viewer;
    }

    /**
     * Method that checks if this has a <code>JPanel</code> to be shown.
     *
     * @return (true iif this has a <code>JPanel</code> to be shown).
     */
    public boolean hasJPanel(){
        return false;
    }

    /**
     * Method that returns the <code>SubPanel</code> to be shown for this.
     *
     * @return (the <code>SubPanel</code> to be shown for this).
     */
    public JPanel getJPanel(){
        return null;
    }

    /**
     * Method that checks if this has a popUp to be shown.
     *
     * @return (true iif this has a popUp to be shown).
     */
    public boolean hasPopup(){
        return false;
    }


    /**
     * Method executed if hasExecuted == true and it's
     */
    public void doPopUp(){

    }

    /**
     * Method that returns true if this needs to directly manipulate the Frame.
     *
     * @return (true iif needs to directly manipulate the Frame).
     */
    private boolean hasDirectFrameManipulation(){
        return false;
    }

    /**
     * Method executed for the direcct FrameManipulation iif hasDirectFrameManipulation()==true.
     */
    private void directFrameManipulation(){

    }

    /**
     * setter method for the subTurnViewer of this.
     *
     * @param subTurnViewer (<code>subTurnViewer</code> that this refers to).
     */
    public void setSubTurn(GUISubTurnViewer subTurnViewer){

    }

    /**
     * Method called when a new status needs to be set.
     * It checks that if  hasJPanel() set the getJPanel();
     * It checks that if  hasDirectFrameManipulation() set the directFrameManipulation();
     * It checks that if  hasPopup() set the doPopUp();
     * then it does the onLoad()
     */
    public final void execute() {
        if(hasJPanel()) {
            guiViewer.setJPanel(getJPanel());
        }
        if(hasDirectFrameManipulation())
            directFrameManipulation();
        if(hasPopup())
            this.doPopUp();
        onLoad();
    }

    /**
     * method that is executed on the loading of the Status.
     */
    public void onLoad(){

    }

    /**
     * method that is executed on the closing of the Status.
     */
    public void onClose(){

    }

    /**
     * Method that returns true if this needs to set the frame title.
     *
     * @return (true iif needs to directly manipulate the Frame).
     */
    public boolean setFrameTitle(){
        return false;
    }

    /**
     * Method that returns the title to be set on the frame -if setFrameTitle()==true-.
     *
     * @return (the title of the frame to be set).
     */
    public String getTitle(){
        return "SANTORINI";
    }
}
