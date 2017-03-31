package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by miguel on 15/03/17.
 */

public class CourseEntity implements Serializable {
    private String id;
    private String name;
    private String description;
    private ArrayList<ChapterEntity> chapters;



    private boolean isOffLineDisposed = false;
    private TrainingEntity trainingEntity;

    public TrainingEntity getTrainingEntity() {
        return trainingEntity;
    }

    public void setTrainingEntity(TrainingEntity trainingEntity) {
        this.trainingEntity = trainingEntity;
    }

    public boolean isOffLineDisposed() {
        return isOffLineDisposed;
    }

    public void setOffLineDisposed(boolean offLineDisposed) {
        isOffLineDisposed = offLineDisposed;
    }

    public ArrayList<ChapterEntity> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<ChapterEntity> chapters) {
        this.chapters = chapters;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
