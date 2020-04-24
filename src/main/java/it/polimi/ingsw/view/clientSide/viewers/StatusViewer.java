package it.polimi.ingsw.view.clientSide.viewers;

/**
 * Interface used in the ApplicationStatus to return the various viewer for CLI, GUI and Terminal
 */
public interface StatusViewer {

    Object toGUI();

    Object toCLI();
}
