package com.cerezaconsulting.compendio.presentation.contracts;

import com.cerezaconsulting.compendio.core.BasePresenter;
import com.cerezaconsulting.compendio.core.BaseView;
import com.cerezaconsulting.compendio.data.model.ActivityEntity;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.data.model.ReviewEntity;

import java.util.ArrayList;

/**
 * Created by miguel on 15/03/17.
 */

public interface SyncContrac {
    interface View extends BaseView<Presenter> {

        void syncSuccess();

        void syncFailed();

        void tryAgainDownloand();

        void notSync();

        void syncFinalize();

    }

    interface Presenter extends BasePresenter {


        void syncActivity(ActivityEntity activityEntity, String idTrainig);

        void syncReview(ReviewEntity reviewEntity);

        void syncTryAgain();

        void syncFinalize();

        void tryAgainDowloandCourses();

    }
}
