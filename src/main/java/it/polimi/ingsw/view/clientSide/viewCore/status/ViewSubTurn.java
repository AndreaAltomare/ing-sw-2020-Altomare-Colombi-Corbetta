package it.polimi.ingsw.view.clientSide.viewCore.status;

import it.polimi.ingsw.model.player.turn.StateType;
import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ClientAddressable;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.FirstPlayerViewer;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.*;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;
import it.polimi.ingsw.view.interfaces.Addressable;

/**
 * Enumeration describing the status of the Game.
 * It indicates the specific actions to perform in specific information needed to display
 * and the action that can be performed in each moment.
 *
 * It specifies, for example, the turn status on which the game is.
 *
 * @author giorgio
 */
public enum ViewSubTurn implements ClientAddressable {


    /**
     * SubTurn relative to the Playing status.
     * In this SubTurn the player have to choose a worker.
     */
    SELECTWORKER("SELECTWORKER"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new SelectWorkerViewer(this);
        }

        @Override
        public Executer getExecuter() {
            return new SelectWorkerExecuter();
        }

        @Override
        public ViewSubTurn getOpponent() {
            return OPPONENT_SELECTWORKER;
        }
    },

    /**
     * SubTurn relative to the Playing status.
     * In this SubTurn the adversary have to choose a worker.
     * The player can do nothing.
     */
    OPPONENT_SELECTWORKER("OPPONENT_SELECTWORKER") {
        @Override
        public SubTurnViewer getSubViewer()  {
            return new OpponentSelectWorkerViewer(this);
        }

        @Override
        public Executer getExecuter() {
            return null;
        }

        @Override
        public ViewSubTurn getOpponent() {
            return SELECTWORKER;
        }
    },

    /**
     * SubTurn relative to the Playing status.
     * In this SubTurn the player can build.
     */
    BUILD("CONSTRUCTION"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new BuildViewer(this);
        }

        @Override
        public StateType toStateType() {
            return StateType.CONSTRUCTION;
        }

        @Override
        public Executer getExecuter() {
            return new BuildBlockExecuter();
        }

        @Override
        public ViewSubTurn getOpponent() {
            return OPPONENT_BUILD;
        }

    },

    /**
     * SubTurn relative to the Playing status.
     * In this SubTurn the adversary can build.
     * The player can do nothing.
     */
    OPPONENT_BUILD("OPPONENT_CONSTRUCTION"){
        @Override
        public SubTurnViewer getSubViewer()  {
            return new OpponentBuildViewer(this);
        }

        @Override
        public Executer getExecuter() {
            return null;
        }

        @Override
        public ViewSubTurn getOpponent() {
            return BUILD;
        }

    },

    /**
     * SubTurn relative to the Playing status.
     * In this SubTurn the player can move.
     */
    MOVE("MOVEMENT"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new MoveViewer(this);
        }

        @Override
        public StateType toStateType() {
            return StateType.MOVEMENT;
        }

        @Override
        public Executer getExecuter() {
            return new MoveWorkerExecuter();
        }

        @Override
        public ViewSubTurn getOpponent() {
            return OPPONENT_MOVE;
        }
    },

    /**
     * SubTurn relative to the Playing status.
     * In this SubTurn the adversary can move.
     * The player can do nothing.
     */
    OPPONENT_MOVE("OPPONENT_MOVEMENT"){
        @Override
        public SubTurnViewer getSubViewer()  {
            return new OpponentMoveViewer(this);
        }

        @Override
        public Executer getExecuter() {
            return null;
        }

        @Override
        public ViewSubTurn getOpponent() {
            return MOVE;
        }
    },

    /**
     * SubTurn relative to the GamePreparation status.
     * In this SubTurn the player can move.
     */
    SELECTCARD ("SELECTCARD"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new SelectCardViewer(this);
        }

        @Override
        public ViewSubTurn getOpponent() {
            return SELECTCARD;
        }
    },

    /**
     * SubTurn relative to the GamePreparation status.
     * In this SubTurn the player can place a worker.
     */
    PLACEWORKER("PLACEWORKER"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new PlaceWorkerViewer(this);
        }

        @Override
        public Executer getExecuter(){
            return new PlaceWorkerExecuter();
        }

        @Override
        public ViewSubTurn getOpponent()  {
            return OPPONENT_PLACEWORKER;
        }
    },

    /**
     * SubTurn relative to the GamePreparation status.
     * In this SubTurn the adversary can place a worker.
     * The player can do nothing.
     */
    OPPONENT_PLACEWORKER("OPPONENT_PLACEWORKER") {
        @Override
        public SubTurnViewer getSubViewer()  {
            return new OpponentPlaceWorkerViewer(this);
        }

        @Override
        public Executer getExecuter() {
            return null;
        }

        @Override
        public ViewSubTurn getOpponent() {
            return PLACEWORKER;
        }
    },

    /**
     * SubTurn relative to the Playing status.
     * In this SubTurn the player can build a block.
     *
     * This has not a correspective on the Server, it's seen as the Build is.
     */
    BUILD_BLOCK("CONSTRUCTION") {
        //Not notified by the server
        @Override
        public SubTurnViewer getSubViewer() {
            return new BuildBlockViewer(this);
        }

        @Override
        public StateType toStateType() {
            return StateType.CONSTRUCTION;
        }

        @Override
        public Executer getExecuter()  {
            BuildBlockExecuter ret = new BuildBlockExecuter();
            try {
                ret.setPlaceable("BLOCK");
            } catch (WrongParametersException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public ViewSubTurn getOpponent() {
            return OPPONENT_BUILD;
        }
    },

    /**
     * SubTurn relative to the Playing status.
     * In this SubTurn the player can build a dome.
     *
     * This has not a correspective on the Server, it's seen as the Build is.
     */
    BUILD_DOME("CONSTRUCTION") {
        //Not notified by the server
        @Override
        public SubTurnViewer getSubViewer() {
            return new BuildDomeViewer(this);
        }

        @Override
        public StateType toStateType() {
            return StateType.CONSTRUCTION;
        }

        @Override
        public Executer getExecuter() {
            BuildBlockExecuter ret = new BuildBlockExecuter();
            try {
                ret.setPlaceable("DOME");
            } catch (WrongParametersException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public ViewSubTurn getOpponent() {
            return OPPONENT_BUILD;
        }
    },

    /**
     * SubTurn relative to the GamePreparation status.
     * In this SubTurn the challenger have to choose the starting player.
     */
    CHOOSE_FIRST_PLAYER( "CHOOSE_FIRST_PLAYER"){
        protected FirstPlayerExecuter executer= new FirstPlayerExecuter();

        @Override
        public ViewSubTurn getOpponent() {
            return CHOOSE_FIRST_PLAYER;
        }

        @Override
        public SubTurnViewer getSubViewer() {
            return new FirstPlayerViewer(this);
        }

        @Override
        public Executer getExecuter() {
            return executer;
        }

    };

    private static ViewSubTurn actualSubTurn = null;
    private static String player;
    private String subTurn;

    private static StateType macroStatus;

    /**
     * Method that sets the status of the Server.
     *
     * @param state (Server's actual <code>StatusType</code>)
     */
    public static void setMacroStatus(StateType state){
        macroStatus = state;
    }

    /**
     * Method called after having selected a worker and returns to the right SubTurn (referring to the Server's method.
     */
    public static void afterSelection(){
        if(macroStatus == StateType.MOVEMENT)
            setSubTurn(MOVE, player);
        else if(macroStatus == StateType.CONSTRUCTION)
            setSubTurn(BUILD, player);
        else{
            //TODO: rimuovere controllo
            if(View.debugging)
                System.out.println("Unknown status");
            setSubTurn(MOVE);
        }

    }

    /**
     * Constructor.
     *
     * @param name (the String referring to the Server <code>StateType</code>).
     */
    ViewSubTurn(String name){
        subTurn = name;
    }


    /**
     * Returns a string identifying this class: "[SubTurn]".
     *
     * @return (a string identifying this class: "[SubTurn]").
     */
    public static String getClassId(){
        return "[SubTurn]";
    }

    /**
     * Method returning a unique String for each class, in this case: "[SubTurn]".
     *
     * @return (a string identifying this class: "[SubTurn]").
     */
    @Override
    public String getMyClassId() {
        return getClassId();
    }


    /**
     * Method returning a unique string for each object inside the Class.
     *
     * @return (the String representation of this subTurn).
     */
    @Override
    public String getId() {
        return subTurn;
    }


    /**
     * Method returning a string identifying this Object and the Class.
     *
     * @return
     */
    @Override
    public String toString(){
        return getClassId() + "\t" + getId();
    }

    /**
     * Method checking if the given Addressable is the same of this.
     *
     * @param pl (the Addressable to be checked)
     * @return
     */
    @Override
    public boolean equals(Addressable pl) {
        return isThis(pl.toString());
    }

    /**
     * Method checking if the given String represent this.
     *
     * @param st (String that will possibly represent this)
     * @return (true iif st represent this)
     */
    @Override
    public boolean isThis(String st) {
        if(!this.toString().equals(st))
            return this.getId().equals(st);
        return true;
    }

    /**
     * Method searching the ViewSubTurn represented by the given String.
     *
     * @param st (The String representation of the searched ViewSubTurn).
     * @return  (the searched ViewSubTurn)
     * @throws NotFoundException    (if there isn't a SubTurn with such a String representation).
     */
    public static ViewSubTurn search(String st) throws NotFoundException {
        for (ViewSubTurn i: ViewSubTurn.values() ) {
            if(i.isThis(st))
                return i;
        }
        throw new NotFoundException();
    }

    /**
     * Method returning the same status if there is the opponent doing it.
     *
     * @return (the relative opponent's subTurn).
     */
    public abstract ViewSubTurn getOpponent();


    /**
     * Method to set the actual SubTurn.
     *
     * @param subTurn (the SubTurn to be set, null if is wanted to be left unnsetted)
     */
    public static void setSubTurn(ViewSubTurn subTurn) {
        actualSubTurn = subTurn;
    }

    /**
     * Method to set the actual SubTurn.
     * It sets it to be the SubTurn represented by
     * the given String, or null if there is no
     * such represented SubTurn
     *
     * @param st (string representing the SubTurn to be set)
     */
    public static void set(String st){
        try {
            setSubTurn(search(st));
        } catch (NotFoundException e) {
            if(st=="NONE"){
                setStaticPlayer("");
                // before it was a SELECTWORKER
                set("OPPONENT_SELECTWORKER");
            }else {
                setSubTurn(null);
            }
        }
    }

    /**
     * Method to set the actual SubTurn and player.
     *
     * It sets it to be the SubTurn represented by
     * the given String, or null if there is no
     * such represented SubTurn.
     *
     * It also set the current player to be the one
     * given.
     *
     * This method calculates if it's the player's turn
     * Or if it's an opponent one and set it properly.
     *
     * @param st (the String representation of the SubTurn).
     * @param player  (the name of the actual Player).
     */
    public static void set(String st, String player){
        if(st=="NONE") {
            setStaticPlayer("");
            // before it was a SELECTWORKER
            set("OPPONENT_SELECTWORKER");
            return;
        }
        set(st);
        setStaticPlayer(player);
        if(actualSubTurn != null && !actualSubTurn.isMyTurn()){
            actualSubTurn = actualSubTurn.getOpponent();
        }
    }

    /**
     * Method to set the actual SubTurn and player.
     *
     * It properly sets it to be the SubTurn given
     * or the opponent's correspecctive.
     *
     * It also set the current player to be the one
     * given
     *
     * @param subTurn (the ViewSubTurn to be set).
     * @param player  (the name of the actual Player).
     */
    public static void setSubTurn(ViewSubTurn subTurn, String player) {
        actualSubTurn = subTurn;
        setStaticPlayer(player);
        if(actualSubTurn != null && (!actualSubTurn.isMyTurn())){
            actualSubTurn = actualSubTurn.getOpponent();
        }
    }


    /**
     * Method returning the actual ViewSubTurn.
     *
     * @return (the accual ViewSubTurn).
     */
    public static ViewSubTurn getActual() {
        return actualSubTurn;
    }

    /**
     * Method to set the actual player -remains the same even on
     * change of SubTurn till it's not changed again-.
     *
     * @param player (the name of the current Player)
     */
    public void setPlayer(String player) {
        setStaticPlayer(player);
    }

    /**
     * Method to set the actual player -remains the same even on
     * change of SubTurn till it's not changed again-.
     *
     * @param player (the name of the current Player)
     */
    public static void setStaticPlayer(String player) {
        ViewSubTurn.player = player;
    }

    /**
     * Method to check if it's the player's SubTurn.
     *
     * @return (true iif currentPlayer == player).
     */
    public boolean isMyTurn(){
        return ViewNickname.getNickname().isThis(player);
    }

    /**
     * Method returning the SubTurnViewer related to this SubTurn.
     *
     * @return (the SubTurnViewer related to this SubTurn).
     * @see ViewSubTurn
     */
    public abstract SubTurnViewer getSubViewer();

    /**
     * Method returning the corresponding StateType of this.
     *
     * @return (the corresponding StateType of this).
     */
    public StateType toStateType() {
        return StateType.NONE;
    }

    /**
     * Method returning the Executer related to this SubTurn.
     *
     * @return (the Executer related to this).
     */
    public Executer getExecuter(){
        return null;
    }

    /**
     * Method returning the actually set player.
     *
     * @return (the actually set player).
     */
    public String getPlayer(){
        return player;
    }
}
