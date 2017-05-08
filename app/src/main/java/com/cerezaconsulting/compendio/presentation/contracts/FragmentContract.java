package com.cerezaconsulting.compendio.presentation.contracts;

import com.cerezaconsulting.compendio.core.BasePresenter;
import com.cerezaconsulting.compendio.core.BaseView;
import com.cerezaconsulting.compendio.data.model.ActivityEntity;
import com.cerezaconsulting.compendio.data.model.FragmentEntity;
import com.cerezaconsulting.compendio.data.model.ReviewEntity;

import java.util.ArrayList;

/**
 * Created by miguel on 15/03/17.
 */

public interface FragmentContract {
    interface View extends BaseView<Presenter> {

        void showFragments(ArrayList<FragmentEntity> fragmentEntities);

    }

    interface Presenter extends BasePresenter {

        void saveFragment(FragmentEntity fragmentEntity, String idTraining, String idChapter);

        void loadFragments(String idTraining, String idChapter);

    }
}
