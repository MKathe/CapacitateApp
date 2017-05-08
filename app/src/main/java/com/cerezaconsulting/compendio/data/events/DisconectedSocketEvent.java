package com.cerezaconsulting.compendio.data.events;

import java.io.Serializable;

/**
 * Created by junior on 05/05/17.
 */

public class DisconectedSocketEvent implements Serializable {


    private boolean active = false;

    public DisconectedSocketEvent(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
