package com.cerezaconsulting.coreproject.data.model;

import java.io.Serializable;

/**
 * Created by junior on 24/03/17.
 */

public class TrainingEntity implements Serializable {

    private int id;
    private int position;
    private int progress;
    private int intellect;
    private String certificate;
    private boolean finished = false;
    private RealeseEntity release;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getIntellect() {
        return intellect;
    }

    public void setIntellect(int intellect) {
        this.intellect = intellect;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public RealeseEntity getRelease() {
        return release;
    }

    public void setRelease(RealeseEntity release) {
        this.release = release;
    }
}
