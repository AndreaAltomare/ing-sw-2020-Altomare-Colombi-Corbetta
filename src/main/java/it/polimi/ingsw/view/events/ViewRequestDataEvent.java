package it.polimi.ingsw.view.events;

import java.util.EventObject;

//this event may be useless from a Distributed-MVC Pattern point of view. It's possible to remove it in further version.
/**
 * Event: View has requested general game data.
 * [VCEvent]
 */
public class ViewRequestDataEvent extends EventObject {
    private String dataRequested; // what data are requested (if necessary)

    /**
     * Constructs a ViewRequestDataEvent to notify the Server that the View needs
     * a new ServerSendDataEvent.
     */
    public ViewRequestDataEvent() {
        super(new Object());
        this.dataRequested = ""; // default request
    }

    public String getDataRequested() {
        return dataRequested;
    }

    public void setDataRequested(String dataRequested) {
        this.dataRequested = dataRequested;
    }
}
