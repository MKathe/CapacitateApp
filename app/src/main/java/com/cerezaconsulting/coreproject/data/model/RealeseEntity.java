package com.cerezaconsulting.coreproject.data.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by junior on 24/03/17.
 */

public class RealeseEntity implements Serializable {

    private int id;
    private long start;
    private long end;
    private int total;
    private CourseEntity course;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }
}
