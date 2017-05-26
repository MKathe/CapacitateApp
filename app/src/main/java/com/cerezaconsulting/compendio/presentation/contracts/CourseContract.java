package com.cerezaconsulting.compendio.presentation.contracts;

import com.cerezaconsulting.compendio.core.BasePresenter;
import com.cerezaconsulting.compendio.core.BaseView;
import com.cerezaconsulting.compendio.data.model.CourseEntity;

import java.util.ArrayList;

/**
 * Created by miguel on 15/03/17.
 */

public interface CourseContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void getCourses(ArrayList<CourseEntity> list);

        void detailCourse(CourseEntity courseEntity);

        void openCourse(CourseEntity courseEntity);

        void sendDoubt(String s, String id);

        void showDialogDoubt(String idTraining);


        void downloadingCourse(boolean active);
    }

    interface Presenter extends BasePresenter {

        void downloadCourseById(CourseEntity courseEntity);

        void loadCourses();

        void loadCoursesFromLocalRepository();

        void sendDoubt(String doubt, String id);
    }
}
