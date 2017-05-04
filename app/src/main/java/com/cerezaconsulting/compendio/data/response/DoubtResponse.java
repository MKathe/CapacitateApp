package com.cerezaconsulting.compendio.data.response;

import java.io.Serializable;

/**
 * Created by junior on 04/05/17.
 */

public class DoubtResponse implements Serializable {
    private String doubt;

    public DoubtResponse(String doubt) {
        this.doubt = doubt;
    }

    public String getDoubt() {
        return doubt;
    }

    public void setDoubt(String doubt) {
        this.doubt = doubt;
    }
}
