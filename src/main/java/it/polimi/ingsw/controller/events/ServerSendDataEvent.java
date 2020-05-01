package it.polimi.ingsw.controller.events;

import java.util.*;

/**
 * Event: Server has sent the data requested from Client.
 * [MVEvent]
 */
public class ServerSendDataEvent extends EventObject {
    private int boardXsize, boardYsize;
    private List<String> players;
    private Map<String, List<String>> workersToPlayer; // usage example: List<String> workers = workersToPlayer["player1"];
    // String worker1Player1, String worker2Player1, String worker1Player2, String worker2Player2, String worker1Player3, String worker2Player3

    // TODO: 30/04/20 List<String> cardName // [Andrea: Esattamente qui quali carte ti devo mandare? Perché le carte vanno scelte prima di posizionare i lavoratori...]
    // TODO: 30/04/20 List<String> cardEpiteth
    // TODO: 30/04/20 List<String> cardDescript

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
