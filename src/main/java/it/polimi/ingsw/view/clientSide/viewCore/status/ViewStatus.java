package it.polimi.ingsw.view.clientSide.viewCore.status;
// TODO: write some comments to explains the meaning of every status defined

import it.polimi.ingsw.view.clientSide.viewCore.data.DataStorage;
import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.ResumingExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetNicknameExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetPlayerNumberExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ClientAddressable;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.*;
import it.polimi.ingsw.view.interfaces.Addressable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration type for representing
 * different status by which manage
 * the Client behavior.
 *
 * It's a kind of FSM with each element of the enum corresponding to one status.
 *
 * @author giorgio
 */
public enum ViewStatus implements ClientAddressable {

    /**
     * Initial status intended to establish a connection with the server (and state a welcome message)
     */
    READY("READY"){
        public ViewStatus getNext(){
            return ViewStatus.LOGIN;
        }

        @Override
        Map<String, Executer> getExecuters() {
            return null;
        }

        @Override
        public StatusViewer getViewer() {
            return new ReadyViewer(getExecuters());
        }

        @Override
        void onLoad() {  }
    },

    /**
     * After having established a connection, the next status is the log in status. In this status the player will choose its name and it will be sent to the server
     */
    LOGIN("LOGIN") {
        @Override
        public ViewStatus getNext() {
            return ViewStatus.WAITING;
        }

        @Override
        Map<String, Executer> getExecuters() {
            Map<String, Executer> ret = new HashMap<String, Executer>(1);
            ret.put("NickName", new SetNicknameExecuter());
            return ret;
        }

        @Override
        public StatusViewer getViewer() {
            return new LoginViewer(getExecuters());
        }

        @Override
        void onLoad() {
        }
    },


    /**
     * After the login, the player will be placed into a "waiting room" waiting for the other players to connect. To exit this status it waits for the server to notify the starting of the game.
     */
    WAITING("WAITING") {
        @Override
        public ViewStatus getNext() {
            return ViewStatus.NEW_GAME;
        }

        @Override
        Map<String, Executer> getExecuters() { return null; }

        @Override
        public StatusViewer getViewer() {
            return new WaitingViewer(getExecuters());
        }

        @Override
        void onLoad() {
        }
    },

    /**
     * This status is (obviously) the beginning of the game. In this status the player wion't do anything, the only things done are the initialisations of all the objects used in the game.
     */
    NEW_GAME ("NEW_GAME") {
        @Override
        public ViewStatus getNext() {
            return ViewStatus.GAME_PREPARATION;
        }

        @Override
        Map<String, Executer> getExecuters() {
            return null;
        }

        @Override
        public StatusViewer getViewer() {
            return new NewGameViewer(getExecuters());
        }

        @Override
        void onLoad() {
            //TODO: implements all the initialisations
        }
    },

    /**
     * After having initialised the objects for the game, in this status the player initialises the game itself (by selecting the cards, placing the workers, ...)
     */
    GAME_PREPARATION("GAME_PREPARATION") {
        @Override
        public ViewStatus getNext() {
            return ViewStatus.PLAYING;
        }

        @Override
        Map<String, Executer> getExecuters() {
            //TODO: set the executers
            return null;
        }

        @Override
        public StatusViewer getViewer() {
            return new GamePreparationViewer(getExecuters());
        }

        @Override
        void onLoad() {
            //todo: implement it
        }

        @Override
        public void afterLoad(){
            Viewer.setAllSubTurnViewer(ViewSubTurn.SELECTCARD);
        }
    },

