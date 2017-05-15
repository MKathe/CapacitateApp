package com.cerezaconsulting.compendio.data.events;

/**
 * Created by junior on 15/05/17.
 */

public class NotificacionCancelEvent {

    private boolean status;

    public NotificacionCancelEvent(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
