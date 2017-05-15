package com.cerezaconsulting.compendio.data.response;

import java.util.Date;

/**
 * Created by junior on 27/04/17.
 */

public class ReviewResponse {

    private String id ;
    private long date;
    private int countdown;
    private boolean completed;
    private String training;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
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

    public String getTraining() {
        return training;
    }

    public void setTraining(String training) {
        this.training = training;
    }
}
