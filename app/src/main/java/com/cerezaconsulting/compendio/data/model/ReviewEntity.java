package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by junior on 03/04/17.
 */

public class ReviewEntity implements Serializable {



    private String id = "";
    private String idLocal = "";
    private Date date;
    private int countdown;
    private boolean completed;
    private boolean isOffline = false;
    private String idTraining;


    public ReviewEntity() {

    }

    public ReviewEntity(String id, String idLocal, Date date, int countdown, boolean completed, boolean isOffline, String idTraining) {
        this.id = id;
        this.idLocal = idLocal;
        this.date = date;
        this.countdown = countdown;
        this.completed = completed;
        this.isOffline = isOffline;
        this.idTraining = idTraining;
    }

    public String getIdTraining() {
        return idTraining;
    }


    public String getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public void setIdTraining(String idTraining) {
        this.idTraining = idTraining;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

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
