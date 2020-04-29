package it.polimi.ingsw.controller.interfaces;

import it.polimi.ingsw.observer.GeneralListener;
import it.polimi.ingsw.view.events.QuitEvent;
import it.polimi.ingsw.view.events.SetNicknameEvent;
import it.polimi.ingsw.view.events.ViewRequestDataEvent;

/**
 * Interface for general information exchange (from View [Client]).
 *
 * VCEvent (Events form Client)
 *
 * @author AndreaAltomare
 */
public interface ClientGeneralListener extends GeneralListener {
    public void onNicknameSubmit(SetNicknameEvent submittedNickname); // handled by Server connection first
    public void onPlayerQuit(QuitEvent quit, String playerNickname);
    public void viewRequestData(ViewRequestDataEvent dataRequest, String playerNickname); // Game match general data
}
