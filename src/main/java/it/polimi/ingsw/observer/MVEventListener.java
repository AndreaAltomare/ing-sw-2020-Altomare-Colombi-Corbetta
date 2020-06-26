package it.polimi.ingsw.observer;

import it.polimi.ingsw.controller.events.*;

/**
 * Listener interface for Controller-generated events.
 *
 * MVEvent (Events form Server)
 *
 * @author Giorgio Corbetta
 */
public interface MVEventListener {
    /* Server general listener */

    /**
     * Updates listener that the next
     * Game status was reached.
     *
     * @param nextStatus Event: Next Game status reached
     */
    public void update(NextStatusEvent nextStatus);

    /**
     * Updates listener with
     * important Game info.
     *
     * @param serverSentData Event: Game info
     */
    public void update(ServerSendDataEvent serverSentData); // Game match general data (to respond to viewRequestData, receive data from Server)

    /**
     * Updates listener informing it that
     * the submitted nickname is invalid.
     *
     * @param invalidNickname Event: Submitted nickname invalid
     */
    public void update(InvalidNicknameEvent invalidNickname);

    /**
     * Updates listener asking for the number
     * of players for the upcoming Game.
     *
     * @param requirePlayersNumber Event: Require players number
     */
    public void update(RequirePlayersNumberEvent requirePlayersNumber);

    /**
     * Updates listener informing it
     * that the game lobby is full.
     *
     * @param lobbyFull Event: Game lobby is full
     */
    public void update(LobbyFullEvent lobbyFull);

    /**
     * Updates listener about the winning of a Player.
     *
     * @param playerWin Event: A Player won the game
     */
    public void update(PlayerWinEvent playerWin);

    /**
     * Updates listener about the loss of a Player.
     *
     * @param playerLose Event: A Player lost the game
     */
    public void update(PlayerLoseEvent playerLose);

    /**
     * Updates listener about the Turn change of a Player.
     *
     * @param turnStatusChange Event: Player's Turn status was changed
     */
    public void update(TurnStatusChangedEvent turnStatusChange);

    /**
     * Updates listener informing it that the Game is over.
     *
     * @param gameOver Event: Game is over
     */
    public void update(GameOverEvent gameOver);

    /**
     * Updates listener about an ongoing Game
     * (previously saved) that can be resumed.
     *
     * @param gameResuming Event: An ongoing Game can be resumed
     */
    public void update(GameResumingEvent gameResuming);

    /**
     * Updates listener informing it that Server
     * is quitting the connection with its Client.
     *
     * @param serverQuit Event: Server is quitting the connection.
     */
    public void update(ServerQuitEvent serverQuit);

    /* Game preparation listener */

    /**
     * Updates listener about the placement of a Worker.
     *
     * @param workerPlaced Event: A Worker was placed on the Board
     */
    public void update(WorkerPlacedEvent workerPlaced);

    /**
     * Updates listener about the selection
     * of a Card performed by a Player.
     *
     * @param cardSelected Event: A Player selected a Card
     */
    public void update(CardSelectedEvent cardSelected);

    /**
     * Updates listener with Cards information.
     *
     * @param cardsInformation Event: Possible Cards information
     */
    public void update(CardsInformationEvent cardsInformation);

    /**
     * Updates listener asking for the
     * Start Player for the upcoming game.
     *
     * @param requireStartPlayer Event: Require Start Player
     */
    public void update(RequireStartPlayerEvent requireStartPlayer);

    /**
     * Updates listener asking for the Player to place his/her own Worker.
     *
     * @param requirePlaceWorkers Event: Player needs to place Workers
     */
    public void update(RequirePlaceWorkersEvent requirePlaceWorkers);

    /* Move executed listener */

    /**
     * Updates listener about the Movement of a Worker.
     *
     * @param workerMoved Event: A Worker was moved
     */
    public void update(WorkerMovedEvent workerMoved);

    /**
     * Updates listener about the Construction of a block.
     *
     * @param blockBuilt Event: A block was built
     */
    public void update(BlockBuiltEvent blockBuilt);

    /**
     * Updates listener about the removal of a Worker.
     *
     * @param workerRemoved Event: A Worker was removed
     */
    public void update(WorkerRemovedEvent workerRemoved);

    /**
     * Updates listener about the removal of a block.
     *
     * @param blockRemoved Event: A block was removed
     */
    public void update(BlockRemovedEvent blockRemoved);

    /**
     * Updates listener informing that a Worker was selected.
     *
     * @param workerSelected Event: A Worker was selected
     */
    public void update(WorkerSelectedEvent workerSelected);

    /**
     * Updates listener informing that
     * the last performed move was undone.
     *
     * @param undoOk Event: Last performed move was undone. UndoOkEvent gives data to restore the last consistent state.
     */
    public void update(UndoOkEvent undoOk);

    /* Message listener */

    /**
     * Updates listener with a Message
     * generated by the Controller.
     *
     * @param message Event: Message generated by the Controller
     */
    public void update(MessageEvent message);

    /* Error message listener */

    /**
     * Updates listener with an Error message
     * generated by the Controller.
     *
     * @param message Event: Error message generated by the Controller
     */
    public void update(ErrorMessageEvent message);

    /* Generic update method */

    /**
     * Updates listener with a (generic) unspecified Event.
     *
     * @param o Generic and unspecified Event
     */
    public void update(Object o);
}
