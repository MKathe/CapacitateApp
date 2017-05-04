package com.cerezaconsulting.compendio.presentation.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseActivityLogin;
import com.cerezaconsulting.compendio.core.BaseFragment;
import com.cerezaconsulting.compendio.presentation.activities.ExampleActivity;
import com.cerezaconsulting.compendio.presentation.activities.PanelActivity;
import com.cerezaconsulting.compendio.presentation.contracts.LoginContract;
import com.cerezaconsulting.compendio.utils.ProgressDialogCustom;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by junior on 27/08/16.
 */
public class LoginFragment extends BaseFragment implements LoginContract.View, Validator.ValidationListener {

    private static final String TAG = ExampleActivity.class.getSimpleName();

    @NotEmpty(message = "Este campo no puede ser vacio")
    @BindView(R.id.etEmail)
    EditText etEmail;

    @NotEmpty(message = "Este campo no puede ser vacio")
    @BindView(R.id.etpassword)
    EditText etpassword;


    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.et_forget_password)
    TextView etForgetPassword;


    private Validator mValidator;

    private LoginContract.Presenter mPresenter;

    private ProgressDialogCustom mProgressDialogCustom;


    public LoginFragment() {
        // Requires empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressDialogCustom = new ProgressDialogCustom(getContext(), "Ingresando...");
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);


    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
        if (active) {
            mProgressDialogCustom.show();
        } else {
            if (mProgressDialogCustom.isShowing()) {
                mProgressDialogCustom.dismiss();
            }
        }


    }

    @Override
    public void showMessage(String msg) {
        ((BaseActivityLogin) getActivity()).showMessage(msg);
    }

    @Override
    public void showErrorMessage(String message) {
        ((BaseActivityLogin) getActivity()).showMessageError(message);
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void loginSuccess() {
        newActivityClearPreview(getActivity(), null, PanelActivity.class);
    }

    @OnClick({R.id.btn_login, R.id.et_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mValidator.validate();
                break;
            case R.id.et_forget_password:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onValidationSucceeded() {
        mPresenter.loginNormal(etEmail.getText().toString(), etpassword.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void chagePasswordDialog (){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Escribe tu correo");

// Set up the input
        final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }


    @OnClick(R.id.et_forget_password)
    public void onClick() {

        chagePasswordDialog();
    }
}
