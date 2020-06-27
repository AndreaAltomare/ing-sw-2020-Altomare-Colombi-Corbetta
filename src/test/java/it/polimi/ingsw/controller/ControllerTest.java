package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.move.MoveOutcomeType;
import it.polimi.ingsw.model.player.turn.StateType;
import it.polimi.ingsw.model.player.worker.Color;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.events.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for CardInfo class, aimed to verify it works properly
 *
 * @author Marco
 */
class ControllerTest {

    Controller controller;
    MyModel myModel;
    List<String> playerList;
    MyObserver myObserver;
    List<Object> messageList;
    List<String> nicknameList;

    /**
     * Private class used to simulate model behavior
     */
    private class MyModel extends Model {
        boolean checkSwitchPlayer;
        boolean checkHandlePlayerLoss;
        boolean checkHandlePlayerWin;

        String playingPlayer;
        StateType playingPlayerState;
        WorkerPlacedEvent workerPlacedEvent;
        WorkerSelectedEvent workerSelectedEvent;
        List<WorkerMovedEvent> workerMovedEventList;
        BlockBuiltEvent blockBuildEvent;
        WorkerRemovedEvent workerRemovedEvent;
        BlockRemovedEvent blockRemovedEvent;
        TurnStatusChangedEvent turnStatusChangedEvent;


        /**
         * private class used to simulate the behavior of Model's Methods
         */
        public MyModel() {
            super();
            checkSwitchPlayer = false;
            checkHandlePlayerLoss = false;
            checkHandlePlayerWin = false;

            workerMovedEventList = new ArrayList<>();
        }

        public void clear() {
            checkSwitchPlayer = false;
            checkHandlePlayerLoss = false;
            checkHandlePlayerWin = false;

            playingPlayer = null;
            playingPlayerState = null;
            workerPlacedEvent = null;
            workerSelectedEvent = null;
            workerMovedEventList = new ArrayList<>();
            blockBuildEvent = null;
            workerRemovedEvent = null;
            blockRemovedEvent = null;
            turnStatusChangedEvent = null;
        }

        public void setPlayingPlayer(String playingPlayer) {
            this.playingPlayer = playingPlayer;
        }

        @Override
        public String getPlayingPlayer() {
            return this.playingPlayer;
        }

        public void setPlayingPlayerState(StateType playingPlayerState) {
            this.playingPlayerState = playingPlayerState;
        }

        @Override
        public StateType getPlayingPlayerState() {
            return playingPlayerState;
        }

        @Override
        public void handlePlayerLoss(String playerNickname) {
            checkHandlePlayerLoss = true;
        }

        @Override
        public void handlePlayerWin(String playerNickname) {
            checkHandlePlayerWin = true;
        }

        @Override
        public void switchPlayer() {
            checkSwitchPlayer = true;
        }

        public void setWorkerPlacedEvent(WorkerPlacedEvent workerPlacedEvent) {
            this.workerPlacedEvent = workerPlacedEvent;
        }

        @Override
        public WorkerPlacedEvent placeWorker(int x, int y, String playerNickname) {
            return this.workerPlacedEvent;
        }

        public void setWorkerSelectedEvent(WorkerSelectedEvent workerSelectedEvent) {
            this.workerSelectedEvent = workerSelectedEvent;
        }

        @Override
        public WorkerSelectedEvent selectWorker(String workerId, String playerNickname) {
            return  this.workerSelectedEvent;
        }

        public void addWorkerMovedEvent( WorkerMovedEvent workerMovedEvent ) {
            this.workerMovedEventList.add(workerMovedEvent);
        }

        @Override
        public List<WorkerMovedEvent> moveWorker(String workerId, int x, int y, String playerNickname) {
            if ( this.workerMovedEventList.isEmpty() ) {
                return  null;
            }
            return this.workerMovedEventList;
        }

        @Override
        public WorkerMovedEvent getMainWorkerMoved() {
            return  workerMovedEventList.get(0);
        }
        public void setBlockBuildEvent(BlockBuiltEvent blockBuildEvent) {
            this.blockBuildEvent = blockBuildEvent;
        }

        @Override
        public BlockBuiltEvent buildBlock(String workerId, int x, int y, PlaceableType blockType, String playerNickname) {
            return this.blockBuildEvent;
        }

        public void setWorkerRemovedEvent(WorkerRemovedEvent workerRemovedEvent) {
            this.workerRemovedEvent = workerRemovedEvent;
        }

        @Override
        public WorkerRemovedEvent removeWorker(String workerId, int x, int y, String playerNickname) {
            return this.workerRemovedEvent;
        }

        public void setBlockRemovedEvent(BlockRemovedEvent blockRemovedEvent) {
            this.blockRemovedEvent = blockRemovedEvent;
        }

        @Override
        public BlockRemovedEvent removeBlock(String workerId, int x, int y, String playerNickname) {
            return  this.blockRemovedEvent;
        }

        public void setTurnStatusChangedEventReturned(TurnStatusChangedEvent turnStatusChangedEvent) {
            this.turnStatusChangedEvent = turnStatusChangedEvent;
        }

