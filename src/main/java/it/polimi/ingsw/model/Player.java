package it.polimi.ingsw.model;

public class Player {
    private String nickname;
    private boolean challenger;
    private boolean startingPlayer;
    // TODO: add turn (state pattern) attribute

    public Player(String nickname) {
        this.nickname = nickname;
        this.challenger = false;
        this.startingPlayer = false;
        // TODO: add turn istanciation
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
}
