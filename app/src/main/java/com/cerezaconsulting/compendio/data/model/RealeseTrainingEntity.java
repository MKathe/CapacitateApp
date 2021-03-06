package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;

/**
 * Created by junior on 24/03/17.
 */

public class RealeseTrainingEntity implements Serializable {

    private int id;
    private String start;
    private String end;
    private int total;
    private CourseEntity course;


    public void setCourse(CourseEntity course) {
        this.course = course;
    }
    public String getCourse() {
        return course.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
