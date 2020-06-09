package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.PlayingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;

public class WTerminalPlayingViewer extends WTerminalStatusViewer {

    private final ViewStatus viewStatus = ViewStatus.PLAYING;
    private boolean move; // true if the player had moved
    private boolean build; // true if the player had build after a movement

    private PlayingViewer playingViewer;

    public WTerminalPlayingViewer(PlayingViewer playingViewer) {
        this.playingViewer = playingViewer;
    }

    /**
     * Returns the value of move
     * @return
     */
    public boolean isMove() {
        return move;
    }

    /**
     * Returns the value of build
     * @return
     */
    public boolean isBuild() {
        return build;
    }

    /**
     * Sets move == true
     */
    public void setMoveTrue() {
        this.move = true;
    }

    /**
     * Sets build == move only if move == true
     */
    public void setBuildAfterMoveTrue() {
        if ( this.move ) {
            build = true;
        }
    }

    /**
     * Sets move == false and build == false
     */
    public void resetValue() {
        this.move = false;
        this.build = false;
    }

    /**
     * Uses the super same method and then sets myCLISubTurnViewer's myCLIStatusViewer with itself
     * @param myWTerminalSubTurnViewer
     */
    @Override
    public void setMyWTerminalSubTurnViewer(WTerminalSubTurnViewer myWTerminalSubTurnViewer) {
        super.setMyWTerminalSubTurnViewer(myWTerminalSubTurnViewer);
        myWTerminalSubTurnViewer.setMyWTerminalStatusViewer( this );
    }

    @Override
    public ViewStatus getViewStatus() {
        return viewStatus;
    }

    @Override
    public void show() {
        if ( this.getMyWTerminalSubTurnViewer() != null) {
            this.getMyWTerminalSubTurnViewer().show();
        }
    }
}
