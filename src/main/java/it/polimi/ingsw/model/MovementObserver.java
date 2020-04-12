package it.polimi.ingsw.model;

public class MovementObserver extends TurnObserver {
    private AdversaryMove adversaryMoveObserver; // adversary move observer

    public MovementObserver(AdversaryMove adversaryMoveObserver) {
        this.adversaryMoveObserver = adversaryMoveObserver;
    }

    @Override
    public void check(Move move, Worker worker) throws DeniedMoveException,LoseException {
        if(adversaryMoveObserver.checkMove(move, worker) == false)
            throw new DeniedMoveException("Move denied by other Player's Card!");
    }
}
