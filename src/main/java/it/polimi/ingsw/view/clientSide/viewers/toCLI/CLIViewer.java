package it.polimi.ingsw.view.clientSide.viewers.toCLI;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;

public class CLIViewer extends Viewer {

    public CLIViewer(){
        Viewer.registerViewer(this);
    }

    @Override
    public void start() {

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
}
