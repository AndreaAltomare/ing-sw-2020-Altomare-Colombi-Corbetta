package it.polimi.ingsw.model;

import it.polimi.ingsw.model.persistence.GameState;
// TODO: Maybe it's to remove (useless)
public class GameStateHandler {
    Model model; // model instance
    GameState gameState;

    public GameStateHandler(Model model) {
        this.model = model;
        this.gameState = new GameState();
        // todo write operation to retrieve game state from ResourceManager
    }




}
