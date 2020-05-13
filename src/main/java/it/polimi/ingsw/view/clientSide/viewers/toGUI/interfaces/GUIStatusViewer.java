package it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.GUIViewer;
import it.polimi.ingsw.view.exceptions.CheckQueueException;

import javax.swing.*;

public abstract class GUIStatusViewer implements SpecificStatusViewer {

    protected GUIViewer guiViewer;

    public void setMyGUIViewer (GUIViewer viewer){ guiViewer = viewer; }

    public boolean hasJPanel(){ return false; }

    public JPanel getJPanel(){ return null; }

    public boolean hasPopup(){ return false;}

    public void doPopUp(){  }

    public boolean hasDirectFrameManipulation(){ return false; }

    public void directFrameManipulation(){  }

    public void execute() throws CheckQueueException {
        if(hasJPanel()) guiViewer.setJPanel(getJPanel());
        if(hasDirectFrameManipulation()) directFrameManipulation();
        if(hasPopup()) this.doPopUp();
    }
}
