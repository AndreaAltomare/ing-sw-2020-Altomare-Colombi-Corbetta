package it.polimi.ingsw.model;

import java.util.ArrayList;

public class MovementManager extends TurnManager {

    public MovementManager(int initialMoves) {
        observers = new ArrayList<>();
        this.movesLeft = initialMoves; // MOVEMENT moves left
    }

    @Override
    public void handle() {
        moveWorker();
    }

    @Override
    public int getMovesLeft() {
        return movesLeft;
    }

    private void moveWorker() {
        // TODO: add statements to make a Worker move
    }
}
