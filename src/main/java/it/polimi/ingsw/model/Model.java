package it.polimi.ingsw.model;

import java.util.List;
import java.util.Map;

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
    private volatile Boolean gameStarted; // tel if the game has started

    public synchronized Boolean isGameStarted() {
        return gameStarted;
    }

    public synchronized void setGameStarted(Boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public static void setChallenger(Object o){}
    public static void setCardsInGame (Object o){}
    public static void setStartPlayer(Object o){}
    public static void setPlayerCard(Object a, Object b){}
    public static int getBoardXSize(){return 0;}
    public static int getBoardYSize(){ return 0; }
    public static List<String> getPlayers(){return null;}
    public static Map<String, List<String>> getWorkers(){return null;}
    public static boolean placeWorker (int x, int y, Object o){return true;}
    public static int WORKERS_PER_PLAYER = 0;
}