    /**
     * The core of the game: it is in this status that the game take place: it's here that the turns alternate and it is here where the game is played.
     */
    PLAYING("PLAYING") {
        @Override
        public ViewStatus getNext() {
            return ViewStatus.GAME_OVER;
        }

        @Override
        Map<String, Executer> getExecuters() {
            //TODO: set the executers
            return null;
        }

        @Override
        public StatusViewer getViewer() {
            return new PlayingViewer(getExecuters());
        }

        @Override
        void onLoad() {
            try {
                ViewSubTurn.getActual().setPlayer("");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    },


    /**
     * Ending status.
     */
    GAME_OVER("GAME_OVER") {
        @Override
        public ViewStatus getNext() {
            return ViewStatus.READY;
        }

        @Override
        Map<String, Executer> getExecuters() {
            //TODO: set the executers
            return null;
        }

        @Override
        public StatusViewer getViewer() {
            return new GameOverViewer(getExecuters());
        }

        @Override
        void onLoad() {

            ViewMessage.populateAndSend("Game Over", ViewMessage.MessageType.FROM_SERVER_MESSAGE);

            ViewObject.clearAll();
            //TODO: set the onLoad
        }
    },
    /**
     * status in which the player has to choose with how many other players it's going to play.
     */
    NUMBER_PLAYER("NUMBER_PLAYER") {
        @Override
        public ViewStatus getNext() {
            return WAITING;
        }

        @Override
        Map<String, Executer> getExecuters() {
            Map<String, Executer> ret = new HashMap<String, Executer>(1);
            ret.put("NumberPlayers", new SetPlayerNumberExecuter());
            return ret;
        }

        @Override
        public StatusViewer getViewer() {
            return new NumberPlayerViewer(getExecuters());
        }

        @Override
        void onLoad() {

        }
    },
    /**
     * closing status.
     */
    CLOSING("CLOSING") {
        @Override
        public ViewStatus getNext() {
            return null;
        }

        @Override
        Map<String, Executer> getExecuters() {
            return null;
        }

        @Override
        public StatusViewer getViewer() {
            return new ClosingViewer(getExecuters());
        }

        @Override
        void onLoad() {

        }
    },
    REQUEST_RESUMING( "REQUEST_RESUMING"){
        @Override
        public ViewStatus getNext() {
            return GAME_PREPARATION;
        }

        @Override
        Map<String, Executer> getExecuters() {
            Map<String, Executer> ret = new HashMap<String, Executer>(1);
            ret.put("Resume", new ResumingExecuter());
            return ret;
        }

        @Override
        public StatusViewer getViewer() {
            return new RequestResumingViewer(getExecuters());
        }

        @Override
        void onLoad() {

        }

    },
    RESUMING("RESUMING"){
        @Override
        public ViewStatus getNext() {
            return PLAYING;
        }

        @Override
        Map<String, Executer> getExecuters() {
            return null;
        }

        @Override
        public StatusViewer getViewer() {
            return new ResumingViewer(getExecuters());
        }

        @Override
        void onLoad() {
            ViewSubTurn.setSubTurn(ViewSubTurn.PLACEWORKER, "");
        }
    };

    private String id;
    private static ViewStatus actualStatus = READY;

    /**
     * Constructor
     *
     * @param id (The id string of this status).
     */
    ViewStatus(String id){
        this.id = id;
    }

    /**
     * Method that returns the id of this status.
     *
     * @return (String : id of this status)
     */
    public String getId(){
        return id;
    }

    /**
     * Method that returns the next status
     *
     * @return (next status)
     */
    public abstract ViewStatus getNext();

    /**
     * Method that returns the list of all Executer that will be used for this status.
     *
     * @return (Array of all Executers used for this status).
     */
    abstract Map<String, Executer> getExecuters();


    /**
     * Method that returns the viewer for the status
     *
     * @return (StatusViewer of this status)
     */
    public abstract StatusViewer getViewer();

    /**
     * Method to be called as soon as the status is changed. It contains all the instruction to be executed on loading.
     */
    abstract void onLoad();

    /**
     * Method that return the base string for identifying the Class.
     *
     * @return (String identifying the status)
     */
    public static String getClassId(){ return "[AppStatus]"; }

    /**
     * Method that returns the string representation of the status.
     *
     * @return (String representation of the status)
     */
    public String toString(){ return getClassId() + "\t" + this.getId(); }

    /**
     * Method to check weather the passed id is of this class or not.
     *
     * @param id (String to check)
     * @return (True iif the String will correspond to the id of an object of this class).
     */
    public static boolean isOfThisClass( @NotNull String id){ return id.startsWith(getClassId()); }

    /**
     * Method intended to search the status designed by the parameter searched
     *
     * @param searched (id of the status searched)
     * @return (The status searched)
     */
    public static ViewStatus searchByString(String searched){
        for (ViewStatus i : ViewStatus.values())
            if (i.isThis(searched))
                return i;
        return null;
    }

    /**
     * Method intended to set the actual status into the status designed by the parameter searched
     *
     * @param status (id of the status to which go to)
     */
    public static void setStatus(String status){
        ViewStatus tmp = searchByString(status);
        actualStatus = (tmp!=null)? tmp: actualStatus;
        if(tmp!=null) {
            actualStatus.onLoad();
            Viewer.setAllStatusViewer(ViewStatus.getActual().getViewer());
        }
    }

    /**
     * Method intended to switch the status into the next status.
     */
    public static void nextStatus(){
        actualStatus = actualStatus.getNext();
        actualStatus.onLoad();
        Viewer.setAllStatusViewer(ViewStatus.getActual().getViewer());
        actualStatus.afterLoad();
    }

    /**
     * Compares this with pl. return true iif represent the same Object.
     *
     * @param pl (the Addressable to be checked)
     * @return (true iif this == pl)
     */
    public boolean equals(Addressable pl){
        return this.isThis(pl.toString());
    }

    /**
     * Method checking weather the given string is identifying this.
     *
     * @param st (String that will possibly represent this)
     * @return (true iif st==this.toString())
     */
    public boolean isThis (String st){
        if(isOfThisClass(st)) {
            return st.equals(this.toString());
        }else{
            return st.equals(this.getId());
        }
    }

    /**
     * Method returning a unique String for each class.
     *
     * @return (unique string for each class)
     */
    public String getMyClassId(){
        return getClassId();
    }



    public static void reset(){
        actualStatus = READY;
        Viewer.setAllStatusViewer(ViewStatus.getActual().getViewer());
    }

    public static void init(){
        reset();
        actualStatus.onLoad();
    }

    public static ViewStatus getActual(){
        return actualStatus;
    }

    void afterLoad(){
        ;
    }
}