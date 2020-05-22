package it.polimi.ingsw.view.clientSide;

import it.polimi.ingsw.controller.events.CardsInformationEvent;
import it.polimi.ingsw.controller.events.NextStatusEvent;
import it.polimi.ingsw.controller.events.RequirePlayersNumberEvent;
import it.polimi.ingsw.controller.events.ServerSendDataEvent;
import it.polimi.ingsw.model.CardInfo;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ViewSender;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.GUIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.TerminalViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

import java.util.*;

public class ViewTester implements ViewSender {

    private final static boolean addWait = false;
    private final static boolean setDefaultChallenger = true;

    private Object lock = new Object();
    private View view = new View(null, null);

    private void myWait(){
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialization(){
        //new TerminalViewer().start();
        new GUIViewer().start();
        //new CLIViewer().start();


        Executer.setSender(this);
    }

    private void end(){
        Viewer.exitAll();
    }

    private void myMain() {

        Object obj = new Object();

        initialization();
        System.out.println("Hello World");

        ViewStatus.init();

        if(addWait){
            synchronized (obj) {
                try {
                    obj.wait(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        view.update((NextStatusEvent)new NextStatusEvent("vai alla login"));
        myWait();
        view.update((RequirePlayersNumberEvent) new RequirePlayersNumberEvent());
        myWait();
        view.update((NextStatusEvent)new NextStatusEvent("vai alla wait"));


        if(addWait) {

            /*ViewMessage.populateAndSend("test fromServerMEssage", ViewMessage.MessageType.FROM_SERVER_MESSAGE);
            ViewMessage.populateAndSend("test serverError", ViewMessage.MessageType.FROM_SERVER_ERROR);
            ViewMessage.populateAndSend("test executerError", ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
            ViewMessage.populateAndSend("test fatalError", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
*/
            synchronized (obj) {
                try {
                    obj.wait(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ViewMessage.populateAndSend("test async", ViewMessage.MessageType.FROM_SERVER_MESSAGE);
            synchronized (obj) {
                try {
                    obj.wait(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        view.update((NextStatusEvent)new NextStatusEvent("newGame"));

        {
            int boardXSize = 5;
            int boardYSize = 5;
            List<String> players = new ArrayList<String>();
            Map<String,List<String>> workersToPlayer = new HashMap<String, List<String>>();

            players.add("player1");
            players.add("player2");
            players.add("player3");

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

        view.update((NextStatusEvent)new NextStatusEvent("go to gamePreparation"));

        //Sending the card information event
        {
            List<CardInfo> cards = new ArrayList<CardInfo>();
            String challenger = "player1";
            String player = "";

            cards.add(new CardInfo("Apollo", "God of Music", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));
            cards.add(new CardInfo("Artemis", "Goddess of the Hunt", "Your Move: Your Worker may move one additional time, but not back to its initial space."));
            cards.add(new CardInfo("Athena", "Goddess of Wisdom", "Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn."));
            cards.add(new CardInfo("Atlas", "Titan Shouldering the Heavens", "Your Build: Your Worker may build a dome at any level."));
            cards.add(new CardInfo("Demetra", "Goddess of the Harvest", "Your Build: Your Worker may build one additional time, but not on the same space."));
            cards.add(new CardInfo("Hephaestus", "God of Blacksmiths", "Your Build: Your Worker may build one additional block (not dome) on top of your first block."));
            cards.add(new CardInfo("Minotaur", "Bull-headed Monster", "Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level."));
            cards.add(new CardInfo("Pan", "God of the Wild", "Win Condition: You also win if your Worker moves down two or more levels."));
            cards.add(new CardInfo("Prometheus", "Titan Benefactor of Mankind", "Your Turn: If your Worker does not move up, it may build both before and after moving."));
            cards.add(new CardInfo("generalGod", "God of this dick", "DON'T CHOOSE ME."));


            CardsInformationEvent cardsInformationEvent = new CardsInformationEvent( cards, challenger, player);

            view.update(cardsInformationEvent);
        }

        {
            List<CardInfo> cards = new ArrayList<CardInfo>();
            String challenger;
            if(setDefaultChallenger)
                challenger = "nickname";
            else
                challenger = "player1";
            String player = "";

            cards.add(new CardInfo("Apollo", "God of Music", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."));
            cards.add(new CardInfo("Artemis", "Goddess of the Hunt", "Your Move: Your Worker may move one additional time, but not back to its initial space."));
            cards.add(new CardInfo("Athena", "Goddess of Wisdom", "Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn."));
            cards.add(new CardInfo("Atlas", "Titan Shouldering the Heavens", "Your Build: Your Worker may build a dome at any level."));
            cards.add(new CardInfo("Demetra", "Goddess of the Harvest", "Your Build: Your Worker may build one additional time, but not on the same space."));
            cards.add(new CardInfo("Hephaestus", "God of Blacksmiths", "Your Build: Your Worker may build one additional block (not dome) on top of your first block."));
            cards.add(new CardInfo("Minotaur", "Bull-headed Monster", "Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level."));
            cards.add(new CardInfo("Pan", "God of the Wild", "Win Condition: You also win if your Worker moves down two or more levels."));
            cards.add(new CardInfo("Prometheus", "Titan Benefactor of Mankind", "Your Turn: If your Worker does not move up, it may build both before and after moving."));
            cards.add(new CardInfo("Default", "God of this dick", "DON'T CHOOSE ME."));
            cards.add(new CardInfo("Default", "God of this dick", "Your Condition: do not choose me."));

            System.out.println(cards.get(cards.size()-1).getDescription());

            CardsInformationEvent cardsInformationEvent = new CardsInformationEvent( cards, challenger, player);

            view.update(cardsInformationEvent);

            if(setDefaultChallenger)
                myWait();
        }


        view.update((NextStatusEvent)new NextStatusEvent("Playing"));

        synchronized (obj) {
            try {
                obj.wait(2500);
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
        System.out.println("aggiorno la board da View");

        /*synchronized (obj) {
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
        Viewer.setAllRefresh();*/

    }

    public static void main(String[] args) {
        new ViewTester().myMain();
    }

    @Override
    public void send(EventObject event) {
        synchronized (lock){
            lock.notifyAll();
        }
    }

}
