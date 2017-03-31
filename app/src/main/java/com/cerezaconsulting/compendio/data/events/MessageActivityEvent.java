package com.cerezaconsulting.compendio.data.events;

/**
 * Created by junior on 29/03/17.
 */

public class MessageActivityEvent {


    public MessageActivityEvent(boolean isCorret, String idFragment) {
        this.isCorret = isCorret;
        this.idFragment = idFragment;
    }

    private boolean isCorret;
    private String idFragment;

    public boolean isCorret() {
        return isCorret;
    }

    public void setCorret(boolean corret) {
        isCorret = corret;
    }

    public String getIdFragment() {
        return idFragment;
    }

    public void setIdFragment(String idFragment) {
        this.idFragment = idFragment;
    }
}
