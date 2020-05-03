package it.polimi.ingsw.view.clientSide.viewers.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;

/**
 * Interface used in the ApplicationStatus to return the various viewer for CLI, GUI and Terminal
 */
public interface StatusViewer {

    TerminalStatusViewer toTerminal();

    GUIStatusViewer toGUI();

    CLIStatusViewer toCLI();
}
