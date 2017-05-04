package com.cerezaconsulting.compendio.presentation.fragments.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.presentation.contracts.CourseContract;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by junior on 19/08/16.
 */
public class AlertConfirmDialog extends AlertDialog implements Validator.ValidationListener {


    private RelativeLayout lyClose;
    private Button btn1;
    private Button btn2;
    @NotEmpty(message = "Aún no ha escrito su duda")
    private EditText textConfirm;
    private RelativeLayout rlyClose;
    private CourseContract.View viewContract;
    private ImageView actionCloseDialog;
    private Validator validator;
    private String idTraining;

    public AlertConfirmDialog(Context context, final CourseContract.View viewContract, String button1, String textConfirmDialog) {
        super(context);
        this.viewContract = checkNotNull(viewContract, "view cannot be null!");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_doubt, null);
        setView(view);
        validator = new Validator(this);
        validator.setValidationListener(this);
        btn1 = (Button) view.findViewById(R.id.button_1);
        textConfirm = (EditText) view.findViewById(R.id.etDefaul);
        rlyClose = (RelativeLayout) view.findViewById(R.id.lyClose);
        actionCloseDialog = (ImageView) view.findViewById(R.id.ivCloseGuidelines);
        this.idTraining = textConfirmDialog;


        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        @ColorInt int color = typedValue.data;

        rlyClose.setBackgroundColor(color);
        btn1.setBackgroundColor(color);

        btn1.setText(button1);
        //btn2.setText(button2);
        // textConfirm.setText(textConfirmDialog);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        actionCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    @Override
    public void onValidationSucceeded() {
        viewContract.sendDoubt(textConfirm.getText().toString(), idTraining);
        this.dismiss();
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
}
