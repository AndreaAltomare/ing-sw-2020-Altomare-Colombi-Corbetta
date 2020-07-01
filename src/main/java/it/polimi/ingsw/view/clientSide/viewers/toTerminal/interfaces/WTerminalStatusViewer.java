package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;

/**
 * Abstract class for all the WTerminalStatus with methods to set and get the <code>WTerminalSubTurnViewer</code>.
 * It implements <code>SpecificStatusViewer</code>
 *
 * @author Marco
 */
public abstract class WTerminalStatusViewer implements SpecificStatusViewer {

    private WTerminalSubTurnViewer myWTerminalSubTurnViewer = null;

    /**
     * Methods which sets the myWTerminalSubTurnViewer with the <code>WTerminalSubTurnViewer</code> chosen
     *
     * @param myWTerminalSubTurnViewer <code>WTerminalSubTurnViewer</code> to set
     */
    public void setMyWTerminalSubTurnViewer(WTerminalSubTurnViewer myWTerminalSubTurnViewer) {
        this.myWTerminalSubTurnViewer = myWTerminalSubTurnViewer;
    }

    /**
     * Methods which return the myWTerminalSubTurnViewer
     *
     * @return <code>WTerminaSubTurnViewer</code> of this class
     */
    public WTerminalSubTurnViewer getMyWTerminalSubTurnViewer() {
        return myWTerminalSubTurnViewer;
    }

    /**
     * Abstract method used by sub classes to menage the their status
     */
    public abstract void show();

}
