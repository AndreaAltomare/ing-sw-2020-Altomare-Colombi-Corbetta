package it.polimi.ingsw.view;
// TODO: write some comments to explains the meaning of every status defined
/**
 * Enumeration type for representing
 * different status by which manage
 * the Client behavior.
 */
public enum ApplicationStatus {
    READY,
    LOGIN,
    WAITING,
    NEW_GAME, // (new partita)
    GAME_PREPARATION, // (posizionamento Worker, etc...)
    PLAYING, // (turni) ==> per i sotto turni, usare il tipo Enum StateType (dal Model)
    GAME_OVER // (fine partita)
}
