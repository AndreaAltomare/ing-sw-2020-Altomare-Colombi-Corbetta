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
    public void update(SetNicknameEvent submittedNickname); // handled by Server connection first
    public void update(SetPlayersNumberEvent playersNumber); // handled by Server connection first
    public void update(GameResumingResponseEvent gameResumingResponse, String playerNickname);
    public void update(QuitEvent quit, String playerNickname);
    public void update(ViewRequestDataEvent dataRequest, String playerNickname); // Game match general data

    /* Game preparation listener */
    public void update(CardSelectionEvent card, String playerNickname);
    public void update(CardsChoosingEvent chosenCards, String playerNickname);
    public void update(SetStartPlayerEvent startPlayer, String playerNickname);
    public void update(PlaceWorkerEvent workerToPlace, String playerNickname);

    /* Move listener */
    public void update(SelectWorkerEvent selectedWorker, String playerNickname);
    public void update(MoveWorkerEvent move, String playerNickname);
    public void update(BuildBlockEvent build, String playerNickname);
    public void update(RemoveWorkerEvent workerToRemove, String playerNickname);
    public void update(RemoveBlockEvent blockToRemove, String playerNickname);

    /* Turn status change listener */
    public void update(TurnStatusChangeEvent turnStatus, String playerNickname);

    /* Generic update method */
    public void update(Object o);
}
