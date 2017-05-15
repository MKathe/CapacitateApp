package com.cerezaconsulting.compendio.data.response;

import java.io.Serializable;

/**
 * Created by junior on 27/04/17.
 */

public class ResponseCompleteSyncResponse implements Serializable {

    public boolean linked;

    public boolean isLinked() {
        return linked;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }
}
