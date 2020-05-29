package it.polimi.ingsw.model;

/**
 * Concrete class for the Observer of a Player's Movement state.
 *
 * @author AndreaAltomare
 */
public class MovementObserver extends TurnObserver {
    private AdversaryMove adversaryMoveObserver; // adversary move observer

    public MovementObserver(AdversaryMove adversaryMoveObserver) {
        this.adversaryMoveObserver = adversaryMoveObserver;
    }

    /**
     * Calls the Opponent's check method for the Move made by the Player.
     *
     * @param move (Move to be checked by the Opponent's observer)
     * @param worker (Worker who made the move)
     * @throws DeniedMoveException (Exception handled by Controller)
     * @throws LoseException (Exception handled by Controller)
     */
    @Override
    public void check(Move move, Worker worker) throws DeniedMoveException,LoseException {
        if(adversaryMoveObserver.checkMove(move, worker) == false)
            throw new DeniedMoveException("Move denied by other Player's Card!");
    }

    public AdversaryMove getAdversaryMoveObserver() {
        return adversaryMoveObserver;
    }
}
