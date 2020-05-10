package it.polimi.ingsw.view.clientSide;

import it.polimi.ingsw.controller.events.NextStatusEvent;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.GUIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.TerminalViewer;

public class ViewTester {
    public static void main(String[] args) {
        View view = new View(null, null);

        new TerminalViewer().start();
        new CLIViewer();
        new GUIViewer();
        System.out.println("Hello World");

        ViewStatus.init();
        view.update(new NextStatusEvent("pippo"));
        /*Viewer.setAllStatusViewer(ViewStatus.getActual().getViewer());


        Viewer.setAllStatusViewer(ViewStatus.getActual().getViewer());*/
    }
}
