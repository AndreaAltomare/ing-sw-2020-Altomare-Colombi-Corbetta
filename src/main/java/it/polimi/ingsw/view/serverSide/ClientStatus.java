package it.polimi.ingsw.view.serverSide;

/**
 * Enum to describe the bossible states of the client.
 *
 * @author giorgio
 */
public enum ClientStatus {

    //elenched in opposite order
    //The previous comment is absolutely an eyesore... BTW it is funny thinking someone someday will laugh at my "TeknicalIngleesc"

    /**
     * Ending status.
     */
    GAME_OVER("GAME_OVER", null),
    /**
     * The core of the game: it is in this status that the game take place: it's here that the turns alternate and it is here where the game is played.
     */
    PLAYING("PLAYING", GAME_OVER),
    /**
     * After having initialised the objects for the game, in this status the player initialises the game itself (by selecting the cards, placing the workers, ...)
     */
    GAME_PREPARATION("GAME_PREPARATION", PLAYING),
    /**
     * This status is (obviously) the beginning of the game. In this status the player wion't do anything, the only things done are the initialisations of all the objects used in the game.
     */
    NEW_GAME("NEW_GAME", GAME_PREPARATION),
    /**
     * After the login, the player will be placed into a "waiting room" waiting for the other players to connect. To exit this status it waits for the server to notify the starting of the game.
     */
    WAITING("WAITING", NEW_GAME),
    /**
     * After having established a connection, the next status is the log in status. In this status the player will choose its name and it will be sent to the server
     */
    LOGIN("LOGIN", WAITING),
    /**
     * Initial status intended to establish a connection with the server (and state a welcome message)
     */
    READY("READY", LOGIN);

    String id;
    ClientStatus next;


    /**
     * Value of the beginning status.
     */
    public static final ClientStatus startingFrom = READY;

    /**
     * Constructor
     *
     * @param id (String identificator of this status)
     * @param next (The next status)
     */
    ClientStatus(String id, ClientStatus next){
        this.id = id;
        this.next = next;
    }

    public String toString(){
        return id;
    }

    /**
     * Method that returns the next status
     *
     * @return (Next status)
     */
    ClientStatus getNext(){
        return next;
    }

    /**
     * Method that searches the status from the given string.
     *
     * @param id (the String identifying the status searched)
     * @return (The searched status)
     */
    ClientStatus searchByString(String id){
        for (ClientStatus i : ClientStatus.values())
            if(i.toString().equals(id))
                return i;
        return null;
    }
}
