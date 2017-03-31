package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by junior on 24/03/17.
 */

public class CoursesEntity  implements Serializable{


    public CoursesEntity(ArrayList<CourseEntity> courseEntities) {
        this.courseEntities = courseEntities;
    }

    private ArrayList<CourseEntity> courseEntities;

    public ArrayList<CourseEntity> getCourseEntities() {
        return courseEntities;
    }

    public void setCourseEntities(ArrayList<CourseEntity> courseEntities) {
        this.courseEntities = courseEntities;
    }
}
