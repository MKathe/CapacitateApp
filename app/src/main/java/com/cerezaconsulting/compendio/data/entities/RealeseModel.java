package com.cerezaconsulting.compendio.data.entities;

import java.io.Serializable;

/**
 * Created by junior on 10/04/17.
 */

public class RealeseModel implements Serializable {
    private String id;
    private String start;
    private String end;
    private String total;
    private String course;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
