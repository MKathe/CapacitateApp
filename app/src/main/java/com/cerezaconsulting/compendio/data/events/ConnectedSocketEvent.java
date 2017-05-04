package com.cerezaconsulting.compendio.data.events;

import java.io.Serializable;

/**
 * Created by junior on 27/04/17.
 */

public class ConnectedSocketEvent implements Serializable {

    private int status = 0;

    public ConnectedSocketEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
