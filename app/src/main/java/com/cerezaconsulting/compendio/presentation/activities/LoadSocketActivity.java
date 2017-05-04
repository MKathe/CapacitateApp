package com.cerezaconsulting.compendio.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseActivity;
import com.cerezaconsulting.compendio.data.events.SyncProcessSocketEvent;
import com.cerezaconsulting.compendio.presentation.contracts.SyncContrac;
import com.cerezaconsulting.compendio.presentation.presenters.SyncPresenter;
import com.cerezaconsulting.compendio.services.SocketService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by miguel on 16/03/17.
 */

public class LoadSocketActivity extends BaseActivity implements SyncContrac.View {


    @BindView(R.id.btn_esc)
    Button btnEsc;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.layout_conected)
    LinearLayout layoutConected;
    @BindView(R.id.layout_disconected)
    LinearLayout layoutDisconected;
    @BindView(R.id.btn_again)
    Button btnAgain;
    @BindView(R.id.layout_error)
    LinearLayout layoutError;

    private SyncContrac.Presenter mSyncPresenter;
    private int isDownload = 0;

    @Subscribe
    public void connectedSocketProcess(SyncProcessSocketEvent event) {


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.fragment_loading_socket);
        ButterKnife.bind(this);
        mSyncPresenter = new SyncPresenter(this, this);
        mSyncPresenter.start();
    }


    @Override
    public void syncSuccess() {

        Log.d("SYNC", "exito");
        layoutError.setVisibility(View.GONE);
        layoutDisconected.setVisibility(View.GONE);
        layoutConected.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new SyncProcessSocketEvent(1));
        // Toast.makeText(this, "EXITO", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void syncFailed() {
        Log.d("SYNC", "no exito");
        isDownload = 0;
        //Toast.makeText(this, "FALLO", Toast.LENGTH_SHORT).show();
        layoutError.setVisibility(View.VISIBLE);
        layoutDisconected.setVisibility(View.GONE);
        layoutConected.setVisibility(View.GONE);
    }

    @Override
    public void tryAgainDownloand() {
        isDownload = 1;
        layoutError.setVisibility(View.GONE);
        layoutDisconected.setVisibility(View.GONE);
        layoutConected.setVisibility(View.VISIBLE);

    }

    @Override
    public void notSync() {


    }

    @Override
    public void syncFinalize() {
        Toast.makeText(this, "Desconectando...", Toast.LENGTH_SHORT).show();
        stopService(new Intent(getBaseContext(), SocketService.class));
        finish();
    }

    @Override
    public void setPresenter(SyncContrac.Presenter presenter) {
        mSyncPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.btn_again, R.id.btn_esc, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_again:
                if (isDownload == 0) {
                    mSyncPresenter.syncTryAgain();
                } else {
                    mSyncPresenter.tryAgainDowloandCourses();
                }

                layoutError.setVisibility(View.GONE);
                layoutDisconected.setVisibility(View.GONE);
                layoutConected.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_esc:
                mSyncPresenter.syncFinalize();


                break;
            case R.id.btn_cancel:
                mSyncPresenter.tryAgainDowloandCourses();
                break;
            case R.id.btn_cancel_off:
                mSyncPresenter.tryAgainDowloandCourses();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false; //I have tried here true also
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

    }
}
