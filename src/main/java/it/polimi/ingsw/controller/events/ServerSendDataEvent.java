package it.polimi.ingsw.controller.events;

import java.util.*;

/**
 * Event: Server has sent the data requested from Client.
 * [MVEvent]
 */
public class ServerSendDataEvent extends EventObject {
    private int boardXsize, boardYsize;
    private List<String> players;
    private Map<String, List<String>> workersToPlayer; // usage example: List<String> workers = workersToPlayer["player1"]; (kind of...)

    public ServerSendDataEvent(int boardXSize, int boardYSize, List<String> players, Map<String,List<String>> workersToPlayer) {
        super(new Object());
        this.boardXsize = boardXSize;
        this.boardYsize = boardYSize;
        this.players = new ArrayList<>(players);
        this.workersToPlayer = new HashMap<>(workersToPlayer);
    }

    public int getBoardXsize() {
        return boardXsize;
    }

    public int getBoardYsize() {
        return boardYsize;
    }

    public List<String> getPlayers() {
        return players;
    }

    public Map<String, List<String>> getWorkersToPlayer() {
        return workersToPlayer;
    }
}
