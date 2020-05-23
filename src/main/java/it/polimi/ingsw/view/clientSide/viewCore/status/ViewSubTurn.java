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
import it.polimi.ingsw.view.interfaces.Addressable;

public enum ViewSubTurn implements ClientAddressable {
    SELECTWORKER("SELECTWORKER"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new SelectWorkerViewer(this);
        }

        @Override
        public Executer getExecuter(){
            return new SelectWorkerExecuter();
        }
    },
    BUILD("BUILD"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new BuildViewer(this);
        }

        @Override
        public StateType toStateType(){
            return StateType.CONSTRUCTION;
        }

        @Override
        public Executer getExecuter(){
            return new BuildBlockExecuter();
        }

    },
    MOVE("MOVE"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new MoveViewer(this);
        }

        @Override
        public StateType toStateType(){
            return StateType.CONSTRUCTION;
        }

        @Override
        public Executer getExecuter(){
            return new MoveWorkerExecuter();
        }
    }, SELECTCARD ("SELECTCARD"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new SelectCardViewer(this);
        }
    }, PLACEWORKER("PLACEWORKER"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new PlaceWorkerViewer(this);
        }

        @Override
        public Executer getExecuter(){
            return new PlaceWorkerExecuter();
        }
    };

    private static ViewSubTurn actualSubTurn = null;
    private static String player;
    private String subTurn;

    ViewSubTurn(String name){
        subTurn = name;
    }

    public static String getClassId(){ return "[SubTurn]"; }

    @Override
    public String getMyClassId() { return getClassId(); }

    @Override
    public String getId() { return subTurn; }

    @Override
    public String toString(){ return getClassId() + "\t" + getId(); }

    @Override
    public boolean equals(Addressable pl) { return isThis(pl.toString()); }

    @Override
    public boolean isThis(String st) { return this.toString().equals(st); }

    public static ViewSubTurn search(String st) throws NotFoundException {
        for (ViewSubTurn i: ViewSubTurn.values() ) {
            if(i.isThis(st))
                return i;
        }
        throw new NotFoundException();
    }

    public static void setSubTurn(ViewSubTurn subTurn){ actualSubTurn = subTurn; }
    //todo override
    public static void set(String st){
        try {
            setSubTurn(search(st));
        } catch (NotFoundException e) {
            setSubTurn(null);
        }
    }

    public static ViewSubTurn getActual(){ return actualSubTurn; }

    public void setPlayer(String player){ this.player = player; }

    public boolean isMyTurn(){return ViewNickname.getNickname().isThis(player); }

    public abstract SubTurnViewer getSubViewer();

    public StateType toStateType(){return StateType.NONE; }

    public Executer getExecuter(){ return null; }
}
