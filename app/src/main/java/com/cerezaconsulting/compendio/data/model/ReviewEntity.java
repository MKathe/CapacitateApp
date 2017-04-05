package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by junior on 03/04/17.
 */

public class ReviewEntity implements Serializable {

    private String id;
    private Date date;
    private int countdown;
    private boolean completed;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


}
