package com.cerezaconsulting.compendio.presentation.contracts;

import com.cerezaconsulting.compendio.core.BasePresenter;
import com.cerezaconsulting.compendio.core.BaseView;

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


        void requestChangePassword(String email);
    }
}
