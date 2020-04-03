package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ConstructionManager extends TurnManager {

    public ConstructionManager(int initialMoves) {
        observers = new ArrayList<>();
        this.movesLeft = initialMoves; // CONSTRUCTION moves left
    }

    @Override
    public void handle() {
        buildBlock();
    }

    @Override
    public int getMovesLeft() {
        return movesLeft;
    }

    private void buildBlock() {
        // TODO: add statements to build a block (or a dome)
    }
}
