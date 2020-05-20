package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.events.*;

import java.util.*;
import java.util.stream.Collectors;

// TODO: DO SOME REFACTORING IN THIS CLASS
/**
 * This class provides and implements a unified and simplified
 * public interface by which Controller can query Model's
 * data and modify them.
 * By using this class, Controller can handle a Game match flow.
 *
 * A Facade Design Pattern is applied to let the Controller interact
 * with the Model subsystem with ease.
 *
 * @author AndreaAltomare
 */
public class Model {
    // TODO: All "volatile" variables needs to be changed to "Atomic" type-of variables [FORSE]
    private Controller controller; // associated controller
    public final int WORKERS_PER_PLAYER;
    private volatile Boolean gameStarted; // tell if the game has started
    private GeneralGameRoom gameRoom;
    private Board board;
    private List<String> cards;
    private volatile MoveOutcomeType moveOutcome; // tells the outcome of a move being executed at a certain point

    /**
     * Constructor.
     * All members are instantiated here.
     */
    public Model() {
        this.gameStarted = false;
        this.gameRoom = new GameRoom();
        this.board = new IslandBoard();
        this.cards = new ArrayList<>();
        this.WORKERS_PER_PLAYER = 2;
        this.moveOutcome = MoveOutcomeType.NONE;
    }

    /**
     * By this method, the game Model is initialized for the new Game.
     */
    public void initialize(Controller controller, List<String> players) {
        this.controller = controller;
        List<Player> playerList = players.stream().map(p -> new Player(p)).collect(Collectors.toList());
        gameRoom.setPlayers(playerList);
    }

    /**
     * Starts a new game.
     */
    public void startGame() throws LoseException {
        gameRoom.players.get(0).startTurn();
    }

    /**
     * Stops the game.
     */
    public void stopGame() {
        setGameStarted(false);
    }

    /**
     * Tell whether the game has started or not.
     *
     * @return (Game has started ? true : false)
     */
    public synchronized Boolean hasGameStarted() {
        return gameStarted;
    }

    public synchronized void setGameStarted(Boolean gameStarted) {
        this.gameStarted = gameStarted;
    }




    /* ############################# GAME PREPARATION METHODS ############################### */




    /**
     * Sort the Players within the given list's new order.
     *
     * @param players (List with new Players' order)
     */
    public void sortPlayers(List<String> players) {
        List<Player> sortedList = new ArrayList<>();

        for(int i = 0; i < players.size(); i++) {
            sortedList.add(gameRoom.getPlayer(players.get(i)));
        }

        gameRoom.setPlayers(sortedList);
    }

    /**
     * Gets the X size of the board.
     *
     * @return Board's X size
     */
    public int getBoardXSize() {
        return board.getXDim();
    }

    /**
     * Gets the Y size of the board.
     *
     * @return Board's Y size
     */
    public int getBoardYSize() {
        return board.getYDim();
    }

    /**
     * Gets the Workers associated to every Player in the game.
     *
     * @return A Map with a List of Workers associated to each Player.
     */
    public Map<String, List<String>> getWorkersToPlayers() {
        Map<String, List<String>> map = new HashMap<>();

        for(Player player : gameRoom.players) {
            List<String> workers = player.getWorkers().stream().map(w -> w.getWorkerId()).collect(Collectors.toList());
            map.put(player.getNickname(),workers);
        }

        return map;
    }

    /**
     * Set the Challenger Player.
     *
     * @param challenger (Challenger's nickname)
     */
    public void setChallenger(String challenger) {
        gameRoom.chooseChallenger(challenger);
    }

    /**
     * Sets Cards for this game.
     *
     * @param cardsInGame (List of Cards' name)
     */
    public void setCardsInGame(List<String> cardsInGame) {
        this.cards = new ArrayList<>(cardsInGame);
    }

    public List<String> getCards() {
        return cards;
    }

    /**
     * Set the Card for a given Player.
     *
     * @param cardName (Card's name)
     * @param playerNickname (Player's nickname)
     */
    public void setPlayerCard(String cardName, String playerNickname) {
        gameRoom.getPlayer(playerNickname).chooseCard(cardName);
    }

    /**
     * Set the Start Player.
     *
     * @param startPlayer (Start Player's nickname)
     */
    public void setStartPlayer(String startPlayer) {
        gameRoom.chooseStartingPlayer(startPlayer);
    }

