package it.polimi.ingsw.model;

public class Player {
    private String nickname;
    private boolean challenger;
    private boolean startingPlayer;
    private TurnManager turn;
    private MovementManager movementState;
    private ConstructionManager constructionState;

    public Player(String nickname) {
        this.nickname = nickname;
        this.challenger = false;
        this.startingPlayer = false;
        this.movementState = new MovementManager(1);
        this.constructionState = new ConstructionManager(0);
        this.turn = movementState; // initial turn State
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

    public void changeState(TurnManager nextState) {
        this.turn = nextState;
    }
}
