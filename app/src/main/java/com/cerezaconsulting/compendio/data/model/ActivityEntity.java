package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;

/**
 * Created by junior on 29/03/17.
 */

public class ActivityEntity implements Serializable {

    private double intellect = 0;
    private int correct = 0;
    private int incorrect = 0;
    private String poorly = "";
    private String idTraining;
    private String idChapter;


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
        return idTraining;
    }

    public void calculateIntellect(int totalQuestion) {
        intellect = (((double) correct) / ((double) totalQuestion)) * 100.0;

    }

    public void setIdTraining(String idTraining) {
        this.idTraining = idTraining;
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
