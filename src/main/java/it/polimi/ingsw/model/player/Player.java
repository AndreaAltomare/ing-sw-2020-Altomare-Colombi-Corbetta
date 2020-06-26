package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardParser;
import it.polimi.ingsw.model.card.GodPower;
import it.polimi.ingsw.model.card.move.MyMove;
import it.polimi.ingsw.model.card.adversaryMove.AdversaryMoveChecker;
import it.polimi.ingsw.model.card.build.BuildChecker;
import it.polimi.ingsw.model.card.build.BuildExecutor;
import it.polimi.ingsw.model.card.move.MoveChecker;
import it.polimi.ingsw.model.card.move.MoveExecutor;
import it.polimi.ingsw.model.card.win.WinChecker;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.move.BuildMove;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.turn.ConstructionManager;
import it.polimi.ingsw.model.player.turn.MovementManager;
import it.polimi.ingsw.model.player.turn.StateType;
import it.polimi.ingsw.model.player.turn.TurnManager;
import it.polimi.ingsw.model.player.worker.ChooseType;
import it.polimi.ingsw.model.player.worker.Worker;
import it.polimi.ingsw.storage.ResourceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Player.
 *
 * @author AndreaAltomare
 */
public class Player {
    private String nickname;
    private boolean challenger;
    private boolean startingPlayer;
    private Card card;
    private StateType turnType;
    private TurnManager turn;
    private MovementManager movementState;
    private ConstructionManager constructionState;
    private List<Worker> workers;
    private boolean playing; // tells if the player is playing or not

    /**
     * Constructs a new {@code Player} object.
     *
     * @param nickname Player's nickname
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.challenger = false;
        this.startingPlayer = false;
        this.card = null;
        this.turnType = StateType.NONE;
        this.movementState = null;
        this.constructionState = null;
        this.turn = movementState; // initial turn State
        this.workers = new ArrayList<>();
        this.playing = false;
    }

    /**
     * Executes a move within a specific Turn
     * (HEART OF MOVE EXECUTION)
     *
     * @param move (Move to execute)
     * @param worker (Worker by which perform the Move)
     * @return Result of move execution
     * @throws WinException (Exception handled by Controller)
     * @throws LoseException (Exception handled by Controller)
     * @throws RunOutMovesException (Exception handled by Controller)
     * @throws BuildBeforeMoveException (Exception handled by Controller)
     * @throws WrongWorkerException (Exception handled by Controller)
     * @throws TurnOverException (Exception handled by Controller)
     * @throws TurnSwitchedException (Exception handled by Controller)
     */
    public boolean executeMove(Move move, Worker worker) throws WinException,LoseException,RunOutMovesException,BuildBeforeMoveException,WrongWorkerException,TurnOverException, TurnSwitchedException {
        boolean executionResult = false;

        try {
            executionResult = turn.handle(move, worker);
        }
        catch(TurnSwitchedException ex) {
            // Movement has been done. Now it's turn for the Construction move
            chooseState(StateType.CONSTRUCTION);
            throw ex;
        }

        return executionResult;
    }

    /**
     * This method is called when a Player's Turn starts.
     * It sets the initial conditions both for the Turn
     * and for its Move Managers.
     *
     * @throws LoseException (Exception handled by Controller)
     */
    public void startTurn() throws LoseException {
        /* 0- Reset Player's Move Manager values */
        card.resetForStart();

        /* 1- Reset Workers Turn values */
        workers.forEach(x -> x.setChosen(ChooseType.CAN_BE_CHOSEN));

        /* 2- Check if there is a Lose Condition */
        if(checkForLostByMovement())
            throw new LoseException(this, "Player " + nickname + "has lost! (Cannot perform any Movement)");

        /* 3- Get to the initial State */
        try {
            chooseState(StateType.MOVEMENT);
        }
        catch (TurnOverException ex) {
            System.err.println("An error occurred while starting " + nickname + "'s turn!");
        }

        /* 4- The Player is now playing */
        this.playing = true;
    }

    /**
     * End the Player's game turn.
     */
    public void endTurn() {
        this.playing = false;
    }

    /**
     * This method is intended for Players usage
     * to choose what move (generally) they want to perform.
     * (HEART OF STATE TURN FLOW)
     *
     * This method shall be called by the Controller
     *
     * @param state (Chosen State type)
     * @return (State-switching is admitted ? true : false)
     * @throws LoseException (Exception handled by Controller)
     * @throws TurnOverException (Exception handled by Controller)
     */
    public boolean chooseState(StateType state) throws LoseException,TurnOverException {
        boolean changeAdmitted = false;

        /* Do not check whether a Turn change is possible if the Player is already in that state */
        if(this.turnType == state)
            return true;

        switch(state) {
            case MOVEMENT:
                changeAdmitted = preliminaryCheck(state);
                if(changeAdmitted)
                    changeAdmitted = this.switchState(movementState);
                break;
            case CONSTRUCTION:
                changeAdmitted = preliminaryCheck(state);
                if(changeAdmitted)
                    changeAdmitted = this.switchState(constructionState);
                break;
            case NONE:
                changeAdmitted = checkIfTurnCompleted();
                if(changeAdmitted)
                    throw new TurnOverException("Turn passed!");
                break;
            default:
                break;
        }

        return changeAdmitted;
    }

