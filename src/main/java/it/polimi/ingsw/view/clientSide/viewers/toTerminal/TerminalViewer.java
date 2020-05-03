package it.polimi.ingsw.view.clientSide.viewers.toTerminal;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;

public class TerminalViewer extends Viewer {
    @Override
    public void start() {  }

    @Override
    public void refresh() {  }

    @Override
    public void setStatusViewer(StatusViewer statusViewer) { statusViewer.toTerminal().print(); }

    @Override
    public void setSubTurnViewer(SubTurnViewer subTurnViewer) {  }

    public TerminalViewer(){
        Viewer.registerViewer(this);
    }
}
