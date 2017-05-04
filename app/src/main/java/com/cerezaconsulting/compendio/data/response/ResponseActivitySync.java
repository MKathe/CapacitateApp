package com.cerezaconsulting.compendio.data.response;

import java.io.Serializable;

/**
 * Created by junior on 01/05/17.
 */

public class ResponseActivitySync  implements Serializable{

    private boolean success;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
