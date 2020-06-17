package it.polimi.ingsw.model.persistence.players;

import it.polimi.ingsw.model.player.turn.StateType;

import java.io.Serializable;
import java.util.List;

public class PlayerData implements Serializable {
    private String nickname;
    private boolean challenger;
    private boolean startingPlayer;
    private CardData card;
    private StateType turn;
    private List<WorkerData> workers;
    private boolean playing;

    /* Default Constructor */
    public PlayerData() {}

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isChallenger() {
        return challenger;
    }

    public void setChallenger(boolean challenger) {
        this.challenger = challenger;
    }

    public boolean isStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(boolean startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public CardData getCard() {
        return card;
    }

    public void setCard(CardData card) {
        this.card = card;
    }

    public StateType getTurn() {
        return turn;
    }

    public void setTurn(StateType turn) {
        this.turn = turn;
    }

    public List<WorkerData> getWorkers() {
        return workers;
    }

    public void setWorkers(List<WorkerData> workers) {
        this.workers = workers;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