        @Override
        public TurnStatusChangedEvent changeTurnStatus(StateType turnStatus, String playerNickname) {
            return this.turnStatusChangedEvent;
        }



    }

    /**
     * Private class used to check controller's notifies
     */
    private class MyObserver implements Observer<Object> {

        List<Object> objectList;
        List<String> stringList;

        public MyObserver(){

            objectList = new ArrayList<>();
            stringList = new ArrayList<>();

        }

        public List<Object> getObjectList() {
            return objectList;
        }

        public List<String> getStringList() {
            return stringList;
        }

        public void clear() {
            objectList = new ArrayList<>();
            stringList = new ArrayList<>();
        }

        @Override
        public void update(Object message) {

            objectList.add(message);
            stringList.add(null);

        }

        @Override
        public void update(Object message, String nickname) {

            objectList.add(message);
            stringList.add(nickname);

        }
    }

    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        myModel = new MyModel();
        playerList = new ArrayList<>();
        playerList.add("andrea");
        playerList.add("giorgio");
        playerList.add("marco");
        controller = new Controller(myModel, playerList);
        myModel.initialize(controller, playerList);
        myObserver = new MyObserver();
        controller.addObserver(myObserver);
        messageList = new ArrayList<>();
        nicknameList = new ArrayList<>();
    }

    /**
     * Reset after test
     */
    @AfterEach
    void tearDown() {

        playerList.clear();
        messageList.clear();
        nicknameList.clear();
        controller.removeObserver(myObserver);
        myObserver = null;
        controller = null;
        myModel = null;

    }

    /**
     * Check if update(SetNicknameEvent, String) can send the correct event
     * Methods used:        size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateSetNickname() {
        String player = playerList.get(0);
        SetNicknameEvent setNicknameEvent = new SetNicknameEvent(player);

        controller.update(setNicknameEvent);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1 );
        assertTrue( nicknameList.size() == 1 );
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(setNicknameEvent.getNickname()));


    }

    /**
     * Check if update(SetPlayersNumberEvent, String) can send the correct event
     * Methods used:        size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateSetPlayersNumber() {

        controller.update(new SetPlayersNumberEvent(2) );

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);

    }

    /**
     * Check if update(GameResumingResponseEvent, String) can send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateGameResumingResponse() {
        String player = playerList.get(0);
        String challenger = playerList.get(1);
        GameResumingResponseEvent gameResumingResponseEvent;


        myModel.setChallenger(challenger);

        /* Game is started */
        myModel.setGameStarted(true);
        gameResumingResponseEvent = new GameResumingResponseEvent(true);
        controller.update(gameResumingResponseEvent, challenger);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(challenger) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game isn't started but the event isn't of challenger */
        myModel.setGameStarted(false);
        gameResumingResponseEvent = new GameResumingResponseEvent(true);
        controller.update(gameResumingResponseEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game isn't started and the event is of challenger */
        myModel.setGameStarted(false);
        gameResumingResponseEvent = new GameResumingResponseEvent(true);
        controller.update(gameResumingResponseEvent, challenger);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();

    }

    /**
     * Check if update(QuitEvent, String) can send the correct event
     * Methods used:        hasGameStarter()            of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateQuit() {
        String player = playerList.get(0);
        QuitEvent quitEvent = new QuitEvent();

        controller.update(quitEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof GameOverEvent );
        assertTrue( nicknameList.get(0) == null );
        assertTrue( !myModel.hasGameStarted() );

    }

    /**
     * Check if update(ViewRequestDataEvent, String) can send the correct event
     * Methods used:        size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateViewRequestData() {
        String player = playerList.get(0);
        ViewRequestDataEvent viewRequestDataEvent = new ViewRequestDataEvent();

        controller.update(viewRequestDataEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


    }

    /**
     * Check if update(CardChoosingEvent, String) can send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateCardChoosing() {
        String player = playerList.get(0);
        String challenger = playerList.get(1);
        CardsChoosingEvent cardChoosingEvent;
        List<String> cardList = new ArrayList<>();
        cardList.add("apollo");
        cardList.add("artemis");

        myModel.setChallenger(challenger);

        /* Game is started */
        myModel.setGameStarted(true);
        cardChoosingEvent = new CardsChoosingEvent(cardList);
        controller.update(cardChoosingEvent, challenger);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(challenger) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game isn't started but the event isn't of the challenger*/
        myModel.setGameStarted(false);
        cardChoosingEvent = new CardsChoosingEvent(cardList);
        controller.update(cardChoosingEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game isn't started and the event is of the challenger*/
        myModel.setGameStarted(false);
        cardChoosingEvent = new CardsChoosingEvent(cardList);
        controller.update(cardChoosingEvent, challenger);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();

    }

    /**
     * Check if update(CardSelectionEvent, String) can send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateCardSelection() {
        String player = playerList.get(0);
        CardSelectionEvent cardSelectionEvent;


        /* Game is started */
        myModel.setGameStarted(true);
        cardSelectionEvent = new CardSelectionEvent("default");
        controller.update(cardSelectionEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );

        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game isn't started */
        myModel.setGameStarted(false);
        cardSelectionEvent = new CardSelectionEvent("default");
        controller.update(cardSelectionEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();

    }

    /**
     * Check if update(SetStarPlayerEvent, String) can send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateSetStarPlayer() {
        String player = playerList.get(0);
        String challenger = playerList.get(1);
        SetStartPlayerEvent setStartPlayerEvent;

        myModel.setChallenger(challenger);

        /* Game is started */
        myModel.setGameStarted(true);
        setStartPlayerEvent = new SetStartPlayerEvent(challenger);
        controller.update(setStartPlayerEvent, challenger);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(challenger) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game isn't started but the event isn't of the challenger*/
        myModel.setGameStarted(false);
        setStartPlayerEvent = new SetStartPlayerEvent(challenger);
        controller.update(setStartPlayerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game isn't started and the event is of the challenger*/
        myModel.setGameStarted(false);
        setStartPlayerEvent = new SetStartPlayerEvent(challenger);
        controller.update(setStartPlayerEvent, challenger);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();

    }

    /**
     * Check if update(PlaceWorkerEvent, String) can send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdatePlaceWorker() {
        String player = playerList.get(0);
        PlaceWorkerEvent placeWorkerEvent;
        WorkerPlacedEvent workerPlacedEvent;


        /* Game is started */
        myModel.setGameStarted(true);
        placeWorkerEvent = new PlaceWorkerEvent(0,0);
        workerPlacedEvent = new WorkerPlacedEvent("[Worker]\t1",placeWorkerEvent.getX(), placeWorkerEvent.getY(), Color.BLUE, true);
        myModel.setWorkerPlacedEvent(workerPlacedEvent);
        controller.update(placeWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game isn't started but workerPlacedEvent == null */
        myModel.setGameStarted(false);
        placeWorkerEvent = new PlaceWorkerEvent(0,0);
        workerPlacedEvent = null;
        myModel.setWorkerPlacedEvent(workerPlacedEvent);
        controller.update(placeWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game isn't started and workerPlacedEvent != null --> place fist worker */
        myModel.setGameStarted(false);
        placeWorkerEvent = new PlaceWorkerEvent(0,0);
        workerPlacedEvent = new WorkerPlacedEvent("[Worker]\t1",placeWorkerEvent.getX(), placeWorkerEvent.getY(), Color.BLUE, true);
        myModel.setWorkerPlacedEvent(workerPlacedEvent);
        controller.update(placeWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof WorkerPlacedEvent );
        assertTrue( ((WorkerPlacedEvent) messageList.get(0)).getWorker().equals(workerPlacedEvent.getWorker()) );
        assertTrue( ((WorkerPlacedEvent) messageList.get(0)).getX() == (workerPlacedEvent.getX()) );
        assertTrue( ((WorkerPlacedEvent) messageList.get(0)).getY() == (workerPlacedEvent.getY()) );
        assertTrue( ((WorkerPlacedEvent) messageList.get(0)).getColor() == (workerPlacedEvent.getColor()) );
        assertTrue( ((WorkerPlacedEvent) messageList.get(0)).success() == (workerPlacedEvent.success()) );
        assertTrue( nicknameList.get(0) == null );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game isn't started and workerPlacedEvent != null --> place second worker */
        myModel.setGameStarted(false);
        placeWorkerEvent = new PlaceWorkerEvent(1,1);
        workerPlacedEvent = new WorkerPlacedEvent("[Worker]\t2",placeWorkerEvent.getX(), placeWorkerEvent.getY(), Color.BLUE, true);
        myModel.setWorkerPlacedEvent(workerPlacedEvent);
        controller.update(placeWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof WorkerPlacedEvent );
        assertTrue( ((WorkerPlacedEvent) messageList.get(0)).getWorker().equals(workerPlacedEvent.getWorker()) );
        assertTrue( ((WorkerPlacedEvent) messageList.get(0)).getX() == (workerPlacedEvent.getX()) );
        assertTrue( ((WorkerPlacedEvent) messageList.get(0)).getY() == (workerPlacedEvent.getY()) );
        assertTrue( ((WorkerPlacedEvent) messageList.get(0)).getColor() == (workerPlacedEvent.getColor()) );
        assertTrue( ((WorkerPlacedEvent) messageList.get(0)).success() == (workerPlacedEvent.success()) );
        assertTrue( nicknameList.get(0) == null);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();

    }

    /**
     * Check if update(SelectWorkerEvent, String) can send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateSelectWorker() {
        String player = playerList.get(0);
        SelectWorkerEvent selectWorkerEvent;
        WorkerSelectedEvent workerSelectedEvent;


        /* Game isn't started */
        myModel.setGameStarted(false);
        selectWorkerEvent = new SelectWorkerEvent("[Worker]\t1");
        workerSelectedEvent = new WorkerSelectedEvent(player, selectWorkerEvent.getWorkerId(), true);
        myModel.setWorkerSelectedEvent(workerSelectedEvent);
        controller.update(selectWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started but selectedWorkerEvent == null */
        myModel.setGameStarted(true);
        selectWorkerEvent = new SelectWorkerEvent("[Worker]\t1");
        workerSelectedEvent = null;
        myModel.setWorkerSelectedEvent(workerSelectedEvent);
        controller.update(selectWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started, selectedWorkerEvent != null but with success == false */
        myModel.setGameStarted(true);
        selectWorkerEvent = new SelectWorkerEvent("[Worker]\t1");
        workerSelectedEvent = new WorkerSelectedEvent(player, selectWorkerEvent.getWorkerId(), false);
        myModel.setWorkerSelectedEvent(workerSelectedEvent);
        controller.update(selectWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof WorkerSelectedEvent );
        assertTrue( ((WorkerSelectedEvent) messageList.get(0)).getPlayerNickname().equals(workerSelectedEvent.getPlayerNickname()));
        assertTrue( ((WorkerSelectedEvent) messageList.get(0)).getWorker().equals(workerSelectedEvent.getWorker()));
        assertTrue( ((WorkerSelectedEvent) messageList.get(0)).success() == workerSelectedEvent.success() );
        assertTrue( nicknameList.get(0) == null );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started, selectedWorkerEvent != null with success == true */
        myModel.setGameStarted(true);
        selectWorkerEvent = new SelectWorkerEvent("[Worker]\t1");
        workerSelectedEvent = new WorkerSelectedEvent(player, selectWorkerEvent.getWorkerId(), true);
        myModel.setWorkerSelectedEvent(workerSelectedEvent);
        controller.update(selectWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof WorkerSelectedEvent );
        assertTrue( ((WorkerSelectedEvent) messageList.get(0)).getPlayerNickname().equals(workerSelectedEvent.getPlayerNickname()));
        assertTrue( ((WorkerSelectedEvent) messageList.get(0)).getWorker().equals(workerSelectedEvent.getWorker()));
        assertTrue( ((WorkerSelectedEvent) messageList.get(0)).success() == workerSelectedEvent.success() );
        assertTrue( nicknameList.get(0) == null );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


    }

    /**
     * Check if update(MoveWorkerEvent, String) can send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      setMoveOutcome( StateType ) of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateMoveWorker() {
        String player = playerList.get(0);
        MoveWorkerEvent moveWorkerEvent;
        WorkerMovedEvent workerMovedEvent1;
        WorkerMovedEvent workerMovedEvent2;

        myModel.setMoveOutcome(MoveOutcomeType.NONE);

        /* Game isn't started */
        myModel.setGameStarted(false);
        moveWorkerEvent = new MoveWorkerEvent("[Worker]\t1", 0,0);
        workerMovedEvent1 = new WorkerMovedEvent(moveWorkerEvent.getWorkerId(), moveWorkerEvent.getX(), moveWorkerEvent.getY(), 1,1, MoveOutcomeType.NONE);
        workerMovedEvent2 = new WorkerMovedEvent("[Worker]\t1", 3,3,4,4,MoveOutcomeType.NONE);
        myModel.addWorkerMovedEvent(workerMovedEvent1);
        myModel.addWorkerMovedEvent(workerMovedEvent2);
        controller.update(moveWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started but PLayingPlayerState != MOVEMENT */
        myModel.setGameStarted(true);
        myModel.setPlayingPlayerState(StateType.CONSTRUCTION);
        moveWorkerEvent = new MoveWorkerEvent("[Worker]\t1", 0,0);
        workerMovedEvent1 = new WorkerMovedEvent(moveWorkerEvent.getWorkerId(), moveWorkerEvent.getX(), moveWorkerEvent.getY(), 1,1, MoveOutcomeType.NONE);
        workerMovedEvent2 = new WorkerMovedEvent("[Worker]\t1", 3,3,4,4,MoveOutcomeType.NONE);
        myModel.addWorkerMovedEvent(workerMovedEvent1);
        myModel.addWorkerMovedEvent(workerMovedEvent2);
        controller.update(moveWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started, PLayingPlayerState == MOVEMENT but workerMovedEventList == null*/
        myModel.setGameStarted(true);
        myModel.setPlayingPlayerState(StateType.MOVEMENT);
        moveWorkerEvent = new MoveWorkerEvent("[Worker]\t1", 0,0);
        controller.update(moveWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started, PLayingPlayerState == MOVEMENT but workerMovedEventList != null*/
        myModel.setGameStarted(true);
        myModel.setPlayingPlayerState(StateType.MOVEMENT);
        moveWorkerEvent = new MoveWorkerEvent("[Worker]\t1", 0,0);
        workerMovedEvent1 = new WorkerMovedEvent(moveWorkerEvent.getWorkerId(), moveWorkerEvent.getX(), moveWorkerEvent.getY(), 1,1, MoveOutcomeType.NONE);
        workerMovedEvent2 = new WorkerMovedEvent("[Worker]\t1", 3,3,4,4,MoveOutcomeType.NONE);
        myModel.addWorkerMovedEvent(workerMovedEvent1);
        myModel.addWorkerMovedEvent(workerMovedEvent2);
        controller.update(moveWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 2);
        assertTrue( nicknameList.size() == 2);
        assertTrue( messageList.get(0) instanceof WorkerMovedEvent );
        assertTrue(((WorkerMovedEvent) messageList.get(0)).getWorker().equals(workerMovedEvent1.getWorker()));
        assertTrue( ((WorkerMovedEvent) messageList.get(0)).getInitialX() == workerMovedEvent1.getInitialX() );
        assertTrue( ((WorkerMovedEvent) messageList.get(0)).getInitialY() == workerMovedEvent1.getInitialY() );
        assertTrue( ((WorkerMovedEvent) messageList.get(0)).getFinalX() == workerMovedEvent1.getFinalX() );
        assertTrue( ((WorkerMovedEvent) messageList.get(0)).getFinalY() == workerMovedEvent1.getFinalY() );
        assertTrue( ((WorkerMovedEvent) messageList.get(0)).getMoveOutcome() == workerMovedEvent1.getMoveOutcome() );
        assertTrue( nicknameList.get(0) == null );
        assertTrue( messageList.get(1) instanceof WorkerMovedEvent );
        assertTrue(((WorkerMovedEvent) messageList.get(1)).getWorker().equals(workerMovedEvent2.getWorker()));
        assertTrue( ((WorkerMovedEvent) messageList.get(1)).getInitialX() == workerMovedEvent2.getInitialX() );
        assertTrue( ((WorkerMovedEvent) messageList.get(1)).getInitialY() == workerMovedEvent2.getInitialY() );
        assertTrue( ((WorkerMovedEvent) messageList.get(1)).getFinalX() == workerMovedEvent2.getFinalX() );
        assertTrue( ((WorkerMovedEvent) messageList.get(1)).getFinalY() == workerMovedEvent2.getFinalY() );
        assertTrue( ((WorkerMovedEvent) messageList.get(1)).getMoveOutcome() == workerMovedEvent2.getMoveOutcome() );
        assertTrue( nicknameList.get(1) == null );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


    }

    /**
     * Check if update(BuildBlockEvent, String) can send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      setMoveOutcome( StateType ) of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateBuildBlockEvent() {
        String player = playerList.get(0);
        BuildBlockEvent buildBlockEvent;
        BlockBuiltEvent blockBuiltEvent;

        myModel.setMoveOutcome(MoveOutcomeType.NONE);

        /* Game isn't started */
        myModel.setGameStarted(false);
        buildBlockEvent = new BuildBlockEvent("[Worker]\t1", 0,0,PlaceableType.BLOCK);
        blockBuiltEvent = new BlockBuiltEvent(buildBlockEvent.getX(), buildBlockEvent.getY(), buildBlockEvent.getBlockType(), MoveOutcomeType.NONE);
        myModel.setBlockBuildEvent(blockBuiltEvent);
        controller.update(buildBlockEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started but PLayingPlayerState != CONSTRUCTION */
        myModel.setGameStarted(true);
        myModel.setPlayingPlayerState(StateType.MOVEMENT);
        buildBlockEvent = new BuildBlockEvent("[Worker]\t1", 0,0,PlaceableType.BLOCK);
        blockBuiltEvent = new BlockBuiltEvent(buildBlockEvent.getX(), buildBlockEvent.getY(), buildBlockEvent.getBlockType(), MoveOutcomeType.NONE);
        myModel.setBlockBuildEvent(blockBuiltEvent);
        controller.update(buildBlockEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started, PLayingPlayerState == CONSTRUCTION but blockBuildEvent == null*/
        myModel.setGameStarted(true);
        myModel.setPlayingPlayerState(StateType.CONSTRUCTION);
        buildBlockEvent = new BuildBlockEvent("[Worker]\t1", 0,0,PlaceableType.BLOCK);
        blockBuiltEvent = null;
        myModel.setBlockBuildEvent(blockBuiltEvent);
        controller.update(buildBlockEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started, PLayingPlayerState == CONSTRUCTION and blockBuildEvent != null */
        myModel.setGameStarted(true);
        myModel.setPlayingPlayerState(StateType.CONSTRUCTION);
        buildBlockEvent = new BuildBlockEvent("[Worker]\t1", 0,0,PlaceableType.BLOCK);
        blockBuiltEvent = new BlockBuiltEvent(buildBlockEvent.getX(), buildBlockEvent.getY(), buildBlockEvent.getBlockType(), MoveOutcomeType.NONE);
        myModel.setBlockBuildEvent(blockBuiltEvent);
        controller.update(buildBlockEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof BlockBuiltEvent );
        assertTrue( ((BlockBuiltEvent) messageList.get(0)).getX() == buildBlockEvent.getX() );
        assertTrue( ((BlockBuiltEvent) messageList.get(0)).getY() == buildBlockEvent.getY() );
        assertTrue( ((BlockBuiltEvent) messageList.get(0)).getBlockType() == buildBlockEvent.getBlockType() );
        assertTrue( ((BlockBuiltEvent) messageList.get(0)).getMoveOutcome() == blockBuiltEvent.getMoveOutcome() );
        assertTrue( nicknameList.get(0) == null );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();

    }

    /**
     * Check if update(RemoveWorkerEvent, String) can send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateRemoveWorker() {
        String player = playerList.get(0);
        RemoveWorkerEvent removeWorkerEvent;
        WorkerRemovedEvent workerRemovedEvent;

        /* Game isn't started */
        myModel.setGameStarted(false);
        removeWorkerEvent = new RemoveWorkerEvent("[Worker]\t1", 0,0);
        workerRemovedEvent = new WorkerRemovedEvent(removeWorkerEvent.getWorkerId(), removeWorkerEvent.getX(), removeWorkerEvent.getY(), false);
        myModel.setWorkerRemovedEvent(workerRemovedEvent);
        controller.update(removeWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started, but workerRemoveEvent == null */
        myModel.setGameStarted(true);
        removeWorkerEvent = new RemoveWorkerEvent("[Worker]\t1", 0,0);
        workerRemovedEvent = null;
        myModel.setWorkerRemovedEvent(workerRemovedEvent);
        controller.update(removeWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started and workerRemoveEvent != null */
        myModel.setGameStarted(true);
        removeWorkerEvent = new RemoveWorkerEvent("[Worker]\t1", 0,0);
        workerRemovedEvent = new WorkerRemovedEvent(removeWorkerEvent.getWorkerId(), removeWorkerEvent.getX(), removeWorkerEvent.getY(), false);
        myModel.setWorkerRemovedEvent(workerRemovedEvent);
        controller.update(removeWorkerEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof WorkerRemovedEvent );
        assertTrue(((WorkerRemovedEvent) messageList.get(0)).getWorker() == null);
        assertTrue( ((WorkerRemovedEvent) messageList.get(0)).getX() == 0 );
        assertTrue( ((WorkerRemovedEvent) messageList.get(0)).getY() == 0 );
        assertTrue(!((WorkerRemovedEvent) messageList.get(0)).success());
        assertTrue( nicknameList.get(0) == null );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


    }

    /**
     * Check if update(RemoveBlockEvent, String) can send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateRemoveBlockEvent() {
        String player = playerList.get(0);
        RemoveBlockEvent removeBlockEvent;
        BlockRemovedEvent blockRemovedEvent;

        /* Game isn't started */
        myModel.setGameStarted(false);
        removeBlockEvent = new RemoveBlockEvent("[Worker]\t1", 0,0);
        blockRemovedEvent = new BlockRemovedEvent(removeBlockEvent.getX(), removeBlockEvent.getY(), PlaceableType.BLOCK, false);
        myModel.setBlockRemovedEvent(blockRemovedEvent);
        controller.update(removeBlockEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started, but blockRemoveEventReturned == null */
        myModel.setGameStarted(true);
        removeBlockEvent = new RemoveBlockEvent("[Worker]\t1", 0,0);
        blockRemovedEvent = null;
        myModel.setBlockRemovedEvent(blockRemovedEvent);
        controller.update(removeBlockEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started and blockRemoveEventReturned != null */
        myModel.setGameStarted(true);
        removeBlockEvent = new RemoveBlockEvent("[Worker]\t1", 0,0);
        blockRemovedEvent = new BlockRemovedEvent(removeBlockEvent.getX(), removeBlockEvent.getY(), PlaceableType.BLOCK, true);
        myModel.setBlockRemovedEvent(blockRemovedEvent);
        controller.update(removeBlockEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof BlockRemovedEvent );
        assertTrue( ((BlockRemovedEvent) messageList.get(0)).getX() == removeBlockEvent.getX() );
        assertTrue( ((BlockRemovedEvent) messageList.get(0)).getY() == removeBlockEvent.getY() );
        assertTrue( ((BlockRemovedEvent) messageList.get(0)).getBlockType() == PlaceableType.BLOCK );
        assertTrue(((BlockRemovedEvent) messageList.get(0)).success());
        assertTrue( nicknameList.get(0) == null );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


    }

    /**
     * Check if update( UndoActionEvent, String ) can send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateUndoActionEvent() {
        String player = playerList.get(0);
        UndoActionEvent undoActionEvent = new UndoActionEvent();

        /* Game isn't started */
        myModel.setGameStarted(false);
        controller.update(undoActionEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started but player isn't playing */
        myModel.setGameStarted(true);
        myModel.setPlayingPlayer("Other Player");
        controller.update(undoActionEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started, player is playing but undoManager isn't active */
        myModel.setGameStarted(true);
        myModel.setPlayingPlayer(player);
        controller.update(undoActionEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();

    }

    /**
     * Check if update( TurnStatusChangeEvent, String ) can active the correct model's function and send the correct event
     * Methods used:        setGameStarter( boolean )   of  Model
     *                      size()                      of  List
     *                      get( int )                  of  List
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateTurnStatusChangeEvent() {
        String player = playerList.get(0);
        TurnStatusChangeEvent turnStatusChangeEvent = new TurnStatusChangeEvent(StateType.CONSTRUCTION);
        TurnStatusChangedEvent turnStatusChangedEvent;

        /* Game isn't started */
        myModel.setGameStarted(false);
        controller.update(turnStatusChangeEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof ErrorMessageEvent );
        assertTrue( nicknameList.get(0).equals(player) );
        assertTrue( !myModel.checkSwitchPlayer );
        assertTrue( !myModel.checkHandlePlayerLoss );
        assertTrue( !myModel.checkHandlePlayerWin );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /* Game is started, undoMove not active, and turnChangedEvent = null */
        myModel.setGameStarted(true);
        turnStatusChangedEvent = null;
        myModel.setTurnStatusChangedEventReturned(turnStatusChangedEvent);
        controller.update(turnStatusChangeEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);
        assertTrue( !myModel.checkSwitchPlayer );
        assertTrue( !myModel.checkHandlePlayerLoss );
        assertTrue( !myModel.checkHandlePlayerWin );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /*  Game is started, undoMove not active, and turnChangedEvent != null but with success == false
            and MoveOutOfType.TURN_SWITCHED */
        myModel.setGameStarted(true);
        turnStatusChangedEvent = new TurnStatusChangedEvent(player, StateType.CONSTRUCTION, false);
        myModel.setTurnStatusChangedEventReturned(turnStatusChangedEvent);
        myModel.setMoveOutcome(MoveOutcomeType.TURN_SWITCHED);
        controller.update(turnStatusChangeEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 2);
        assertTrue( nicknameList.size() == 2);
        assertTrue( messageList.get(0) instanceof TurnStatusChangedEvent );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).getPlayerNickname().equals(turnStatusChangedEvent.getPlayerNickname()) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).getState().equals(turnStatusChangedEvent.getState()) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).success() == turnStatusChangedEvent.success() );
        assertTrue( nicknameList.get(0) == null );
        assertTrue( messageList.get(1) instanceof TurnStatusChangedEvent );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(1)).getPlayerNickname().equals(turnStatusChangedEvent.getPlayerNickname()) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(1)).getState().equals(StateType.CONSTRUCTION) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(1)).success() );
        assertTrue( nicknameList.get(1) == null );
        assertTrue( !myModel.checkSwitchPlayer );
        assertTrue( !myModel.checkHandlePlayerLoss );
        assertTrue( !myModel.checkHandlePlayerWin );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /*  Game is started, undoMove active, and turnChangedEvent != null but with success == true and StateType != NONE
            and MoveOutOfType.TURN_OVER */
        myModel.setGameStarted(true);
        turnStatusChangedEvent = new TurnStatusChangedEvent(player, StateType.MOVEMENT, true);
        myModel.setTurnStatusChangedEventReturned(turnStatusChangedEvent);
        myModel.setMoveOutcome(MoveOutcomeType.TURN_OVER);
        controller.update(turnStatusChangeEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof TurnStatusChangedEvent );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).getPlayerNickname().equals(turnStatusChangedEvent.getPlayerNickname()) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).getState().equals(turnStatusChangedEvent.getState()) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).success() == turnStatusChangedEvent.success() );
        assertTrue( nicknameList.get(0) == null );
        assertTrue( myModel.checkSwitchPlayer );
        assertTrue( !myModel.checkHandlePlayerLoss );
        assertTrue( !myModel.checkHandlePlayerWin );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /*  Game is started, undoMove active, and turnChangedEvent != null but with success == true and StateType != NONE
            and MoveOutOfType.lOSS */
        myModel.setGameStarted(true);
        turnStatusChangedEvent = new TurnStatusChangedEvent(player, StateType.MOVEMENT, true);
        myModel.setTurnStatusChangedEventReturned(turnStatusChangedEvent);
        myModel.setMoveOutcome(MoveOutcomeType.LOSS);
        controller.update(turnStatusChangeEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof TurnStatusChangedEvent );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).getPlayerNickname().equals(turnStatusChangedEvent.getPlayerNickname()) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).getState().equals(turnStatusChangedEvent.getState()) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).success() == turnStatusChangedEvent.success() );
        assertTrue( nicknameList.get(0) == null );
        assertTrue( myModel.checkSwitchPlayer );
        assertTrue( myModel.checkHandlePlayerLoss );
        assertTrue( !myModel.checkHandlePlayerWin );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /*  Game is started, undoMove active, and turnChangedEvent != null but with success == true and StateType != NONE
            and MoveOutOfType.WIN */
        myModel.setGameStarted(true);
        turnStatusChangedEvent = new TurnStatusChangedEvent(player, StateType.MOVEMENT, true);
        myModel.setTurnStatusChangedEventReturned(turnStatusChangedEvent);
        myModel.setMoveOutcome(MoveOutcomeType.WIN);
        controller.update(turnStatusChangeEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof TurnStatusChangedEvent );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).getPlayerNickname().equals(turnStatusChangedEvent.getPlayerNickname()) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).getState().equals(turnStatusChangedEvent.getState()) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).success() == turnStatusChangedEvent.success() );
        assertTrue( nicknameList.get(0) == null );
        assertTrue( !myModel.checkSwitchPlayer );
        assertTrue( !myModel.checkHandlePlayerLoss );
        assertTrue( myModel.checkHandlePlayerWin );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();


        /*  Game is started, undoMove active, and turnChangedEvent != null but with success == true and StateType != NONE
            and MoveOutOfType.ANY */
        myModel.setGameStarted(true);
        turnStatusChangedEvent = new TurnStatusChangedEvent(player, StateType.MOVEMENT, true);
        myModel.setTurnStatusChangedEventReturned(turnStatusChangedEvent);
        myModel.setMoveOutcome(MoveOutcomeType.ANY);
        controller.update(turnStatusChangeEvent, player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 1);
        assertTrue( nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof TurnStatusChangedEvent );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).getPlayerNickname().equals(turnStatusChangedEvent.getPlayerNickname()) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).getState().equals(turnStatusChangedEvent.getState()) );
        assertTrue( ((TurnStatusChangedEvent) messageList.get(0)).success() == turnStatusChangedEvent.success() );
        assertTrue( nicknameList.get(0) == null );
        assertTrue( !myModel.checkSwitchPlayer );
        assertTrue( !myModel.checkHandlePlayerLoss );
        assertTrue( !myModel.checkHandlePlayerWin );


        // clear myObserver and myModel
        myObserver.clear();
        myModel.clear();

    }

    /**
     * Check if update( Object ) can print an Error String
     *
     * Black Box and White Box
     */
    @Test
    void testUpdateObject() {
        Object object = new Object();

        controller.update(object);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == 0);
        assertTrue( nicknameList.size() == 0);

    }

    /**
     * Checks if notifyFromModel(Object) can correctly notify
     * Methods used:        size()              of  List
     *                      get( int )          of  List
     *
     * Black Box and White Box
     */
    @Test
    void notifyFromModel() {
        Object o = new Object();

        controller.notifyFromModel(o);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue(messageList.size() == 1);
        assertTrue(nicknameList.size() == 1);
        assertTrue( messageList.get(0).equals(o) );
        assertTrue( nicknameList.get(0) == null );
    }

    /**
     * Checks if notifyFromModel(Object, String) can correctly notify
     * Methods used:        size()              of  List
     *                      get( int )          of  List
     *
     * Black Box and White Box
     */
    @Test
    void testNotifyFromModel() {
        Object o = new Object();
        String string = "string";

        controller.notifyFromModel(o, string);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue(messageList.size() == 1);
        assertTrue(nicknameList.size() == 1);
        assertTrue( messageList.get(0).equals(o) );
        assertTrue( nicknameList.get(0).equals(string) );
    }

    /**
     * Checks if gameOver() can correctly notify
     * Methods used:        size()              of  List
     *                      get( int )          of  List
     *                      contains( Object )  of  List
     *
     * Black Box and White Box
     */
    @Test
    void gameOver() {

        controller.gameOver();

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue( messageList.size() == playerList.size() );
        assertTrue( nicknameList.size() == playerList.size() );
        for ( Object message : messageList ) {
            assertTrue( message instanceof GameOverEvent );
        }
        for (int i = 0; i < playerList.size(); i++) {
            assertTrue( nicknameList.contains(playerList.get(i)) );
        }
    }

    /**
     * Checks if gameOverMessage( String ) can correctly notify
     * Methods used:        size()          of  List
     *                      get( int )      of  List
     *
     * Black Box and White Box
     */
    @Test
    void gameOverMessage() {
        String player = "Player1";

        controller.gameOverMessage(player);

        messageList = myObserver.getObjectList();
        nicknameList = myObserver.getStringList();

        assertTrue(messageList.size() == 1);
        assertTrue(nicknameList.size() == 1);
        assertTrue( messageList.get(0) instanceof GameOverEvent );
        assertTrue( nicknameList.get(0).equals(player) );
    }

    /**
     * Checks if disconnectPlayer( String ) can correctly notify
     * Methods used:        size()          of  List
     *                      get( int )      of  List
     *
     * Black Box and White Box
     */
    @Test
    void disconnectPlayer() {
        String player = "Player1";

        controller.disconnectPlayer(player);

        messageList = myObserver.getObjectList();
        playerList = myObserver.getStringList();

        assertTrue(messageList.size() == 1);
        assertTrue(playerList.size() == 1);
        assertTrue(messageList.get(0) instanceof ServerQuitEvent);
        assertTrue(playerList.get(0).equals(player));
    }

    /**
     * Checks if printControlMessage( String ) prints the correct String
     *
     * Black Box and White Box
     */
    @Test
    void printControlMessage() {
        final String MESSAGE_TO_STRING = "Hello World!";

        System.out.println();

        System.out.printf("%-15s","[Controller]:");
        controller.printControlMessage(MESSAGE_TO_STRING);

        System.out.printf("%-15s" + MESSAGE_TO_STRING + "\n","[Check]:");
    }
}