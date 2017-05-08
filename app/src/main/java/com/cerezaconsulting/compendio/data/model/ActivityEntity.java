package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;

/**
 * Created by junior on 29/03/17.
 */

public class ActivityEntity implements Serializable {


    private String id = "";
    private double intellect = 0;
    private int correct = 0;
    private int incorrect = 0;
    private String poorly = "";
    private String training = "";
    private String idChapter = "";
    private boolean isOffline = false;



    public ActivityEntity(String id, double intellect, int correct, int incorrect, String poorly, String training, String idChapter, boolean isOffline) {
        this.id = id;
        this.intellect = intellect;
        this.correct = correct;
        this.incorrect = incorrect;
        this.poorly = poorly;
        this.training = training;
        this.idChapter = idChapter;
        this.isOffline = isOffline;
    }

    public ActivityEntity() {

    }

    public ActivityEntity(String id, int correct, int incorrect, double intellect, String poorly, String idChapter, String idTraining) {

        this.id = id;
        this.intellect = intellect;
        this.correct = correct;
        this.incorrect = incorrect;
        this.poorly = poorly;
        this.training = idTraining;
        this.idChapter = idChapter;
        this.isOffline = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public String getPoorly() {
        return poorly;
    }

    public void setPoorly(String poorly) {
        this.poorly = poorly;
    }

    public void incrementIncorrect() {
        incorrect++;
    }

    public void incrementCorrect() {
        correct++;
    }

    public void addIdFragmentToPoorly(String idFragment) {


        if (poorly.equals(""))
            poorly = idFragment;
        else
            poorly = poorly + "," + idFragment;
    }

    public String getIdTraining() {
        return training;
    }

    public void calculateIntellect(int totalQuestion) {
        intellect = (((double) correct) / ((double) totalQuestion)) * 100.0;

    }

    public void setIdTraining(String idTraining) {
        this.training = idTraining;
    }

    public String getIdChapter() {
        return idChapter;
    }

    public void setIdChapter(String idChapter) {
        this.idChapter = idChapter;
    }

    public double getIntellect() {
        return intellect;
    }

    public void setIntellect(double intellect) {
        this.intellect = intellect;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }


}
