package com.cerezaconsulting.compendio.data.response;

import java.io.Serializable;

/**
 * Created by junior on 08/05/17.
 */

public class ChangePasswordResponse implements Serializable {

    private String email_or_phone;


    public ChangePasswordResponse(String email_or_phone) {
        this.email_or_phone = email_or_phone;
    }

    public String getEmail_or_phone() {
        return email_or_phone;
    }

    public void setEmail_or_phone(String email_or_phone) {
        this.email_or_phone = email_or_phone;
    }
}
