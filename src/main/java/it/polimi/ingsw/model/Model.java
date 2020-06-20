package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.board.placeables.Placeable;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.build.MyConstruction;
import it.polimi.ingsw.model.card.move.MyMove;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.gameRoom.GameRoom;
import it.polimi.ingsw.model.gameRoom.GeneralGameRoom;
import it.polimi.ingsw.model.move.*;
import it.polimi.ingsw.model.persistence.*;
import it.polimi.ingsw.model.persistence.board.BoardState;
import it.polimi.ingsw.model.persistence.board.CellState;
import it.polimi.ingsw.model.persistence.board.PlaceableData;
import it.polimi.ingsw.model.persistence.players.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.turn.StateType;
import it.polimi.ingsw.model.player.worker.Worker;

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
    private GameState gameState;
    private Controller controller; // associated controller
    public final int WORKERS_PER_PLAYER;
    private volatile Boolean gameStarted; // tell if the game has started
    private GeneralGameRoom gameRoom;
    private Board board;
    private List<String> cards;
    private volatile MoveOutcomeType moveOutcome; // tells the outcome of a move being executed at a certain point
    private volatile int lastPlayingIndex;
    private WorkerMovedEvent mainWorkerMoved;

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
        this.lastPlayingIndex = 0;
    }

    /**
     * By this method, the game Model is initialized for the new Game.
     */
    public void initialize(Controller controller, List<String> players) {
        this.controller = controller;
        List<Player> playerList = players.stream().map(p -> new Player(p)).collect(Collectors.toList());
        gameRoom.setPlayers(playerList);
        this.gameState = new GameState();
    }

    /**
     * Starts a new game.
     */
    public void startGame() throws LoseException {
        gameRoom.getPlayersList().get(0).startTurn();
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
     * Registers Players' Turns Observers.
     */
    public void registerTurnObservers() {
        gameRoom.registerObservers();
    }

    /**
     * Sort the Players within the given list's new order.
     *
     * @param players (List with new Players' order)
     */
    public void sortPlayers(List<String> players) {
        List<Player> sortedList = new ArrayList<>();

        for(int i = 0; i < players.size(); i++)
            sortedList.add(gameRoom.getPlayer(players.get(i)));

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

        for(Player player : gameRoom.getPlayersList()) {
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
    public WorkerPlacedEvent placeWorker(int x, int y, String playerNickname) {
        boolean placed = true;

        /* 1- Get the Player and instantiate a new Worker */
        Player player = gameRoom.getPlayer(playerNickname);
        Worker worker = new Worker(player);

        /* 2- Try to place the Worker */
        try {
            placed = worker.place(board.getCellAt(x, y));
            //placed = board.getCellAt(x, y).placeOn(worker);
        }
        catch (OutOfBoardException ex) {
            placed = false;
        }

        /* 3- If the Worker was correctly placed, register it among the Player's Workers */
        if(placed) {
            player.registerWorker(worker);
            worker.registerColor();
            return new WorkerPlacedEvent(worker.getWorkerId(), x, y, worker.getColor(), true);
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
            return "CHALLENGER_NOT_SET_YET"; // never actually returns this
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
            return "START_PLAYER_NOT_SET_YET"; // never actually returns this
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
     *
     * @return StateType of the playing Player
     */
    public StateType getPlayingPlayerState() {
        for(Player player : gameRoom.getPlayersList())
            if (player.isPlaying())
                return player.getTurnType();

        return StateType.NONE;
    }

    /**
     *
     * @return The playing Player's reference
     */
    public Player getPlayingPlayerReference() {
        for(Player player : gameRoom.getPlayersList())
            if (player.isPlaying())
                return player;

        return null;
    }




    /* ############################## MOVE EXECUTION METHODS ################################ */




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
            if (w != null)
                moveSuccess = player.chooseWorker(w);

            /* 3- Return the result */
            return new WorkerSelectedEvent(playerNickname, workerId, moveSuccess);
        }

        return null;
    }



    public List<WorkerMovedEvent> moveWorker(String workerId, int x, int y, String playerNickname) {
        List<WorkerMovedEvent> workersMoved = new ArrayList<>();
        boolean moveSuccess = false;
        Player player;
        Worker w;
        Cell initialPosition;
        Cell nextPosition;
        Move moveToExecute;
        int finalX = x;
        int finalY = y;

        /* Perform the move just if the Player who requested it is the playing one */
        if(getPlayingPlayer().equals(playerNickname)) {
            /* 1- Gather needed data */
            player = gameRoom.getPlayer(playerNickname);
            w = player.getWorker(workerId);

            if(w != null) {
                initialPosition = w.position();
                moveOutcome = MoveOutcomeType.NOT_EXECUTED;

                /* 2- Execute move */
                try {
                    nextPosition = board.getCellAt(x, y);
                    moveToExecute = new Move(initialPosition, nextPosition);
                    moveSuccess = player.executeMove(moveToExecute, w);
                    if (moveSuccess)
                        moveOutcome = MoveOutcomeType.EXECUTED;
                    else {
                        finalX = w.position().getX();
                        finalY = w.position().getY();
                    }
                } catch (TurnSwitchedException ex) {
                    moveSuccess = true;
                    moveOutcome = MoveOutcomeType.TURN_SWITCHED;
                    //controller.notifyFromModel(new TurnStatusChangedEvent(playerNickname, StateType.CONSTRUCTION, true), playerNickname); // todo: useless, to remove.
                } catch (TurnOverException ex) {
                    moveSuccess = true;
                    moveOutcome = MoveOutcomeType.TURN_OVER;
                    //switchPlayer();
                } catch (LoseException ex) { // when a Player perform a Move which he/she could not execute
                    moveSuccess = true; // move was executed, but in this case, a Lose Condition is thereby triggered
                    moveOutcome = MoveOutcomeType.LOSS;
                    lastPlayingIndex = gameRoom.getPlayersList().indexOf(player);
                } catch (WinException ex) {
                    moveSuccess = true;
                    moveOutcome = MoveOutcomeType.WIN;
                } catch (OutOfBoardException ex) {
                    moveSuccess = false;
                    finalX = w.position().getX();
                    finalY = w.position().getY();
                    moveOutcome = MoveOutcomeType.OUT_OF_BOARD;
                } catch (RunOutMovesException ex) {
                    moveSuccess = false;
                    finalX = w.position().getX();
                    finalY = w.position().getY();
                    moveOutcome = MoveOutcomeType.RUN_OUT_OF_MOVES;
                } catch (BuildBeforeMoveException ex) {
                    moveSuccess = false;
                    finalX = w.position().getX();
                    finalY = w.position().getY();
                    moveOutcome = MoveOutcomeType.BUILD_BEFORE_MOVE;
                } catch (WrongWorkerException ex) {
                    moveSuccess = false;
                    finalX = w.position().getX();
                    finalY = w.position().getY();
                    moveOutcome = MoveOutcomeType.WRONG_WORKER;
                }

                /* 3- Return the result */
                mainWorkerMoved = new WorkerMovedEvent(workerId, initialPosition.getX(), initialPosition.getY(), finalX, finalY, moveOutcome);
                workersMoved.add(mainWorkerMoved);
                if(player.forcedOpponentMove() != null)
                    workersMoved.add(new WorkerMovedEvent(player.forcedOpponentMove().getWorkerId(), player.forcedOpponentMove().getInitialX(), player.forcedOpponentMove().getInitialY(), player.forcedOpponentMove().getFinalX(), player.forcedOpponentMove().getFinalY(), MoveOutcomeType.OPPONENT_WORKER_MOVED));
            }
            else {
                mainWorkerMoved = new WorkerMovedEvent(workerId, 0, 0, 0, 0, MoveOutcomeType.WRONG_WORKER);
                workersMoved.add(mainWorkerMoved);
            }

            return workersMoved;
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

            if(w != null) {
                currentPosition = w.position();
                moveOutcome = MoveOutcomeType.NOT_EXECUTED; // TODO: dire a Giorgio di questo tipo enumerativo che specifica l'esito della mossa eseguita (in maniera più specifica rispetto a un semplice booleano)

                /* 2- Execute move */
                try {
                    selectedCell = board.getCellAt(x, y);
                    moveToExecute = new BuildMove(currentPosition, selectedCell, blockType);
                    moveSuccess = player.executeMove(moveToExecute, w); // we are sure this will call ConstructionManager because if the Player doesn't switch the turn, he cannot send a "Construction-type" request
                    if (moveSuccess)
                        moveOutcome = MoveOutcomeType.EXECUTED;
                } catch (TurnSwitchedException ex) {
                    moveSuccess = true;
                    moveOutcome = MoveOutcomeType.TURN_SWITCHED;
                    //controller.notifyFromModel(new TurnStatusChangedEvent(playerNickname, StateType.MOVEMENT, true), playerNickname); // todo: useless, to remove.
                } catch (TurnOverException ex) {
                    moveSuccess = true;
                    moveOutcome = MoveOutcomeType.TURN_OVER;
                    //switchPlayer();
                } catch (LoseException ex) { // when a Player perform a Move which he/she could not execute
                    moveSuccess = true; // move was executed, but in this case, a Lose Condition is thereby triggered
                    moveOutcome = MoveOutcomeType.LOSS;
                    lastPlayingIndex = gameRoom.getPlayersList().indexOf(player);
                } catch (WinException ex) {
                    moveSuccess = true;
                    moveOutcome = MoveOutcomeType.WIN;
                } catch (OutOfBoardException ex) {
                    moveSuccess = false;
                    moveOutcome = MoveOutcomeType.OUT_OF_BOARD;
                } catch (RunOutMovesException ex) {
                    moveSuccess = false;
                    moveOutcome = MoveOutcomeType.RUN_OUT_OF_MOVES;
                } catch (BuildBeforeMoveException ex) {
                    moveSuccess = false;
                    moveOutcome = MoveOutcomeType.BUILD_BEFORE_MOVE;
                } catch (WrongWorkerException ex) {
                    moveSuccess = false;
                    moveOutcome = MoveOutcomeType.WRONG_WORKER;
                }

                /* 3- Return the result */
                return new BlockBuiltEvent(x, y, blockType, moveOutcome);
            }
            else
                return new BlockBuiltEvent(x, y, blockType, MoveOutcomeType.WRONG_WORKER);
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
            moveOutcome = MoveOutcomeType.NOT_EXECUTED;

            /* 2- Execute move */
            try {
                changeSuccess = player.chooseState(turnStatus);
            }
            catch (TurnOverException ex) {
                changeSuccess = true;
                moveOutcome = MoveOutcomeType.TURN_OVER;
            }
            catch (LoseException ex) {
                changeSuccess = true;
                moveOutcome = MoveOutcomeType.LOSS; // todo gestire nel controller
                lastPlayingIndex = gameRoom.getPlayersList().indexOf(player);
            }

            /* 3- Return the result */
            return new TurnStatusChangedEvent(playerNickname, turnStatus, changeSuccess);
        }

        return null;
    }




    /* ########################### AUXILIARY AND SUPPORT METHODS ############################ */




    public void switchPlayer() {
        String playingPlayer;
        Player player;
        int playerIndex; // index of a Player who is already removed from the gameRoom's players' list (decremented so to maintain the playing order)
        boolean playerSwitched = false; // tells when a switching among Players has been executed

        while(!playerSwitched && gameStarted) {
            /* 1- End the current playing Player's turn */
            playerIndex = endCurrentPlayerTurn();
            /* 2- Select the next Player */
            Player nextPlayer = selectNextPlayer(playerIndex);
            /* 3- Next Player starts to play */
            try {
                nextPlayer.startTurn();
                playerSwitched = true;
                controller.printControlMessage("### It's " + nextPlayer.getNickname() + "'s turn.");
                /* 4- Notify the Player */
                controller.notifyFromModel(new TurnStatusChangedEvent(nextPlayer.getNickname(), StateType.MOVEMENT, true));
            } catch (LoseException ex) {
                /* 4- Handle the loss of the Player */
                //controller.notifyFromModel(new PlayerLoseEvent(nextPlayer.getNickname(), "Player " + nextPlayer.getNickname() + " has lost the game!")); // todo: to remove
                // todo if the message doesn't arrive, think about setting-up a timer (thread-related problems...)
                lastPlayingIndex = gameRoom.getPlayersList().indexOf(nextPlayer);
                handlePlayerLoss(nextPlayer);
            }
        }
    }

    /**
     * End current Player's turn.
     *
     * @return Last current Player's index
     */
    private int endCurrentPlayerTurn() {
        String playingPlayer;
        Player player;
        int playerIndex;
        playingPlayer = getPlayingPlayer();
        if (!playingPlayer.equals("")) {
            player = gameRoom.getPlayer(playingPlayer);
            playerIndex = gameRoom.getPlayersList().indexOf(player);
            endPlayerTurn(playingPlayer);
        }
        else
            playerIndex = lastPlayingIndex - 1;
        return playerIndex;
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
        /* 0- End Player's Turn */
        losingPlayer.endTurn();
        /* 1- Notify all Players in game about the losingPlayer's loss */
        controller.notifyFromModel(new PlayerLoseEvent(losingPlayer.getNickname(), "Player " + losingPlayer.getNickname() + " has lost the game!"));
        /* 2- Unregister the Player from the game */
        unregisterPlayer(losingPlayer);
        controller.printControlMessage("### Player '" + losingPlayer.getNickname() + "' has lost the game.");
        /* 3- Check if there is just one Player left in the game */
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
        controller.printControlMessage("### Player '" + winningPlayer.getNickname() + "' has won.");
        controller.gameOver();
    }

    /**
     *
     * @param index (Last playing Player's index)
     * @return Next playing Player
     */
    private Player selectNextPlayer(int index) {
//        Player player = gameRoom.getPlayer(playingPlayer); // todo: maybe to remove (useless)
//        int index = gameRoom.getPlayersList().indexOf(player); // todo: maybe to remove (useless)
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
        /* 1- Take Player's Workers, remove them and notify views */
        removePlayerWorkers(losingPlayer);
        /* 2- Remove the Player from the Players list */
        gameRoom.removePlayer(losingPlayer.getNickname());
        /* 3- Remove the Player-associated Virtual View */ // todo: to remove (creates problems...)
        //controller.removeView(losingPlayer.getNickname()); // todo: to remove (creates problems...)
    }

    /**
     * Take Player's Workers, remove them and notify Views.
     *
     * @param losingPlayer Player whose Workers are to be removed
     */
    private void removePlayerWorkers(Player losingPlayer) {
        List<Worker> workers = losingPlayer.getWorkers();
        List<WorkerRemovedEvent> removedWorkers = workers.stream().map(x -> new WorkerRemovedEvent(x.getWorkerId(), x.position().getX(), x.position().getY(), true)).collect(Collectors.toList());
        board.removeWorkers(losingPlayer);
        removedWorkers.forEach(x -> controller.notifyFromModel(x));
    }

    public MoveOutcomeType getMoveOutcome() {
        return moveOutcome;
    }

    public synchronized void setMoveOutcome(MoveOutcomeType moveOutcome) {
        this.moveOutcome = moveOutcome;
    }

    public List<String> players() {
        return gameRoom.getPlayersList().stream().map(p -> p.getNickname()).collect(Collectors.toList());
    }

    /**
     * Given a Player's nickname, removes it from the game.
     *
     * @param playerNickname Player's nickname
     */
    public void removePlayer(String playerNickname) {
        gameRoom.removePlayer(playerNickname);
    }








    // TODO: refactor all these methods
    /* ############################# GAME STATE HANDLING LOGIC ############################## */




    public void restoreGameState() {
        setPlayersState(gameState.getPlayers());
        setBoardState(gameState.getBoard());
        setGameStarted(gameState.isGameStarted());
    }




    /* ##### GAME PERSISTENCE ##### */
    public GameState getLastGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState createGameState() {
        GameState gameState = new GameState();

        gameState.setGameStarted(this.gameStarted);
        gameState.setBoard(this.createBoardState());
        gameState.setPlayers(this.createPlayersState());
        this.gameState = gameState; // save reference

        return gameState;
    }


    /* ##### ACTION UNDO ##### */
    /* ### BOARD ### */

    public BoardState getLastBoardState() {
        return gameState.getBoard();
    }

    public void setBoardState(BoardState boardState) {
        gameState.setBoard(boardState);

        // todo modify actual game scenario accordingly (when restoring old scenario)
        /* Modify actual game scenario accordingly (when restoring old scenario) */
        /* 1- Clear board */
        board.clear();

        /* 2- Fill the board */
        CellState[][] cells = boardState.getBoard();
        for(int x = 0; x < boardState.getXSize(); x++) {
            for(int y = 0; y < boardState.getYSize(); y++) {
                /* 2.1- Get buildings information */
                Deque<PlaceableData> cellBuilding = new ArrayDeque<>(cells[x][y].getBuilding());
                int buildingHeight = cellBuilding.size();
                for(int h = 0; h < buildingHeight; h++) {
                    /* 2.2 - Analyze Placeable Type info and build accordingly */
                    try {
                        PlaceableData placeable = cellBuilding.removeFirst();
                        if(placeable.getPlaceableType() != PlaceableType.WORKER)
                            board.getCellAt(x, y).place(placeable.getPlaceableType());
                        else {
                            Worker w = getWorker(placeable.getWorkerId());
                            if(w != null)
                                w.place(board.getCellAt(x, y));
                        }
                    }
                    catch (OutOfBoardException ex) {
                        System.err.println("ERROR: Something went wrong when trying to restore Board's state!");
                    }
                }
            }
        }
    }


    public BoardState createBoardState() {
        BoardState boardState = new BoardState();
        CellState[][] cellStates = new CellState[board.getXDim()][board.getYDim()];
        Deque<PlaceableData> building;

        /* Cells preparation */
        for(int x = 0; x < board.getXDim(); x++)
            for(int y = 0; y < board.getYDim(); y++) {
                cellStates[x][y] = new CellState();
                /* Buildings preparation */
                building = new ArrayDeque<>();
                Cell cell;
                int height = 0;

                try {
                    cell = board.getCellAt(x, y);
                    height = cell.getHeigth();
                }
                catch (OutOfBoardException ex) {
                    System.err.println("ERROR: Something went wrong when trying to save Board's state!");
                    return null;
                }

                for(int h = 0; h < height; h++) {
                    Placeable placeableObj = cell.getPlaceableAt(h);
                    PlaceableData placeableDataObj = new PlaceableData();
                    placeableDataObj.setPlaceable(placeableObj);

                    building.add(placeableDataObj); // save Placeable info // todo verificare che la push funziona e che il deque rispecchia il deque della Board
                }

                cellStates[x][y].setBuilding(building); // save Cell info
            }

        /* Save Board info */
        boardState.setBoard(cellStates);
        boardState.setXSize(board.getXDim());
        boardState.setYSize(board.getYDim());

        return boardState;
    }

    /* ### PLAYERS ### */
    public PlayersState getLastPlayersState() {
        return gameState.getPlayers();
    }

    private void setPlayersState(PlayersState playersState) {
        gameState.setPlayers(playersState);

        // todo modify actual game scenario accordingly (when restoring old scenario)
        /* Modify actual game scenario accordingly (when restoring old scenario) */
        /* 0- Get Players' nicknames */
        Map<String, PlayerData> playersData = new HashMap<>(playersState.getData());

        /* 1- Foreach Player: restore its state */
        Set<String> playersSet = new HashSet<>(playersData.keySet());
        for(String playerNickname : playersSet) {
            /* 1.1- Gather Player's data */
            PlayerData playerData = playersData.get(playerNickname);
            Player player = gameRoom.getPlayer(playerNickname);
            /* 1.2- Restore basic info/flags (Challenger) */
            player.setChallenger(playerData.isChallenger());
            /* 1.3- Restore Card info/flags */ // todo aggiungere una classe CardData per salvare lo stato dell'esecuzione del turno
            player.chooseCard(playerData.getCard().getName());
                // Card info
            CardData cardData = playerData.getCard();
            Card card = player.getCard();
                // Movement
            MyMove playerMove = card.getMyMove();
            MovementData movementData = cardData.getMovement();
            try {
                playerMove.setStartingPosition(board.getCellAt(movementData.getStartingPositionX(), movementData.getStartingPositionY()));
                playerMove.setLastMove(new Move(movementData.getLastMoveFloorDirection(), movementData.getLastMoveLevelDirection(), movementData.getLastMoveLevelDepth(), board.getCellAt(movementData.getLastMoveSelectedCellX(), movementData.getLastMoveSelectedCellY())));
            }
            catch (OutOfBoardException ex) {
                System.err.println("ERROR: Something went wrong when trying to restore Players' state!");
            }
            playerMove.setMovesLeft(movementData.getMovesLeft());
                // Construction
            MyConstruction playerConstruction = card.getMyConstruction();
            ConstructionData constructionData = cardData.getConstruction();
            try {
                playerConstruction.setLastMove(new BuildMove(constructionData.getLastMoveFloorDirection(), constructionData.getLastMoveLevelDirection(), constructionData.getLastMoveLevelDepth(), board.getCellAt(constructionData.getLastMoveSelectedCellX(), constructionData.getLastMoveSelectedCellY()), constructionData.getLastMoveBlockType()));
            }
            catch (OutOfBoardException ex) {
                System.err.println("ERROR: Something went wrong when trying to restore Players' state!");
            }
            playerConstruction.setConstructionLeft(constructionData.getMovesLeft());
                // Card flags
            card.setMovementExecuted(cardData.isMovementExecuted());
            card.setConstructionExecuted(cardData.isConstructionExecuted());
            card.setTurnCompleted(cardData.isTurnCompleted());

            player.setTurn(playerData.getTurn());
            /* 1.4- Restore basic info/flags */
            player.setStartingPlayer(playerData.isStartingPlayer());
            player.setPlaying(playerData.isPlaying());
            /* 2- Register Workers and restore related info */
            List<WorkerData> workersData = new ArrayList<>(playerData.getWorkers());
            /* 2.0- Delete any previous Worker owned */
            player.clearWorkers();
            for(WorkerData wd : workersData) {
                /* 2.1- Instantiate a new Worker */
                Worker worker = new Worker(player);
                /* 2.2- Restore basic Worker's info/flag */
                worker.setId(wd.getWorkerId());
                worker.setColor(wd.getColor());
                worker.setChosen(wd.getChosen());
                /* 2.3- [The Worker will be placed later] */ // todo vedere se il posizionamento posticipato funziona bene
                /* 2.4- Register the Worker among those owned by the Player */
                player.registerWorker(worker);
            }
        }
        /* Players will be sorted by the Controller */
        /* Turn Observers will be registered by the Controller */
    }

    public PlayersState createPlayersState() {
        PlayersState playersState = new PlayersState();
        Map<String, PlayerData> playersData = new HashMap<>();

        /* Players info preparation */
        for(int i = 0; i < gameRoom.getPlayersList().size(); i++) {
            Player player = gameRoom.getPlayer(i);
            List<Worker> workers = player.getWorkers();
            List<WorkerData> workersData = new ArrayList<>();
            /* Workers preparation */
            for(int j = 0; j < workers.size(); j++) {
                Worker w = workers.get(j);
                WorkerData wd = new WorkerData();
                wd.setWorkerId(w.getWorkerId());
                wd.setChosen(w.getChosenStatus());
                wd.setColor(w.getColor());
                /* Save Worker data */
                workersData.add(wd);
            }

            /* Player data preparation */
            PlayerData pd = new PlayerData();
            pd.setNickname(player.getNickname());
            pd.setChallenger(player.isChallenger());
            pd.setStartingPlayer(player.isStartingPlayer());
            /* Gather Card info */
            Card card = player.getCard();
            CardData cardData = new CardData();
            cardData.setName(card.getName());
            cardData.setMovementExecuted(card.hasExecutedMovement());
            cardData.setConstructionExecuted(card.hasExecutedConstruction());
            cardData.setTurnCompleted(card.isTurnCompleted());
            /* Gather Movement and Construction info */
            // Movement
            MovementData movementData = new MovementData();
            Cell startingPosition = card.getMyMove().getStartingPosition();
            if (startingPosition != null) {
                movementData.setStartingPositionX(startingPosition.getX());
                movementData.setStartingPositionY(startingPosition.getY());
            }
                // Gather last move data
            Move lastMove = card.getMyMove().getLastMove();
            if(lastMove != null) {
                movementData.setLastMoveFloorDirection(lastMove.getFloorDirection());
                movementData.setLastMoveLevelDirection(lastMove.getLevelDirection());
                movementData.setLastMoveLevelDepth(lastMove.getLevelDepth());
                movementData.setLastMoveSelectedCellX(lastMove.getSelectedCell().getX());
                movementData.setLastMoveSelectedCellY(lastMove.getSelectedCell().getY());
            }
            else {
                movementData.setLastMoveFloorDirection(FloorDirection.NONE);
                movementData.setLastMoveLevelDirection(LevelDirection.NONE);
                movementData.setLastMoveLevelDepth(0);
                movementData.setLastMoveSelectedCellX(0);
                movementData.setLastMoveSelectedCellY(0);
            }
                // moves left
            movementData.setMovesLeft(card.getMyMove().getMovesLeft());

            // Construction
            ConstructionData constructionData = new ConstructionData();
                // Gather last move data
            BuildMove lastBuild = card.getMyConstruction().getLastMove();
            if(lastBuild != null) {
                constructionData.setLastMoveBlockType(lastBuild.getBlockType());
                constructionData.setLastMoveFloorDirection(lastBuild.getFloorDirection());
                constructionData.setLastMoveLevelDirection(lastBuild.getLevelDirection());
                constructionData.setLastMoveLevelDepth(lastBuild.getLevelDepth());
                constructionData.setLastMoveSelectedCellX(lastBuild.getSelectedCell().getX());
                constructionData.setLastMoveSelectedCellY(lastBuild.getSelectedCell().getY());
            }
            else {
                constructionData.setLastMoveBlockType(PlaceableType.NONE);
                constructionData.setLastMoveFloorDirection(FloorDirection.NONE);
                constructionData.setLastMoveLevelDirection(LevelDirection.NONE);
                constructionData.setLastMoveLevelDepth(0);
                constructionData.setLastMoveSelectedCellX(0);
                constructionData.setLastMoveSelectedCellY(0);
            }
                // constructions left
            constructionData.setMovesLeft(card.getMyConstruction().getConstructionLeft());
            // remaining data
            cardData.setMovement(movementData);
            cardData.setConstruction(constructionData);
            pd.setCard(cardData);
            pd.setWorkers(workersData);
            pd.setTurn(player.getTurnType());
            pd.setPlaying(player.isPlaying());

            /* Save Player data */
            playersData.put(player.getNickname(), pd);
        }

        /* Save Players info */
        playersState.setData(playersData);

        return playersState;
    }


    /**
     * Given a Worker's ID, find and return its Worker's instance.
     *
     * @param workerId Worker's ID
     * @return Worker's instance (null if no Worker has the provided ID)
     */
    private Worker getWorker(String workerId) {
        Worker w = null;

        for(Player p : gameRoom.getPlayersList()) {
            w = p.getWorker(workerId);

            if(w != null)
                return w;
        }

        return null;
    }


    public WorkerMovedEvent getMainWorkerMoved() {
        return mainWorkerMoved;
    }

    public void setMainWorkerMoved(WorkerMovedEvent mainWorkerMoved) {
        this.mainWorkerMoved = mainWorkerMoved;
    }
}