    /**
     * Place a new Worker onto the Game Board.
     *
     * @param x (X axis position)
     * @param y (Y axis position)
     * @param playerNickname (Player who wants to place a new Worker)
     * @return The new placed Worker's ID
     */
    public String placeWorker(int x, int y, String playerNickname) {
        boolean placed = true;

        /* 1- Get the Player and instantiate a new Worker */
        Player player = gameRoom.getPlayer(playerNickname);
        Worker worker = new Worker(player);

        /* 2- Try to place th Worker */
        try {
            placed = board.getCellAt(x, y).placeOn(worker);
        }
        catch (OutOfBoardException ex) {
            placed = false;
        }

        /* 3- If the Worker was correctly placed, register it among the Player's Workers */
        if(placed) {
            player.registerWorker(worker);
            return worker.getWorkerId();
        }
        else {
            worker = null;
            return null;
        }
    }

    /**
     *
     * @return Challenger's nickname
     */
    public String challenger() {
        Player challenger = gameRoom.getChallenger();

        if(challenger != null)
            return challenger.getNickname();
        else
            return "CHALLENGER_NOT_SET_YET";
    }

    /**
     *
     * @return Start Player's nickname
     */
    public String startPlayer() {
        Player startPlayer = gameRoom.getStartingPlayer();

        if(startPlayer != null)
            return startPlayer.getNickname();
        else
            return "START_PLAYER_NOT_SET_YET";
    }

    /**
     *
     * @return The playing Player's nickname
     */
    public String getPlayingPlayer() {
        for(Player player : gameRoom.getPlayersList())
            if (player.isPlaying())
                return player.getNickname();

        return "";
    }

    /**
     * Select a Worker to play with this turn.
     *
     * @param workerId (Worker's unique identifier)
     * @param playerNickname (Player's nickname)
     * @return An event which encapsulates the performed move's info
     */
    public WorkerSelectedEvent selectWorker(String workerId, String playerNickname) {
        boolean moveSuccess = false;
        Player player;
        Worker w;

        /* Perform the move just if the Player who requested it is the playing one */
        if(getPlayingPlayer().equals(playerNickname)) {
            /* 1- Gather needed data */
            player = gameRoom.getPlayer(playerNickname);
            w = player.getWorker(workerId);

            /* 2- Execute move */
            moveSuccess = player.chooseWorker(w);

            /* 3- Return the result */
            return new WorkerSelectedEvent(playerNickname, workerId, moveSuccess);
        }

        return null;
    }




    /* ############################## MOVE EXECUTION METHODS ################################ */




    public WorkerMovedEvent moveWorker(String workerId, int x, int y, String playerNickname) {
        boolean moveSuccess = false;
        Player player;
        Worker w;
        Cell initialPosition;
        Cell nextPosition;
        Move moveToExecute;

        /* Perform the move just if the Player who requested it is the playing one */
        if(getPlayingPlayer().equals(playerNickname)) {
            /* 1- Gather needed data */
            player = gameRoom.getPlayer(playerNickname);
            w = player.getWorker(workerId);
            initialPosition = w.position();
            moveOutcome = MoveOutcomeType.NOT_EXECUTED;

            /* 2- Execute move */
            try {
                nextPosition = board.getCellAt(x,y);
                moveToExecute = new Move(initialPosition, nextPosition);
                moveSuccess = player.executeMove(moveToExecute, w);
                if (moveSuccess)
                    moveOutcome = MoveOutcomeType.EXECUTED;
            }
            catch(TurnOverException ex) {
                moveSuccess = true;
                moveOutcome = MoveOutcomeType.TURN_OVER;
                //switchPlayer();
            }
            catch(LoseException ex) { // when a Player perform a Move which he/she could not execute
                moveSuccess = true; // move was executed, but in this case, a Lose Condition is thereby triggered
                moveOutcome = MoveOutcomeType.LOSS;
            }
            catch(WinException ex) {
                moveSuccess = true;
                moveOutcome = MoveOutcomeType.WIN;
            }
            catch(OutOfBoardException ex) {
                moveSuccess = false;
                moveOutcome = MoveOutcomeType.OUT_OF_BOARD;
            }
            catch(RunOutMovesException ex) {
                moveSuccess = false;
                moveOutcome = MoveOutcomeType.RUN_OUT_OF_MOVES;
            }
            catch(BuildBeforeMoveException ex) {
                moveSuccess = false;
                moveOutcome = MoveOutcomeType.BUILD_BEFORE_MOVE;
            }
            catch(WrongWorkerException ex) {
                moveSuccess = false;
                moveOutcome = MoveOutcomeType.WRONG_WORKER;
            }

            /* 3- Return the result */
            return new WorkerMovedEvent(workerId, initialPosition.getX(), initialPosition.getY(), w.position().getX(), w.position().getY(), moveOutcome);
        }

        return null;
    }

