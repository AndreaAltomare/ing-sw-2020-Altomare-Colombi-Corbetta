package it.polimi.ingsw.view.clientSide;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.model.CardInfo;
import it.polimi.ingsw.model.MoveOutcomeType;
import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.StateType;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ViewSender;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.GUIViewer;
import it.polimi.ingsw.view.events.*;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

import java.util.*;

public class ViewTester implements ViewSender {


    //#####MODIFICABILI PER TESTARE DIVERSE CONFIGURAZIONI####

    //attributo per far eseguire le waiting
    private final static boolean addWait = true;
    private final static boolean sendTestMessages = false;

    private final static boolean stopAtLogin = false;
    private final static boolean stopAtWaiting = false;
    private final static boolean stopAtNewGame = false;
    private final static boolean stopAtGamePreparation = false;
    private final static boolean stopAtPlaying = false;
    private final static boolean stopAtGameOver = false;

    private final static boolean invalidNickname = false;
    private final static boolean requirePlayerNumber = true;


    private final static boolean setDefaultChallenger = true;
    private final static boolean isFirstCardChooser = true;
    private final static boolean isSecondCardChooser = false;

    private final static boolean isFirstPlacer = false;
    private final static boolean isSecondPlacer = true;
    private final static boolean isThirdPlacer = false;

    private final static boolean validPlacing = true;

    private final static boolean winning = true;
    private final static int turnNumber = 10;
    private final static int turnLoose = 3;


    //####ATTRIBUTI RAPPRESENTANTI LO STATO INTERNO DEL SERVER -O QUALCOSA DI SIMILE####
    private String playerName = "";
    private int numberPlayers = 0;
    private List<String> players;
    List<CardInfo> deck;
    List<CardInfo> available;
    //Serve per posizionare i worker del giocatore.
    private int myWorkerId = 10;


    //####ATTRIBUTI PER IL FUNZIONAMENTO DELLA CLASSE####
    private final Object waitingObj = new Object();
    private Object lock = new Object();
    private Object selected = new Object();
    private Object moved = new Object();
    private View view = new View(null, null);

    //####MOSSE####
    private String[][] workers = new String[2][2];

    private class Mossa{
        int player;
        int worker;
        int xMov;
        int yMov;
        int xBuild;
        int yBuild;
        PlaceableType blockType;

        Mossa(int player, int worker, int xMov, int yMov, int xBuild, int yBuild, PlaceableType blockType){
            this.player = player;
            this.worker = worker;
            this.xMov = xMov;
            this.yMov = yMov;
            this.xBuild = xBuild;
            this.yBuild = yBuild;
            this.blockType = blockType;
        }

        String getPlayer(){
            return players.get(player+1);
        }

        String getWorker(){
            return workers[player][worker];
        }

        int getxMov(){
            return xMov;
        }

        int getyMov(){
            return yMov;
        }

        int getxBuild(){
            return xBuild;
        }

        int getyBuild(){
            return yBuild;
        }

        PlaceableType getBlockType(){
            return blockType;
        }
    }
    private List<Mossa> mosse;


    //####MAIN####
    public static void main(String[] args) {
        new ViewTester().myMain2();
        System.out.println("Fine Test");
        end();
        System.exit(0);
    }

    //####MYMAIN####
    //funzione col flusso di controllo del "test"
    private void myMain2(){

        //Inizializzo la View
        initialization();

        waiting();

        if(stopAtLogin)
            return;

        //Vado allo stato di login
        loginStatus();

        if(requirePlayerNumber) {
            numberPlayerRequired();
        }

        if(stopAtWaiting)
            return;

        waitingStatus();

        if(stopAtNewGame)
            return;

        newGameStatus();

        if(stopAtGamePreparation)
            return;

        gamePreparationStatus();

        if(stopAtPlaying)
            return;

        playingStatus();

        if(stopAtGameOver)
            return;

        String winner;
        if(winning){
            winner = playerName;
        }else{
            winner = players.get(1);
        }
        view.update(new PlayerWinEvent(winner, "You won !!!", "Ohcu, you lost..."));
        waiting();

        gameOverStatus();

    }