    /**
     *
     * @return True iff the Player's Turn is completed and it can be passed
     */
    private boolean checkIfTurnCompleted() {
        return card.isTurnCompleted();
    }

    /**
     * Preliminary check to first understand if the switch is possible or not.
     *
     * @param state (Chosen State type)
     * @return (State-switching is admitted ? true : false)
     */
    private boolean preliminaryCheck(StateType state) {
        boolean canChangeTurn = false;

        switch(state) {
            case MOVEMENT:
                if(card.getMyMove().getMovesLeft() >= 1)
                    canChangeTurn = true;
                break;

            case CONSTRUCTION:
                if(card.getMyConstruction().getConstructionLeft() >= 1)
                    canChangeTurn = true;
                break;

            default:
                break;
        }

        return canChangeTurn;
    }

    /**
     * This method switches the States for the (abstract) FSM
     * that controls the Turn flow.
     *
     * @param nextState (Next FSM State to go in)
     * @return (State-switching is admitted ? true : false)
     * @throws LoseException (Exception handled by Controller)
     */
    public boolean switchState(TurnManager nextState) throws LoseException {
        /* Check if a Lose Condition (by denied movements) occurs */
        if(nextState.state() == StateType.MOVEMENT) {
            if (checkForLostByMovement())
                throw new LoseException(this, "Player " + nickname + "has lost! (Cannot perform any Movement)");
            else
                this.turnType = StateType.MOVEMENT;
        }

        /* Check if a Lose Condition (by denied constructions) occurs */
        if(nextState.state() == StateType.CONSTRUCTION) {
            if (checkForLostByConstruction())
                throw new LoseException(this, "Player " + nickname + "has lost! (Cannot perform any Construction)");
            else
                this.turnType = StateType.CONSTRUCTION;
        }

        this.turn = nextState;
        return true; // everything ok
    }


    /**
     * Instantiate a new Card and initialize Turn Managers
     *
     * @param cardName (Chosen Card)
     */
    public void chooseCard(String cardName) {
        GodPower godPower = ResourceManager.callGodPower(cardName);
        List<MoveChecker> moveCheckers = CardParser.getMoveCheckers(godPower);
        MoveExecutor moveExecutor = CardParser.getMoveExecutor(godPower);
        List<BuildChecker> buildCheckers = CardParser.getBuildCheckers(godPower);
        BuildExecutor buildExecutor = CardParser.getBuildExecutor(godPower);
        List<WinChecker> winCheckers = CardParser.getWinCheckers(godPower);
        List<AdversaryMoveChecker> adversaryMoveCheckers = CardParser.getAdversaryMoveCheckers(godPower);

        /* Card initialization */
        this.card = new Card(godPower, moveCheckers, moveExecutor, buildCheckers, buildExecutor, winCheckers, adversaryMoveCheckers);
        this.movementState = new MovementManager(card);
        this.constructionState = new ConstructionManager(card);
        this.turn = movementState;
    }

    /**
     * Method called when a Worker needs to be register among
     * a Player's Workers List.
     *
     * @param worker (Worker to be registered)
     */
    public void registerWorker(Worker worker) {
        workers.add(worker);
    }

    /**
     * Remove all owned Workers from
     * the Player's Worker List.
     */
    public void clearWorkers() {
        workers.clear();
    }

    /**
     * This method lets the Player choose a Worker and to set
     * it to the chosen one for this Turn.
     *
     * @param worker (Worker to choose)
     * @return (Worker has been correctly chosen ? true : false)
     */
    public boolean chooseWorker(Worker worker) {
        /* 0- if the Worker has already been chosen, do not change anything */
        if(worker.isChosen())
            return true;

        /* 1- if a Worker is in a Lose condition, don't let the Player to select it */
        /* In this case, just a Lose condition triggered by denied Movements is checked,
        * because conditions to make a Movement are more stringent than the ones to make
        * a Construction.
        */
        if(checkForWorkerLostByMovement(worker))
            return false;

        /* 2- Check if the selected Worker belongs to the Player (by nickname, because it's unique among Players) */
        if(!worker.getOwner().getNickname().equals(this.nickname))
            return false;

        if(worker.getChosenStatus() == ChooseType.CAN_BE_CHOSEN) {
            /* 2.1- Set the Worker status to CHOSEN */
            worker.setChosen(ChooseType.CHOSEN);

            /* 2.2- Other Player's Workers status must be set to NOT_CHOSEN */
            for (Worker workerObj : workers)
                if (!workerObj.isChosen())
                    workerObj.setChosen(ChooseType.NOT_CHOSEN);

            return true;
        }

        return false;
    }