    public BlockBuiltEvent buildBlock(String workerId, int x, int y, PlaceableType blockType, String playerNickname) {
        boolean moveSuccess = false;
        Player player;
        Worker w;
        Cell currentPosition;
        Cell selectedCell;
        BuildMove moveToExecute;

        /* Perform the move just if the Player who requested it is the playing one */
        if(getPlayingPlayer().equals(playerNickname)) {
            /* 1- Gather needed data */
            player = gameRoom.getPlayer(playerNickname);
            w = player.getWorker(workerId);
            currentPosition = w.position();
            moveOutcome = MoveOutcomeType.NOT_EXECUTED; // TODO: dire a Giorgio di questo tipo enumerativo che specifica l'esito della mossa eseguita (in maniera più specifica rispetto a un semplice booleano)

            /* 2- Execute move */
            try {
                selectedCell = board.getCellAt(x,y);
                moveToExecute = new BuildMove(currentPosition, selectedCell, blockType);
                moveSuccess = player.executeMove(moveToExecute, w); // we are sure this will call ConstructionManager because if the Player doesn't switch the turn, he cannot send a "Construction-type" request
                if (moveSuccess)
                    moveOutcome = MoveOutcomeType.EXECUTED;
            }
            catch(TurnOverException ex) {
                moveSuccess = true;
                moveOutcome = MoveOutcomeType.TURN_OVER;
                //switchPlayer();
            }
            catch(LoseException ex) { // when a Player perform a Move which he/she could not execute
                moveSuccess = true; // move was executed, but in this case, a Lose Condition is thereby triggered
                moveOutcome = MoveOutcomeType.LOSS;
            }
            catch(WinException ex) {
                moveSuccess = true;
                moveOutcome = MoveOutcomeType.WIN;
            }
            catch(OutOfBoardException ex) {
                moveSuccess = false;
                moveOutcome = MoveOutcomeType.OUT_OF_BOARD;
            }
            catch(RunOutMovesException ex) {
                moveSuccess = false;
                moveOutcome = MoveOutcomeType.RUN_OUT_OF_MOVES;
            }
            catch(BuildBeforeMoveException ex) {
                moveSuccess = false;
                moveOutcome = MoveOutcomeType.BUILD_BEFORE_MOVE;
            }
            catch(WrongWorkerException ex) {
                moveSuccess = false;
                moveOutcome = MoveOutcomeType.WRONG_WORKER;
            }

            /* 3- Return the result */
            return new BlockBuiltEvent(x, y, blockType, moveOutcome);
        }

        return null;
    }

    public WorkerRemovedEvent removeWorker(String workerId, int x, int y, String playerNickname) {
        /* !!! This functionality is left for future changes in game rules implementation !!! */
        return new WorkerRemovedEvent(workerId, x, y, false); // ...therefore, no action is actually taken.
    }

    public BlockRemovedEvent removeBlock(String workerId, int x, int y, String playerNickname) {
        /* !!! This functionality is left for future changes in game rules implementation !!! */
        PlaceableType blockType = PlaceableType.NONE;
        return new BlockRemovedEvent(x, y, blockType, false); // ...therefore, no action is actually taken.
    }

    public TurnStatusChangedEvent changeTurnStatus(StateType turnStatus, String playerNickname) {
        boolean changeSuccess = false;
        Player player;

        /* Perform the move just if the Player who requested it is the playing one */
        if(getPlayingPlayer().equals(playerNickname)) {
            /* 1- Gather needed data */
            player = gameRoom.getPlayer(playerNickname);

            /* 2- Execute move */
            try {
                player.chooseState(turnStatus);
                changeSuccess = true;
            }
            catch (LoseException ex) {
                moveOutcome = MoveOutcomeType.LOSS; // todo gestire nel controller
            }

            /* 3- Return the result */
            return new TurnStatusChangedEvent(playerNickname, turnStatus, changeSuccess);
        }

        return null;
    }




    /* ########################### AUXILIARY AND SUPPORT METHODS ############################ */




