package com.cerezaconsulting.coreproject.presentation.contracts;

import com.cerezaconsulting.coreproject.core.BasePresenter;
import com.cerezaconsulting.coreproject.core.BaseView;

/**
 * Especifica el contrato entre la vista y el presentador para logueo
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {


        boolean isActive();

        void loginSuccess();

    }

    interface Presenter extends BasePresenter {


        void loginNormal(String username, String email);


    }
}
