package com.cerezaconsulting.compendio.presentation.presenters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.cerezaconsulting.compendio.data.local.CompedioDbHelper;
import com.cerezaconsulting.compendio.data.local.CompendioPersistenceContract;
import com.cerezaconsulting.compendio.data.local.SessionManager;
import com.cerezaconsulting.compendio.data.model.ActivityEntity;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.data.model.CoursesEntity;
import com.cerezaconsulting.compendio.data.model.FragmentEntity;
import com.cerezaconsulting.compendio.data.model.ReviewEntity;
import com.cerezaconsulting.compendio.data.model.TrainingEntity;
import com.cerezaconsulting.compendio.data.remote.ServiceFactory;
import com.cerezaconsulting.compendio.data.remote.request.CourseRequest;
import com.cerezaconsulting.compendio.data.remote.request.SyncRequest;
import com.cerezaconsulting.compendio.data.response.ActivityResponse;
import com.cerezaconsulting.compendio.data.response.ResponseActivitySync;
import com.cerezaconsulting.compendio.data.response.ReviewResponse;
import com.cerezaconsulting.compendio.data.response.TrackReviewResponse;
import com.cerezaconsulting.compendio.presentation.contracts.FragmentContract;
import com.cerezaconsulting.compendio.presentation.contracts.SyncContrac;
import com.cerezaconsulting.compendio.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by junior on 27/04/17.
 */

public class FragmentPresenter implements FragmentContract.Presenter {

    private FragmentContract.View mView;
    private Context context;
    private CompedioDbHelper mCompedioDbHelper;


    public FragmentPresenter(FragmentContract.View mView, Context context) {
        this.mView = mView;
        this.mView.setPresenter(this);
        this.context = context;
        this.mCompedioDbHelper = new CompedioDbHelper(context);


    }

    public void getFragments(String idTrainig, String idChapter) {


        List<FragmentEntity> fragmentEntities = new ArrayList<>();
        SQLiteDatabase db = mCompedioDbHelper.getReadableDatabase();

        String[] projection = {
                CompendioPersistenceContract.FragmentEntity._ID,
                CompendioPersistenceContract.FragmentEntity.TITLE,
                CompendioPersistenceContract.FragmentEntity.CONTENT,
                CompendioPersistenceContract.FragmentEntity.TRAINING,
                CompendioPersistenceContract.FragmentEntity.CHAPTER,

        };


        String[] args = new String[]{idTrainig, idChapter};
        Cursor c = db.query(
                CompendioPersistenceContract.FragmentEntity.TABLE_NAME,
                projection, "idTraning = ? AND idChapter = ?", args, null, null, null);


        FragmentEntity fragmentEntity = null;

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.FragmentEntity._ID));
                String title = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.FragmentEntity.TITLE));
                String content = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.FragmentEntity.CONTENT));

                fragmentEntity = new FragmentEntity(id, title, content);

                fragmentEntities.add(fragmentEntity);

            }

        }
        if (c != null) {
            c.close();
        }

        db.close();

        mView.showFragments((ArrayList<FragmentEntity>) fragmentEntities);
    }


    @Override
    public void start() {


    }


    @Override
    public void saveFragment(FragmentEntity fragmentEntity, String idTraining, String idChapter) {

    }

    @Override
    public void loadFragments(String idTraining, String idChapter) {
        getFragments(idTraining, idChapter);
    }
}
