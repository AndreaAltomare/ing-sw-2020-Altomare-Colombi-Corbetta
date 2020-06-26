package it.polimi.ingsw.observer;

import it.polimi.ingsw.view.events.*;

/**
 * Listener interface for View-generated events.
 *
 * VCEvent (Events form Client)
 *
 * @author AndreaAltomare
 */
public interface VCEventListener {
    /* Client general listener */

    /**
     * Updates listener with a submitted nickname.
     *
     * @param submittedNickname Event: A nickname was submitted
     */
    public void update(SetNicknameEvent submittedNickname); // handled by Server connection first

    /**
     * Updates listener with a number of Players for the upcoming game.
     *
     * @param playersNumber Event: A number of Players was submitted
     */
    public void update(SetPlayersNumberEvent playersNumber); // handled by Server connection first

    /**
     * Updates listener with a response about the resuming of the Game.
     *
     * @param gameResumingResponse Event: Response on the game resuming
     * @param playerNickname Player who generated the event
     */
    public void update(GameResumingResponseEvent gameResumingResponse, String playerNickname);

    /**
     * Updates listener about a Player quit.
     *
     * @param quit Event: A Player quit the game
     * @param playerNickname Player who generated the event
     */
    public void update(QuitEvent quit, String playerNickname);

    /**
     * Updates listener with a data request from the Client.
     *
     * @param dataRequest Event: Data request from the Client
     * @param playerNickname Player who generated the event
     */
    public void update(ViewRequestDataEvent dataRequest, String playerNickname); // Game match general data

    /* Game preparation listener */

    /**
     * Updates listener with a Card selection by a Player.
     *
     * @param card Event: Card selection
     * @param playerNickname Player selected the Card
     */
    public void update(CardSelectionEvent card, String playerNickname);

    /**
     * Updates listener with a Card choosing by the Challenger Player.
     *
     * @param chosenCards Event: Chosen Card
     * @param playerNickname Player chose the Cards
     */
    public void update(CardsChoosingEvent chosenCards, String playerNickname);

    /**
     * Updates listener about the chose of Start Player by The Challenger.
     *
     * @param startPlayer Event: Start Player was chosen
     * @param playerNickname Player chose the Start Player
     */
    public void update(SetStartPlayerEvent startPlayer, String playerNickname);

    /**
     * Updates listener with a Worker placement.
     *
     * @param workerToPlace Event: Worker placement
     * @param playerNickname Player who placed the Workers
     */
    public void update(PlaceWorkerEvent workerToPlace, String playerNickname);

    /* Move listener */

    /**
     * Updates listener about the selection of a Worker.
     *
     * @param selectedWorker Event: Worker selection
     * @param playerNickname Player who made the move
     */
    public void update(SelectWorkerEvent selectedWorker, String playerNickname);

    /**
     * Updates listener about the movement of a Worker.
     *
     * @param move Event: Worker movement
     * @param playerNickname Player who made the move
     */
    public void update(MoveWorkerEvent move, String playerNickname);

    /**
     * Updates listener about the construction of a block.
     *
     * @param build Event: Block construction
     * @param playerNickname Player who made the move
     */
    public void update(BuildBlockEvent build, String playerNickname);

    /**
     * Updates listener about the removal of a Worker.
     *
     * @param workerToRemove Event: Worker removal
     * @param playerNickname Player who made the move
     */
    public void update(RemoveWorkerEvent workerToRemove, String playerNickname);

    /**
     * Updates listener about the removal of a block.
     *
     * @param blockToRemove Event: Block removal
     * @param playerNickname Player who made the move
     */
    public void update(RemoveBlockEvent blockToRemove, String playerNickname);

    /**
     * Updates listener with a request of action-undo from a Player.
     *
     * @param undoAction Event: Undo action
     * @param playerNickname Player who requested the Undo
     */
    public void update(UndoActionEvent undoAction, String playerNickname);

    /* Turn status change listener */

    /**
     * Updates listener with a request of Turn status change.
     *
     * @param turnStatus Event: Turn status change request
     * @param playerNickname Player who requested the Turn change
     */
    public void update(TurnStatusChangeEvent turnStatus, String playerNickname);

    /* Generic update method */

    /**
     * Updates listener with a (generic) unspecified Event.
     *
     * @param o Generic and unspecified Event
     */
    public void update(Object o);
}
