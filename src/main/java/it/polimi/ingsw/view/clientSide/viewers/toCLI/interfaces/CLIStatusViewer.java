package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;

/**
 * Abstract class for all the CLIStatus with methods to set and get the <code>CLISubTurnViewer</code>.
 * It implements <code>SpecificStatusViewer</code>
 *
 * @author Marco
 */
public abstract class CLIStatusViewer implements SpecificStatusViewer {

    private CLISubTurnViewer myCLISubTurnViewer = null;

    /**
     * Methods which sets the myCLISubTurnViewer with the <code>CLISubTurnViewer</code> chosen
     *
     * @param myCLISubTurnViewer <code>CLISubTurnViewer</code> to set
     */
    public void setMyCLISubTurnViewer(CLISubTurnViewer myCLISubTurnViewer) {
        this.myCLISubTurnViewer = myCLISubTurnViewer;
    }

    /**
     * Methods which return the myCLISubTurnViewer
     *
     * @return <code>CliSubTurnViewer</code> of this class
     */
    public CLISubTurnViewer getMyCLISubTurnViewer() {
        return myCLISubTurnViewer;
    }

    /**
     * Abstract method used by sub classes to menage the their status
     */
    public abstract void show();
}
