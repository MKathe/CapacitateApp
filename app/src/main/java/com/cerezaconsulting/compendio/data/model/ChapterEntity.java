package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by miguel on 16/03/17.
 */

public class ChapterEntity implements Serializable {
    private String id;
    private String name;
    private boolean finished = false;
    private ArrayList<QuestionEntity> questionEntities;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public ArrayList<QuestionEntity> getQuestionEntities() {
        return questionEntities;
    }

    public void setQuestionEntities(ArrayList<QuestionEntity> questionEntities) {
        this.questionEntities = questionEntities;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public ArrayList<FragmentEntity> getFragments() {
        return fragments;
    }

    public void setFragments(ArrayList<FragmentEntity> fragments) {
        this.fragments = fragments;
    }

    private ArrayList<FragmentEntity> fragments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (name != null) {
            return name;
        } else {
            return "";
        }
    }

    public void setName(String name) {
        this.name = name;
    }


}
