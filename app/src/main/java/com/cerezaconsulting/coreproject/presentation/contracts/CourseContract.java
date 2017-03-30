package com.cerezaconsulting.coreproject.presentation.contracts;

import com.cerezaconsulting.coreproject.core.BasePresenter;
import com.cerezaconsulting.coreproject.core.BaseView;
import com.cerezaconsulting.coreproject.data.model.CourseEntity;

import java.util.ArrayList;

/**
 * Created by miguel on 15/03/17.
 */

public interface CourseContract {
    interface View extends BaseView<Presenter>{
        boolean isActive();
        void getCourses(ArrayList<CourseEntity> list);
        void detailCourse(CourseEntity courseEntity);

        void openCourse(CourseEntity courseEntity);
    }
    interface Presenter extends BasePresenter{

        void downloadCourseById(String idTraining);

        void loadCourses();

        void loadCoursesFromLocalRepository();
    }
}