    /**
     * This method tells whether a Player has lost the game
     * because he/she cannot perform any Movement.
     *
     * @return (Player cannot perform any Movement ? true : false)
     */
    private boolean checkForLostByMovement() {
        boolean workerHasLost = false;

        /* For each worker check if a Move can be performed */
        for(Worker workerObj : workers) {
            workerHasLost = checkForWorkerLostByMovement(workerObj);
            if (!workerHasLost)
                return false;

            if(card.hasExecutedConstruction() && workerObj.isChosen())
                return true; // no more actions possible within the same SELECTED worker.

            if(card.hasExecutedMovement() && !card.hasExecutedConstruction() && workerObj.isChosen())
                return false; // worker can still make a construction in this case
        }

        return true;
    }

    /**
     * This method tells whether a Player has lost the game
     * because he/she cannot perform any Construction.
     *
     * @return (Player cannot perform any Construction ? true : false)
     */
    private boolean checkForLostByConstruction() {

        /* For each worker check if a BuildMove can be performed */
        for(Worker workerObj : workers) {
            if (!checkForWorkerLostByConstruction(workerObj))
                return false;

            if(card.hasExecutedMovement() && workerObj.isChosen())
                return true; // no more actions possible within the same SELECTED worker.
        }

        return true;
    }

    /**
     * Given a Worker, this method tells if the Worker
     * is in a Lose because it cannot make any move.
     *
     * @param worker (Worker whose Lose condition has to be checked)
     * @return (Worker cannot make any Movement Move ? true : false)
     */
    private boolean checkForWorkerLostByMovement(Worker worker) {
        List<Cell> adjacentCells;

        /* 0- If the Worker is not the one already chosen, do not check */
        if(worker.getChosenStatus() == ChooseType.NOT_CHOSEN)
            return true;

        /* 1- Get adjacent Cells from the one the Worker is placed on */
        adjacentCells = worker.position().getBoard().getAdjacentCells(worker.position());

        /* 2- For each Cell, check if a Move is possible */
        for(Cell cell : adjacentCells) {
            Move moveToCheck = new Move(worker.position(),cell);

            // If at least one Movement is allowed, Player has not lost.
            if(card.getMyMove().checkMove(moveToCheck, worker))
                return false;
        }

        return true;
    }

    /**
     * Given a Worker, this method tells if the Worker
     * is in a Lose because it cannot make any Construction.
     *
     * @param worker (Worker whose Lose condition has to be checked)
     * @return (Worker cannot make any Build Move ? true : false)
     */
    private boolean checkForWorkerLostByConstruction(Worker worker) {
        List<Cell> adjacentCells;

        /* 0- If the Worker is not the one already chosen, do not check */
        if(worker.getChosenStatus() == ChooseType.NOT_CHOSEN)
            return true;

        /* 1- Get adjacent Cells from the one the Worker is placed on */
        adjacentCells = worker.position().getBoard().getAdjacentCells(worker.position());

        /* 2- For each Cell, check if a Construction is possible */
        for(Cell cell : adjacentCells) {
            BuildMove moveToCheck = new BuildMove(worker.position(),cell, PlaceableType.ANY);

            // If at least one Movement is allowed, Player has not lost.
            if(card.getMyConstruction().checkMove(moveToCheck, worker))
                return false;
        }

        return true;
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

    public List<Worker> getWorkers() {
        return workers;
    }

    /**
     * Given a Worker's unique identifier,
     * returns the related Worker's instance.
     *
     * @param workerId (Worker's unique identifier)
     * @return The related Worker's instance
     */
    public Worker getWorker(String workerId) {
        for(Worker w : workers)
            if(w.getWorkerId().equals(workerId))
                return w;

        return null;
    }

    /**
     *
     * @return True if the Player is playing
     */
    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * Given a State Type, Turn is set.
     *
     * @param turn State Type of Turn
     */
    public void setTurn(StateType turn) {
        if (turn == StateType.MOVEMENT) {
            this.turn = movementState;
            this.turnType = StateType.MOVEMENT;
        }
        else if (turn == StateType.CONSTRUCTION) {
            this.turn = constructionState;
            this.turnType = StateType.CONSTRUCTION;
        }
        else {
            this.turn = constructionState;
            this.turnType = StateType.NONE;
        }
    }

    public StateType getTurnType() {
        return turnType;
    }

    /**
     * Gets information about the (eventual) movement
     * forced upon an opponent's Worker during
     * Player's last executed move.
     *
     * @return A WorkerMoved object
     */
    public MyMove.WorkerMoved forcedOpponentMove() {
        return card.getMyMove().getOpponentForcedMove();
    }
}
