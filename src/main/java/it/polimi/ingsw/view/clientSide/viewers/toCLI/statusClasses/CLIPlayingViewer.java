package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.PlayingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;

public class CLIPlayingViewer extends CLIStatusViewer {

    private final ViewStatus viewStatus = ViewStatus.PLAYING;
    private boolean move; // true if the player had moved
    private boolean build; // true if the player had build after a movement

    private PlayingViewer playingViewer;

    public CLIPlayingViewer( PlayingViewer playingViewer) {
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
    public void setBuildTrue() {
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

    @Override
    public ViewStatus getViewStatus() {
        return viewStatus;
    }

    @Override
    public void show() {
        if ( this.getMyCLISubTurnViewer() != null) {
            this.getMyCLISubTurnViewer().show();
        }
    }
}
