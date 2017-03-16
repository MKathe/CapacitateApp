package com.cerezaconsulting.coreproject.data.model;

import java.io.Serializable;

/**
 * Created by miguel on 15/03/17.
 */

public class CourseEntity implements Serializable {
    private String id;
    private String name;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
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
