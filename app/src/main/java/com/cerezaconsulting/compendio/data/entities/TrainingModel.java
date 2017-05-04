package com.cerezaconsulting.compendio.data.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by junior on 10/04/17.
 */

public class TrainingModel implements Serializable {

    private String id;
    private double position;
    private double progress;
    private double intellect;
    private RealeseModel realese;
    private String certificate;
    private boolean review;
    private boolean finished;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
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

    public RealeseModel getRealese() {
        return realese;
    }

    public void setRealese(RealeseModel realese) {
        this.realese = realese;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public boolean isReview() {
        return review;
    }

    public void setReview(boolean review) {
        this.review = review;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
