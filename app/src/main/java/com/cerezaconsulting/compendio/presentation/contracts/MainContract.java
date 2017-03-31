package com.cerezaconsulting.compendio.presentation.contracts;

import com.cerezaconsulting.compendio.core.BasePresenter;
import com.cerezaconsulting.compendio.core.BaseView;

/**
 * Especifica el contrato entre la vista y el presentador para logueo
 */
public interface MainContract {

    interface View extends BaseView<Presenter> {


        boolean isActive();
    }

    interface Presenter extends BasePresenter {


    }
}
