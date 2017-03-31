package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by junior on 24/03/17.
 */

public class TrainingEntity implements Serializable {

    private int id;
    private double position;
    private double progress;
    private double intellect;
    private String certificate;
    private boolean finished = false;
    private RealeseEntity release;
    private ArrayList<ActivityEntity> activityEntities;

    public ArrayList<ActivityEntity> getActivityEntities() {
        return activityEntities;
    }

    public void setActivityEntities(ArrayList<ActivityEntity> activityEntities) {
        this.activityEntities = activityEntities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public double getIntellect() {
        return intellect;
    }

    public void setIntellect(double intellect) {
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
