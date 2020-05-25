package it.polimi.ingsw.view.clientSide.viewCore.status;

import it.polimi.ingsw.model.StateType;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.BuildBlockExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.MoveWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.PlaceWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SelectWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ClientAddressable;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.*;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalSubTurnViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;
import it.polimi.ingsw.view.interfaces.Addressable;

// TODO: 24/05/20 refacoring [eliminate superfluous getExecuter]
public enum ViewSubTurn implements ClientAddressable {
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
    },OPPONENT_SELECTWORKER("OPPONENT_SELECTWORKER") {
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
    };

    private static ViewSubTurn actualSubTurn = null;
    private static String player;
    private String subTurn;

    ViewSubTurn(String name){
        subTurn = name;
    }

    public static String getClassId(){
        return "[SubTurn]";
    }

    @Override
    public String getMyClassId() {
        return getClassId();
    }

    @Override
    public String getId() {
        return subTurn;
    }

    @Override
    public String toString(){
        return getClassId() + "\t" + getId();
    }

    @Override
    public boolean equals(Addressable pl) {
        return isThis(pl.toString());
    }

    @Override
    public boolean isThis(String st) {
        if(!this.toString().equals(st))
            return this.getId().equals(st);
        return true;
    }

    public static ViewSubTurn search(String st) throws NotFoundException {
        for (ViewSubTurn i: ViewSubTurn.values() ) {
            if(i.isThis(st))
                return i;
        }
        throw new NotFoundException();
    }

    public abstract ViewSubTurn getOpponent();

    public static void setSubTurn(ViewSubTurn subTurn) {
        actualSubTurn = subTurn;
    }

    public static void set(String st){
        try {
            setSubTurn(search(st));
        } catch (NotFoundException e) {
            setSubTurn(null);
        }
    }

    public static void set(String st, String player){
        set(st);
        setStaticPlayer(player);
        if(actualSubTurn != null && !actualSubTurn.isMyTurn()){
            actualSubTurn = actualSubTurn.getOpponent();
        }
    }

    public static void setSubTurn(ViewSubTurn subTurn, String player) {
        actualSubTurn = subTurn;
        setStaticPlayer(player);
        if(actualSubTurn != null && (!actualSubTurn.isMyTurn())){
            actualSubTurn = actualSubTurn.getOpponent();
        }
    }


    public static ViewSubTurn getActual() {
        return actualSubTurn;
    }

    public void setPlayer(String player) {
        setStaticPlayer(player);
    }

    public static void setStaticPlayer(String player) {
        ViewSubTurn.player = player;
    }

    public boolean isMyTurn(){
        return ViewNickname.getNickname().isThis(player);
    }

    public abstract SubTurnViewer getSubViewer();

    public StateType toStateType() {
        return StateType.NONE;
    }

    public Executer getExecuter(){
        return null;
    }

    public String getPlayer(){
        return player;
    }
}
