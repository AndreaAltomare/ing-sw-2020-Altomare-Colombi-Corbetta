package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.FileManager;

import java.util.ArrayList;
import java.util.List;

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
    private List<Worker> workers;

    public Player(String nickname) {
        this.nickname = nickname;
        this.challenger = false;
        this.startingPlayer = false;
        this.card = null;
        this.movementState = null;
        this.constructionState = null;
        this.turn = movementState; // initial turn State
        this.workers = new ArrayList<>();
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
    public void executeMove(Move move,Worker worker) throws WinException,LoseException,RunOutMovesException,BuildBeforeMoveException,WrongWorkerException {
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

        /* 2- Reset Workers Turn values */
        workers.forEach(x -> x.setChosen(ChooseType.CAN_BE_CHOSEN));

        /* 3- Get to the initial State */
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

    /**
     * Method called when a Worker needs to be register among
     * a Player's Workers List
     *
     * @param worker
     */
    public void registerWorker(Worker worker) {
        workers.add(worker);
    }

    //TODO: if a Worker is in a Lose condition, don't let the Player to select it

    /**
     * This method lets the Player choose a Worker and to set
     * it to the chosen one for this Turn
     *
     * @param worker (Worker to choose)
     * @return (Worker has been correctly chosen ? true : false)
     */
    public boolean chooseWorker(Worker worker) {
        /* 1- Check if the selected Worker belongs to the Player (by nickname, because it's unique among Players) */
        if(!worker.getOwner().getNickname().equals(this.nickname))
            return false;

        if(worker.getChosenStatus() == ChooseType.CAN_BE_CHOSEN || worker.isChosen()) {
            /* 1- Set the Worker status to CHOSEN */
            worker.setChosen(ChooseType.CHOSEN);

            /* 2- Other Player's Workers status must be set to NOT_CHOSEN */
            for (Worker workerObj : workers)
                if (!workerObj.isChosen())
                    workerObj.setChosen(ChooseType.NOT_CHOSEN);

            return true;
        }

        return false;
    }
}
