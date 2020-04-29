package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: View has requested general game data.
 * [VCEvent]
 */
public class ViewRequestDataEvent extends EventObject {
    private String dataRequested; // what data are requested (if necessary)

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
