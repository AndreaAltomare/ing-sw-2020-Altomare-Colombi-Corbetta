package it.polimi.ingsw.model.player.worker;

/**
 * Enumeration type to tell whether a Worker
 * can either be chosen for a turn or not,
 * or it has already been chosen.
 *
 * @author AndreaAltomare
 */
public enum ChooseType {
    CHOSEN,
    NOT_CHOSEN,
    CAN_BE_CHOSEN
}
