package it.polimi.ingsw.view.clientSide.viewers.toGUI;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;

public class GUIViewer extends Viewer {

    public GUIViewer(){
        Viewer.registerViewer(this);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void setStatusViewer(StatusViewer statusViewer) {

    }

    @Override
    public void setSubTurnViewer(SubTurnViewer subTurnViewer) {

    }

    @Override
    public void run() {

    }
}