    //####STRUCTURAL METHODS####
    private void myWait(){
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void myWaitSelected(){
        synchronized (selected){
            try {
                selected.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void myWaitMove(){
        synchronized (moved){
            try {
                moved.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void end(){
        Viewer.exitAll();
    }

    private void waiting(){
        if(addWait){
            synchronized (waitingObj) {
                try {
                    waitingObj.wait(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void myNotify(){
        synchronized (lock){
            lock.notifyAll();
        }
    }

    //####HELPER METHODS####

    private void initialization(){
        //new TerminalViewer().start();
        new GUIViewer().start();
        //new CLIViewer().start();

        Executer.setSender(this);
        ViewStatus.init();
    }

    //####REP HEPLER####
    private void initDeck(){
        deck = new ArrayList<CardInfo>();

        deck.add(new CardInfo("Apollo", "God of Music", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));
        deck.add(new CardInfo("Artemis", "Goddess of the Hunt", "Your Move: Your Worker may move one additional time, but not back to its initial space."));
        deck.add(new CardInfo("Athena", "Goddess of Wisdom", "Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn."));
        deck.add(new CardInfo("Atlas", "Titan Shouldering the Heavens", "Your Build: Your Worker may build a dome at any level."));
        deck.add(new CardInfo("Demeter", "Goddess of the Harvest", "Your Build: Your Worker may build one additional time, but not on the same space."));
        deck.add(new CardInfo("Hephaestus", "God of Blacksmiths", "Your Build: Your Worker may build one additional block (not dome) on top of your first block."));
        deck.add(new CardInfo("Minotaur", "Bull-headed Monster", "Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level."));
        deck.add(new CardInfo("Pan", "God of the Wild", "Win Condition: You also win if your Worker moves down two or more levels."));
        deck.add(new CardInfo("Prometheus", "Titan Benefactor of Mankind", "Your Turn: If your Worker does not move up, it may build both before and after moving."));
        deck.add(new CardInfo("generalGod", "", "DON'T CHOOSE ME."));
    }

    private void setAvailable(List<String> nameList){
        available = new ArrayList<>(3);
        for (CardInfo card: deck) {
            if(nameList.contains(card.getName())){
                System.out.println("[SERVER] Challenger choosed " + card.getName());
                available.add(card);
            }
        }
    }

    private void removeAvailable(String name){
        List<CardInfo> tmp = new ArrayList<>(available);
        available = new ArrayList<>(3);
        for(CardInfo card: tmp){
            if(!card.getName().equals(name))
                available.add(card);
        }
    }

    //####STATUS METHODS####

    private void loginStatus(){

        view.update((NextStatusEvent)new NextStatusEvent("vai alla login"));

        //Qua viene richiesto il nickname...
        //Se invalidNickname è impostato a true, allora continua a rispondere con un invalid nickname, altrimenti il controllo torna a questo thread che esce da myWait

        myWait();

    }

    private void numberPlayerRequired(){

        view.update((RequirePlayersNumberEvent) new RequirePlayersNumberEvent());
        myWait();

    }

    private void waitingStatus(){
        //Va allo stato di waiting
        view.update((NextStatusEvent)new NextStatusEvent("vai alla wait"));

        waiting();

        //se sendTestMessages invia dei messaggi di test
        if(sendTestMessages){
            view.update((MessageEvent) new MessageEvent("test fromServerMEssage"));
            view.update((ErrorMessageEvent) new ErrorMessageEvent("test serverError"));
            ViewMessage.populateAndSend("test executerError", ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
            ViewMessage.populateAndSend("test fatalError", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);

            waiting();

            ViewMessage.populateAndSend("test async", ViewMessage.MessageType.FROM_SERVER_MESSAGE);
        }
    }

    public void newGameStatus(){
        //Va nella fase di "new game"
        view.update((NextStatusEvent)new NextStatusEvent("newGame"));

        //invia il ServerSendDataEvent
        {
            int boardXSize = 5;
            int boardYSize = 5;
            players = new ArrayList<String>(3);
            Map<String,List<String>> workersToPlayer = new HashMap<String, List<String>>();

            players.add(playerName);
            players.add("player1");
            if(numberPlayers>=3)
                players.add("player2");


            view.update((ServerSendDataEvent) new ServerSendDataEvent(boardXSize, boardYSize, players, workersToPlayer));
        }
    }

    public void gamePreparationStatus(){
        //Passa allo stato di "Game Preparation"
        view.update((NextStatusEvent)new NextStatusEvent("go to gamePreparation"));

        //Managing the choice of the cards

        String challenger;
        String player = "";

        initDeck();

        if(setDefaultChallenger)
            challenger = playerName;
        else
            challenger = players.get(1);

        view.update(new CardsInformationEvent( deck, challenger, player));

        if(setDefaultChallenger) {
            myWait();
        }else{
            List<String> tmpName = new ArrayList<>();
            tmpName.add(deck.get(0).getName());
            tmpName.add(deck.get(1).getName());
            if(numberPlayers>=3)
                tmpName.add(deck.get(2).getName());
            setAvailable(tmpName);
        }

        if(isFirstCardChooser)
            player = playerName;
        else {
            player = players.get(1);
        }

        view.update(new CardsInformationEvent( available, challenger, player));

        if(isFirstCardChooser) {
            myWait();
            if(numberPlayers == 2){
                String playerCard = available.remove(0).getName();
                view.update(new CardSelectedEvent(playerCard, players.get(1), true));
            }
        }else{
            String playerCard;
            if(numberPlayers == 2){
                playerCard = available.remove(0).getName();
                view.update(new CardSelectedEvent(playerCard, player, true));

                playerCard = available.remove(0).getName();
                view.update(new CardSelectedEvent(playerCard, playerName, true));
            }else{
                playerCard = available.remove(0).getName();
                view.update(new CardSelectedEvent(playerCard, player, true));
            }
        }

        if(numberPlayers >= 3){
            if(isSecondCardChooser){
                player = playerName;
            }else{
                player = players.get(2);
            }

            view.update(new CardsInformationEvent( available, challenger, player));

            if(isSecondCardChooser) {
                myWait();
                view.update(new CardSelectedEvent(available.get(0).getName(), players.get(2), true));
            }else{
                String playerCard;
                playerCard = available.remove(0).getName();
                view.update(new CardSelectedEvent(playerCard, player, true));

                if(isFirstCardChooser){
                    playerCard = available.remove(0).getName();
                    view.update(new CardSelectedEvent(playerCard, players.get(1), true));
                }else{
                    playerCard = available.remove(0).getName();
                    view.update(new CardSelectedEvent(playerCard, playerName, true));
                }
            }
        }

        //Placing the workers

        if(isFirstPlacer) {
            view.update(new RequirePlaceWorkersEvent(playerName));
            myWait();
        }else{
            view.update(new RequirePlaceWorkersEvent(players.get(1)));
            waiting();
            view.update(new WorkerPlacedEvent("[Worker]\t1", 0, 0, true));
            workers[0][0] = "[Worker]\t1";
        }

        if(isSecondPlacer) {
            view.update(new RequirePlaceWorkersEvent(ViewNickname.getMyNickname()));
            myWait();
        }else{
            if(numberPlayers == 2){
                view.update(new RequirePlaceWorkersEvent(players.get(1)));
                waiting();
                view.update(new WorkerPlacedEvent("[Worker]\t2", 0, 1, true));
                workers[0][0] = "[Worker]\t2";
            }else{
                view.update(new RequirePlaceWorkersEvent(players.get(2)));
                waiting();
                view.update(new WorkerPlacedEvent("[Worker]\t2", 0, 1, true));
                workers[1][0] = "[Worker]\t2";
            }
        }
        if(numberPlayers >= 3){
            if(isThirdPlacer) {
                view.update(new RequirePlaceWorkersEvent(ViewNickname.getMyNickname()));
                myWait();
            }else{
                if(isFirstPlacer){
                    view.update(new RequirePlaceWorkersEvent(players.get(1)));
                    waiting();
                    view.update(new WorkerPlacedEvent("[Worker]\t3", 0, 2, true));
                    workers[0][0] = "[Worker]\t3";
                }else{
                    view.update(new RequirePlaceWorkersEvent(players.get(2)));
                    waiting();
                    view.update(new WorkerPlacedEvent("[Worker]\t3", 0, 2, true));
                    workers[1][0] = "[Worker]\t3";
                }
            }
        }


        //Posiziona il secondo Worker

        if(isFirstPlacer) {
            view.update(new RequirePlaceWorkersEvent(playerName));
            myWait();
        }else{
            view.update(new RequirePlaceWorkersEvent(players.get(1)));
            waiting();
            view.update(new WorkerPlacedEvent("[Worker]\t4", 1, 0, true));
            workers[0][1] = "[Worker]\t4";
        }

        if(isSecondPlacer) {
            view.update(new RequirePlaceWorkersEvent(ViewNickname.getMyNickname()));
            myWait();
        }else{
            if(numberPlayers == 2){
                view.update(new RequirePlaceWorkersEvent(players.get(1)));
                waiting();
                view.update(new WorkerPlacedEvent("[Worker]\t5", 1, 1, true));
                workers[0][1] = "[Worker]\t5";
            }else{
                view.update(new RequirePlaceWorkersEvent(players.get(2)));
                waiting();
                view.update(new WorkerPlacedEvent("[Worker]\t5", 1, 1, true));
                workers[1][1] = "[Worker]\t5";
            }
        }

        if(numberPlayers>=3){
            if(isThirdPlacer) {
                view.update(new RequirePlaceWorkersEvent(ViewNickname.getMyNickname()));
                myWait();
            }else{
                if(isFirstPlacer){
                    view.update(new RequirePlaceWorkersEvent(players.get(1)));
                    waiting();
                    view.update(new WorkerPlacedEvent("[Worker]\t6", 1, 2, true));
                    workers[0][1] = "[Worker]\t6";
                }else{
                    view.update(new RequirePlaceWorkersEvent(players.get(2)));
                    waiting();
                    view.update(new WorkerPlacedEvent("[Worker]\t6", 1, 2, true));
                    workers[1][1] = "[Worker]\t6";
                }
            }
        }
    }

    private void playingStatus(){
        //Go to playing status
        view.update((NextStatusEvent) new NextStatusEvent("Go to playing"));

        initMosse();

        waiting();

        int actual;
        for(int turn = 0; turn < Math.min(turnNumber, mosse.size()/2); turn ++){
            for(int i = 0; i<numberPlayers; i++){
                actual = 2*turn + i;
                if(actual == turnLoose){
                    view.update(new PlayerLoseEvent(playerName, "You lost! KEK"));
                }
                if(i==numberPlayers-1){
                    if(actual< turnLoose)
                        myTurn();
                }else{
                    opponentTurn(mosse.get(actual));
                }
            }
        }
    }

    public void gameOverStatus(){
        view.update((NextStatusEvent) new NextStatusEvent("Go to gameOver"));
        waiting();
    }

    //####SEND METHODS####

    public void send(SetNicknameEvent event){
        if (invalidNickname){
            view.update(new InvalidNicknameEvent());
            System.out.println("Invalid nickname");
        } else{
            playerName = event.getNickname();
            System.out.println("[SERVER] nome impostato a " + playerName);
            myNotify();
        }
    }

    public void send(SetPlayersNumberEvent event){
        numberPlayers = event.getNumberOfPlayers();
        System.out.println("[SERVER] numero di giocatori impostato a " + numberPlayers);
        myNotify();
    }

    public void send(CardsChoosingEvent event){
        setAvailable(event.getCards());
        myNotify();
    }

    public void send(CardSelectionEvent event){
        view.update(new CardSelectedEvent(event.getCardName(), playerName, true));
        removeAvailable(event.getCardName());
        System.out.println("[SERVER] il giocatore ha scelto " + event.getCardName());
        myNotify();
    }

    public void send(SelectWorkerEvent event){
        view.update(new WorkerSelectedEvent(ViewNickname.getMyNickname(), event.getWorkerId(), true));
        synchronized (selected) {
            selected.notifyAll();
        }
        myNotify();
    }

    @Override
    public void send(EventObject event) {
        myNotify();
    }

    public void send(PlaceWorkerEvent event){
        view.update(new WorkerPlacedEvent("[Worker]\t" + String.valueOf(myWorkerId), event.getX(), event.getY(), validPlacing));
        myWorkerId++;
        myNotify();
    }

    public void send(TurnStatusChangeEvent event){
        view.update(new TurnStatusChangedEvent(playerName, event.getTurnStatus(), true));
        System.out.println("Recived: "  + event.getTurnStatus());
        if(event.getTurnStatus() == StateType.MOVEMENT)
            synchronized (moved){
                moved.notifyAll();
            }
        myNotify();
    }



    public void send(MoveWorkerEvent event){
        try {
            view.update(new WorkerMovedEvent(event.getWorkerId(), ((ViewWorker)ViewWorker.search(event.getWorkerId())).getPosition().getX(), ((ViewWorker)ViewWorker.search(event.getWorkerId())).getPosition().getY(), event.getX(), event.getY(), MoveOutcomeType.EXECUTED));
        } catch (NotFoundException | WrongViewObjectException e) {
            e.printStackTrace();
        }
        myNotify();
    }

    public void send(BuildBlockEvent event){
        view.update(new BlockBuiltEvent(event.getX(), event.getY(), event.getBlockType(), MoveOutcomeType.EXECUTED));
        myNotify();
    }


    //####TURN METHODS####

    private void initMosse(){
        mosse = new ArrayList<>(32);

        mosse.add(new Mossa(0,0, 0, 1, 0, 0,  PlaceableType.BLOCK));
        mosse.add(new Mossa(1,0, 0, 1, 0, 1,  PlaceableType.BLOCK));
        mosse.add(new Mossa(0,0, 0, 0, 0, 1,  PlaceableType.BLOCK));
        mosse.add(new Mossa(1,0, 0, 1, 0, 1,  PlaceableType.BLOCK));
        mosse.add(new Mossa(0,0, 0, 1, 0, 0,  PlaceableType.BLOCK));
        mosse.add(new Mossa(1,0, 0, 1, 0, 1,  PlaceableType.BLOCK));
        mosse.add(new Mossa(0,0, 0, 0, 0, 1,  PlaceableType.BLOCK));
        mosse.add(new Mossa(1,0, 0, 1, 0, 1,  PlaceableType.BLOCK));
        mosse.add(new Mossa(0,0, 0, 1, 0, 0,  PlaceableType.BLOCK));
        mosse.add(new Mossa(1,0, 0, 1, 0, 1,  PlaceableType.BLOCK));
        mosse.add(new Mossa(0,0, 0, 0, 0, 1,  PlaceableType.BLOCK));
        mosse.add(new Mossa(1,0, 0, 1, 0, 1,  PlaceableType.BLOCK));

    }

    private void opponentTurn(Mossa mossa){
        simulateTurn(mossa.getPlayer(), mossa.getWorker(), mossa.getxMov(), mossa.getyMov(), mossa.getxBuild(), mossa.getyBuild(), mossa.getBlockType());
    }

    private void simulateTurn(String player, String worker, int xMov, int yMov, int xBuild, int yBuild, PlaceableType blockType){
        view.update(new TurnStatusChangedEvent(player, StateType.MOVEMENT, true));
        waiting();
        view.update(new WorkerSelectedEvent(player, worker, true));
        waiting();
        view.update(new TurnStatusChangedEvent(player, StateType.MOVEMENT, true));
        waiting();
        try {
            view.update(new WorkerMovedEvent(worker, ((ViewWorker)ViewWorker.search(worker)).getPosition().getX(), ((ViewWorker)ViewWorker.search(worker)).getPosition().getY(), xMov, yMov, MoveOutcomeType.EXECUTED));
        } catch (NotFoundException | WrongViewObjectException e) {
            view.update(new WorkerMovedEvent(worker, 0, 0, xMov, yMov, MoveOutcomeType.EXECUTED));
        }
        waiting();
        view.update(new TurnStatusChangedEvent(player, StateType.CONSTRUCTION, true));
        waiting();
        view.update(new BlockBuiltEvent(xBuild, yBuild, blockType,  MoveOutcomeType.EXECUTED));
    }

    private void myTurn(){
        view.update(new TurnStatusChangedEvent(playerName, StateType.MOVEMENT, true));
        myWaitSelected();
        view.update(new TurnStatusChangedEvent(playerName, StateType.MOVEMENT, true));
        myWaitMove();
    }

    /*private void myMain() {


        initialization();

        waiting();

        view.update((NextStatusEvent)new NextStatusEvent("vai alla login"));

        //Qua viene richiesto il nickname...
        //Se invalidNickname è impostato, allora continua a rispondere con un invalid nickname, altrimenti il controllo torna a questo thread che esce da myWait

        myWait();

        //Se requirePlayerNumber richiede il numero di giocatori
        if(requirePlayerNumber) {
            view.update((RequirePlayersNumberEvent) new RequirePlayersNumberEvent());
            myWait();
        }

        //Va allo stato di waiting
        view.update((NextStatusEvent)new NextStatusEvent("vai alla wait"));

        waiting();

        //se sendTestMessages invia dei messaggi di test
        if(sendTestMessages){
            view.update((MessageEvent) new MessageEvent("test fromServerMEssage"));
            view.update((ErrorMessageEvent) new ErrorMessageEvent("test serverError"));
            ViewMessage.populateAndSend("test executerError", ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
            ViewMessage.populateAndSend("test fatalError", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);

            waiting();

            ViewMessage.populateAndSend("test async", ViewMessage.MessageType.FROM_SERVER_MESSAGE);
        }

        //Va nella fase di "new game"
        view.update((NextStatusEvent)new NextStatusEvent("newGame"));

        //invia il ServerSendDataEvent
        {
            int boardXSize = 5;
            int boardYSize = 5;
            List<String> players = new ArrayList<String>();
            Map<String,List<String>> workersToPlayer = new HashMap<String, List<String>>();

            players.add("player1");
            players.add("player2");
            players.add(ViewNickname.getMyNickname());

            List<String> w1 = new ArrayList<String>();
            List<String> w2 = new ArrayList<String>();
            List<String> w3 = new ArrayList<String>();

            w1.add(("1"));
            w1.add(("2"));
            w2.add(("3"));
            w2.add(("4"));
            w3.add(("5"));
            w3.add(("6"));

            view.update((ServerSendDataEvent) new ServerSendDataEvent(boardXSize, boardYSize, players, workersToPlayer));
        }

        //Passa allo stato di "Game Preparation"
        view.update((NextStatusEvent)new NextStatusEvent("go to gamePreparation"));

        //Sending the card information event for the challenger
        {
            List<CardInfo> cards = new ArrayList<CardInfo>();
            String challenger;
            if(setDefaultChallenger)
                challenger = ViewNickname.getMyNickname();
            else
                challenger = "player1";
            String player = "";

            cards.add(new CardInfo("Apollo", "God of Music", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));

            cards.add(new CardInfo("Artemis", "Goddess of the Hunt", "Your Move: Your Worker may move one additional time, but not back to its initial space."));
            //cards.add(new CardInfo("Artemis", "Goddess of the Hunt", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));

            cards.add(new CardInfo("Athena", "Goddess of Wisdom", "Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn."));
            //cards.add(new CardInfo("Athena", "Goddess of Wisdom", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));

            cards.add(new CardInfo("Atlas", "Titan Shouldering the Heavens", "Your Build: Your Worker may build a dome at any level."));
            //cards.add(new CardInfo("Atlas", "Titan Shouldering the Heavens", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));

            cards.add(new CardInfo("Demeter", "Goddess of the Harvest", "Your Build: Your Worker may build one additional time, but not on the same space."));
            //cards.add(new CardInfo("Demeter", "Goddess of the Harvest", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));

            cards.add(new CardInfo("Hephaestus", "God of Blacksmiths", "Your Build: Your Worker may build one additional block (not dome) on top of your first block."));
            //cards.add(new CardInfo("Hephaestus", "God of Blacksmiths", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));

            cards.add(new CardInfo("Minotaur", "Bull-headed Monster", "Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level."));
            //cards.add(new CardInfo("Minotaur", "Bull-headed Monster", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));

            cards.add(new CardInfo("Pan", "God of the Wild", "Win Condition: You also win if your Worker moves down two or more levels."));
            //cards.add(new CardInfo("Pan", "God of the Wild", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));

            cards.add(new CardInfo("Prometheus", "Titan Benefactor of Mankind", "Your Turn: If your Worker does not move up, it may build both before and after moving."));
            //cards.add(new CardInfo("Prometheus", "Titan Benefactor of Mankind", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));

            cards.add(new CardInfo("generalGod", "", "DON'T CHOOSE ME."));


            CardsInformationEvent cardsInformationEvent = new CardsInformationEvent( cards, challenger, player);

            view.update(cardsInformationEvent);
            if(setDefaultChallenger)
                myWait();
        }

        //Sending the card information event for the firstPlayer
        {
            List<CardInfo> cards = new ArrayList<CardInfo>();
            String challenger;
            challenger = "player1";
            String player = "player2";
            if (isFirstCardChooser)
                player = ViewNickname.getMyNickname();

            cards.add(new CardInfo("Apollo", "God of Music", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));
            cards.add(new CardInfo("Artemis", "Goddess of the Hunt", "Your Move: Your Worker may move one additional time, but not back to its initial space."));
            cards.add(new CardInfo("Athena", "Goddess of Wisdom", "Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn."));

            System.out.println(cards.get(cards.size()-1).getDescription());

            CardsInformationEvent cardsInformationEvent = new CardsInformationEvent( cards, challenger, player);

            view.update(cardsInformationEvent);

            if(isFirstCardChooser)
                myWait();
        }

        //Sending the card information event for the secondPlayer
        {
            List<CardInfo> cards = new ArrayList<CardInfo>();
            String challenger;
            challenger = "player1";
            String player = "player2";
            if (isSecondCardChooser)
                player = ViewNickname.getMyNickname();

            cards.add(new CardInfo("Apollo", "God of Music", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));
            cards.add(new CardInfo("Artemis", "Goddess of the Hunt", "Your Move: Your Worker may move one additional time, but not back to its initial space."));

            System.out.println(cards.get(cards.size()-1).getDescription());

            CardsInformationEvent cardsInformationEvent = new CardsInformationEvent( cards, challenger, player);

            view.update(cardsInformationEvent);

            if(isSecondCardChooser)
                myWait();
        }

        if(isFirstPlacer) {
            view.update(new RequirePlaceWorkersEvent(ViewNickname.getMyNickname()));
            myWait();
        }else{
            view.update(new RequirePlaceWorkersEvent("player1"));
            waiting();
            view.update(new WorkerPlacedEvent("[Worker]\t1", 0, 0, true));
        }
        if(isSecondPlacer) {
            view.update(new RequirePlaceWorkersEvent(ViewNickname.getMyNickname()));
            myWait();
        }else{
            view.update(new RequirePlaceWorkersEvent("player1"));
            waiting();
            view.update(new WorkerPlacedEvent("[Worker]\t2", 0, 1, true));
        }
        if(isThirdPlacer) {
            view.update(new RequirePlaceWorkersEvent(ViewNickname.getMyNickname()));
            myWait();
        }else{
            view.update(new RequirePlaceWorkersEvent("player1"));
            waiting();
            view.update(new WorkerPlacedEvent("[Worker]\t3", 0, 2, true));
        }

        if(isFirstPlacer) {
            view.update(new RequirePlaceWorkersEvent(ViewNickname.getMyNickname()));
            myWait();
        }else{
            view.update(new RequirePlaceWorkersEvent("player2"));
            waiting();
            view.update(new WorkerPlacedEvent("[Worker]\t4", 1, 0, true));
        }
        if(isSecondPlacer) {
            view.update(new RequirePlaceWorkersEvent(ViewNickname.getMyNickname()));
            myWait();
        }else{
            view.update(new RequirePlaceWorkersEvent("player2"));
            waiting();
            view.update(new WorkerPlacedEvent("[Worker]\t5", 1, 1, true));
        }
        if(isThirdPlacer) {
            view.update(new RequirePlaceWorkersEvent(ViewNickname.getMyNickname()));
            myWait();
        }else{
            view.update(new RequirePlaceWorkersEvent("player2"));
            waiting();
            view.update(new WorkerPlacedEvent("[Worker]\t6", 1, 2, true));
        }

        //todo sistemare questa parte
        //Finita la fase di posizionamento invio la ServerSendDataEvent
        {
            int boardXSize = 5;
            int boardYSize = 5;
            List<String> players = new ArrayList<String>();
            Map<String,List<String>> workersToPlayer = new HashMap<String, List<String>>();

            players.add("player1");
            players.add("player2");
            players.add(ViewNickname.getMyNickname());

            List<String> w1 = new ArrayList<String>();
            List<String> w2 = new ArrayList<String>();
            List<String> w3 = new ArrayList<String>();

            w1.add(("1"));
            w1.add(("2"));
            w2.add(("3"));
            w2.add(("4"));
            w3.add(("5"));
            w3.add(("6"));

            view.update((ServerSendDataEvent) new ServerSendDataEvent(boardXSize, boardYSize, players, workersToPlayer));
        }

        //Go to playing status
        view.update((NextStatusEvent) new NextStatusEvent("Go to playing"));

        waiting();

        simulateTurn("player1", "[Worker]\t2", 3, 3, 1, 1, PlaceableType.BLOCK);

        view.update(new TurnStatusChangedEvent(ViewNickname.getMyNickname(), StateType.MOVEMENT, true));
        myWait();
        view.update(new TurnStatusChangedEvent(ViewNickname.getMyNickname(), StateType.MOVEMENT, true));
        myWait();



        *//*view.update((NextStatusEvent)new NextStatusEvent("Playing"));

        synchronized (waitingObj) {
            try {
                waitingObj.wait(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {

            ViewBoard.getBoard().getCellAt(0, 0).buildLevel();
            ViewBoard.getBoard().getCellAt(0, 0).buildLevel();
            ViewBoard.getBoard().getCellAt(0, 0).buildLevel();

            ViewBoard.getBoard().getCellAt(0, 1).buildLevel();
            ViewBoard.getBoard().getCellAt(0, 1).buildLevel();
            ViewBoard.getBoard().getCellAt(0, 1).buildLevel();
            ViewBoard.getBoard().getCellAt(0, 1).buildDome();

            ViewBoard.getBoard().getCellAt(0, 2).buildLevel();
            ViewBoard.getBoard().getCellAt(0, 2).buildLevel();

            ViewBoard.getBoard().getCellAt(0, 3).buildLevel();
            ViewBoard.getBoard().getCellAt(0, 3).buildLevel();
            ViewBoard.getBoard().getCellAt(0, 3).buildDome();

            ViewBoard.getBoard().getCellAt(0, 4).buildDome();
            ViewBoard.getBoard().getCellAt(1, 4).buildLevel();
            ViewBoard.getBoard().getCellAt(2, 4).buildDome();
            ViewBoard.getBoard().getCellAt(2, 2).buildDome();
            ViewBoard.getBoard().getCellAt(3, 4).buildLevel();
            ViewBoard.getBoard().getCellAt(4, 4).buildDome();

            ViewBoard.getBoard().getCellAt(1, 0).buildLevel();

            ViewBoard.getBoard().getCellAt(1, 1).buildLevel();
            ViewBoard.getBoard().getCellAt(1, 1).buildDome();

            ViewBoard.getBoard().getCellAt(2, 0).buildLevel();
            ViewBoard.getBoard().getCellAt(3, 0).buildLevel();
            ViewBoard.getBoard().getCellAt(4, 0).buildLevel();

        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(ViewBoard.getBoard().toTerminal());
        ViewBoard.getBoard().toGUI();
        Viewer.setAllRefresh();
        System.out.println("aggiorno la board da View");*//*

        *//*synchronized (obj) {
            try {
                obj.wait(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ViewBoard.getBoard().setSelectedCell(0, 0);
        Viewer.setAllRefresh();
        System.out.println("aggiorno la board da VIEW");
        //Viewer.setAllRefresh();

        synchronized (obj) {
            try {
                obj.wait(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ViewBoard.getBoard().setSelectedCell(1, 1);
        Viewer.setAllRefresh();

        synchronized (obj) {
            try {
                obj.wait(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ViewBoard.getBoard().setSelectedCell(null);
        Viewer.setAllRefresh();

        synchronized (obj) {
            try {
                obj.wait(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ViewBoard.getBoard().setSelectedCell(0, 0);
        Viewer.setAllRefresh();*//*

    }
*/

}
