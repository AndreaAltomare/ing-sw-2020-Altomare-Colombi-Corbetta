package it.polimi.ingsw.model;

public class GameStateHandler {
    Model model; // model instance
    GameState gameState;

    public GameStateHandler(Model model) {
        this.model = model;
        this.gameState = new GameState();
        // todo write operation to retrieve game state from ResourceManager
    }




}