    public void switchPlayer() {
        boolean playerSwitched = false; // tells when a switching among Players has been executed

        /* 1- End the current playing Player's turn */
        String playingPlayer = getPlayingPlayer();
        if (!playingPlayer.equals(""))
            endPlayerTurn(playingPlayer);

        while(!playerSwitched && gameStarted) {
            /* 2- Select the next Player */
            Player nextPlayer;
            playingPlayer = getPlayingPlayer();
            if (!playingPlayer.equals(""))
                nextPlayer = selectNextPlayer(playingPlayer);
            else
                nextPlayer = selectNextPlayer(gameRoom.getPlayer(0).getNickname());
            /* 3- Next Player starts to play */
            try {
                nextPlayer.startTurn();
                playerSwitched = true;
                /* 4- Notify the Player */
                controller.notifyFromModel(new TurnStatusChangedEvent(nextPlayer.getNickname(), StateType.MOVEMENT, true));
            } catch (LoseException ex) {
                /* 4- Handle the loss of the Player */
                controller.notifyFromModel(new PlayerLoseEvent(nextPlayer.getNickname(), "Player " + nextPlayer.getNickname() + " has lost the game!"));
                // todo if the message doesn't arrive, think about setting-up a timer (thread-related problems...)
                handlePlayerLoss(nextPlayer);
            }
        }
    }

    /**
     * Overloaded method to call from Controller.
     * Handle the Player's loss.
     *
     * @param playerNickname (Player's nickname)
     */
    public void handlePlayerLoss(String playerNickname) {
        this.handlePlayerLoss(gameRoom.getPlayer(playerNickname));
    }

    private void handlePlayerLoss(Player losingPlayer) {
        /* 1- Unregister the Player from the game */
        unregisterPlayer(losingPlayer);
        /* 2- Check if there is just one Player left in the game */
        if(onlyOnePlayerLeft()) {
            /* 3- Get the last Player left in the game */
            Player lastPlayer = getLastPlayerInGame();
            handlePlayerWin(lastPlayer);
        }
    }

    /**
     * Overloaded method to call from Controller.
     * Handle the Player's Win.
     *
     * @param playerNickname (Player's nickname)
     */
    public void handlePlayerWin(String playerNickname) {
        this.handlePlayerWin(gameRoom.getPlayer(playerNickname));
    }

    private void handlePlayerWin(Player winningPlayer) {
        /* 1- Stop the game */
        stopGame();
        /* 2- Notify Players about the Player win */
        controller.notifyFromModel(new PlayerWinEvent(winningPlayer.getNickname(), "You won!", "Player " + winningPlayer.getNickname() + " has won the game."));
    }

    /**
     *
     * @param playingPlayer (Current playing Player)
     * @return Next playing Player
     */
    private Player selectNextPlayer(String playingPlayer) {
        Player player = gameRoom.getPlayer(playingPlayer);
        int index = gameRoom.getPlayersList().indexOf(player);
        index++;
        Player nextPlayer = gameRoom.getPlayer((index % gameRoom.getPlayersList().size()));
        return nextPlayer;
    }

    private void endPlayerTurn(String playerNickname) {
        gameRoom.getPlayer(playerNickname).endTurn();
    }

    private Player getLastPlayerInGame() {
        return gameRoom.getPlayer(0);
    }

    private boolean onlyOnePlayerLeft() {
        return gameRoom.getPlayersList().size() == 1;
    }

    private void unregisterPlayer(Player losingPlayer) {
        /* 1- Take Player's Workers, remove them and notify views*/
        List<Worker> workers = losingPlayer.getWorkers();
        List<WorkerRemovedEvent> removedWorkers = workers.stream().map(x -> new WorkerRemovedEvent(x.getWorkerId(), x.position().getX(), x.position().getY(), true)).collect(Collectors.toList());
        board.removeWorkers(losingPlayer);
        removedWorkers.forEach(x -> controller.notifyFromModel(x));
        /* 2- Remove the Player from the Players list */
        gameRoom.removePlayer(losingPlayer.getNickname());
        /* 3- Remove the Player-associated Virtual View */
        controller.removeView(losingPlayer.getNickname());
    }

    public MoveOutcomeType getMoveOutcome() {
        return moveOutcome;
    }

    public synchronized void setMoveOutcome(MoveOutcomeType moveOutcome) {
        this.moveOutcome = moveOutcome;
    }
}
