package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.FileManager;

/**
 * Class representing a Player
 *
 * @author AndreaAltomare
 */
public class Player {
    private String nickname;
    private boolean challenger;
    private boolean startingPlayer;
    private Card card;
    private TurnManager turn;
    private MovementManager movementState;
    private ConstructionManager constructionState;

    public Player(String nickname) {
        this.nickname = nickname;
        this.challenger = false;
        this.startingPlayer = false;
        this.card = null;
        this.movementState = null;
        this.constructionState = null;
        this.turn = movementState; // initial turn State
    }

    /**
     * Execute a move within a specific Turn
     * (HEART OF MOVE EXECUTION)
     *
     * @param move
     * @param worker
     * @throws WinException (Exception handled by Controller)
     * @throws LoseException (Exception handled by Controller)
     * @throws RunOutMovesException (Exception handled by Controller)
     */
    public void executeMove(Move move,Worker worker) throws WinException,LoseException,RunOutMovesException,BuildBeforeMoveException {
        turn.handle(move, worker);
    }

    /**
     * This method is called when a Player's Turn starts.
     * It sets the initial conditions both for the Turn
     * and for its Move Managers.
     */
    public void startTurn() {
        /* 1- Reset Player's Move Manager values */
        card.resetForStart();

        /* 2- Get to the initial State */
        chooseState(StateType.MOVEMENT);
    }

    /**
     * This method is intended for Players usage
     * to choose what move (generally) they want to perform
     * (HEART OF STATE TURN FLOW)
     *
     * This method shall be called by the Controller
     *
     * @param state (Chosen State type)
     */
    public void chooseState(StateType state) {
        switch(state) {
            case MOVEMENT:
                this.switchState(movementState);
                break;
            case CONSTRUCTION:
                this.switchState(constructionState);
                break;
            default:
                break;
        }
    }

    public void switchState(TurnManager nextState) {
        this.turn = nextState;
    }

    /**
     * Instantiate a new Card and initialize Turn Managers
     *
     * @param cardName (Chosen Card)
     */
    public void chooseCard(String cardName) {
        FileManager fileManager = FileManager.getIstance();
        String json = fileManager.getCard(cardName);

        /* JSON DESERIALIZATION WITH GSON */
        Gson gson = new Gson();
        GodPower godPower = gson.fromJson(json, GodPower.class);

        /* Card initialization */
        this.card = new Card(godPower);
        this.movementState = new MovementManager(card);
        this.constructionState = new ConstructionManager(card);
        this.turn = movementState;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isChallenger() {
        return challenger;
    }

    public boolean isStartingPlayer() {
        return startingPlayer;
    }

    public void setChallenger(boolean challenger) {
        this.challenger = challenger;
    }

    public void setStartingPlayer(boolean startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public Card getCard() {
        return card;
    }

    public MovementManager getMovementManager() {
        return movementState;
    }

    public ConstructionManager getConstructionManager() {
        return constructionState;
    }
}
