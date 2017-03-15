package com.cerezaconsulting.coreproject.data.response;

import java.io.Serializable;

/**
 * Created by junior on 30/09/16.
 */
public class LoginResponse implements Serializable {


    private String email_or_phone;
    private String password;


    public LoginResponse(String email_or_phone, String password) {
        this.email_or_phone = email_or_phone;
        this.password = password;
    }

    public String getEmail_or_phone() {
        return email_or_phone;
    }

    public void setEmail_or_phone(String email_or_phone) {
        this.email_or_phone = email_or_phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
