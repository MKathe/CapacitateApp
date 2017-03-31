package com.cerezaconsulting.compendio.presentation.contracts;

import com.cerezaconsulting.compendio.core.BasePresenter;
import com.cerezaconsulting.compendio.core.BaseView;
import com.cerezaconsulting.compendio.data.model.FragmentEntity;

import java.util.ArrayList;

/**
 * Created by miguel on 16/03/17.
 */

public interface FragmentsContract {
    interface View extends BaseView<Presenter>{
        boolean isActive();
        void getFragments(ArrayList<FragmentEntity> list);
    }
    interface Presenter extends BasePresenter{
        void getFragments(String id);
    }
}
