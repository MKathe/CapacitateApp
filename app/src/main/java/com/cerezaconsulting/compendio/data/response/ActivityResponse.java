package com.cerezaconsulting.compendio.data.response;

import java.io.Serializable;

/**
 * Created by junior on 27/04/17.
 */

public class ActivityResponse implements Serializable {


    private String id;
    private int correct = 0;
    private int incorrect = 0;
    private String poorly;
    private String training;
    private String chapter;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPoorly() {
        return poorly;
    }

    public void setPoorly(String poorly) {
        this.poorly = poorly;
    }

    public String getTraining() {
        return training;
    }

    public void setTraining(String training) {
        this.training = training;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
