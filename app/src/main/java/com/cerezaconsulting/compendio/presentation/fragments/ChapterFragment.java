package com.cerezaconsulting.compendio.presentation.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseActivity;
import com.cerezaconsulting.compendio.core.BaseFragment;
import com.cerezaconsulting.compendio.core.ScrollChildSwipeRefreshLayout;
import com.cerezaconsulting.compendio.data.events.MessageChapterCompleteEvent;
import com.cerezaconsulting.compendio.data.local.SessionManager;
import com.cerezaconsulting.compendio.data.model.ChapterEntity;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.presentation.activities.FragmentsActivity;
import com.cerezaconsulting.compendio.presentation.activities.QuestionActivity;
import com.cerezaconsulting.compendio.presentation.adapters.CardFragmentPagerAdapter;
import com.cerezaconsulting.compendio.presentation.adapters.ChapterAdapter;
import com.cerezaconsulting.compendio.presentation.adapters.ShadowTransformer;
import com.cerezaconsulting.compendio.presentation.contracts.ChapterContract;
import com.cerezaconsulting.compendio.presentation.fragments.dialog.AlertConfirmDialog;
import com.cerezaconsulting.compendio.presentation.fragments.dialog.AlertConfirmDialogOff;
import com.cerezaconsulting.compendio.presentation.presenters.communicator.CommunicatorChapterItem;
import com.cerezaconsulting.compendio.utils.ListLinks;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 16/03/17.
 */

public class ChapterFragment extends BaseFragment implements ChapterContract.View {

    private static final int REQUEST_FRAGMENT = 200;

    @BindView(R.id.tv_light_bulb)
    TextView tvLightBulb;
    @BindView(R.id.tv_advance)
    TextView tvAdvance;
    @BindView(R.id.tv_number_advance)
    TextView tvNumberAdvance;
    @BindView(R.id.rv_items)
    RecyclerView rvItems;
    @BindView(R.id.ll_items)
    LinearLayout llItems;
    @BindView(R.id.iv_no_items)
    ImageView ivNoItems;
    @BindView(R.id.tv_no_items)
    TextView tvNoItems;
    @BindView(R.id.ll_no_items)
    LinearLayout llNoItems;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    @BindView(R.id.refresh_layout)
    ScrollChildSwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private CourseEntity courseEntity;
    private ChapterContract.Presenter presenter;
    private LinearLayoutManager layoutManager;
    private ChapterAdapter adapter;
    private SessionManager sessionManager;
    private CardFragmentPagerAdapter pagerAdapter;
    private ChapterContract.Presenter mPresenter;

    public static ChapterFragment newInstance(Bundle bundle) {
        ChapterFragment fragment = new ChapterFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(boolean event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChapterEntity event) {
/*
        if (event != null) {

            for (int i = 0; i < courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size(); i++) {
                if (event.getId().equals(courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(i).getId())) {

                    CoursesEntity courseEntity = sessionManager.getCoures();

                    viewPager.setCurrentItem(i);

                    *//*
                    Log.e("EVENT", "---" + i + "--");
                    viewPager.setCurrentItem(i);
                    pagerAdapter.getItem(i).onResume();*//*

                    //EventBus.getDefault().postSticky(true);
                    *//*viewPager.setCurrentItem(i);
                    ((CardFragment) pagerAdapter.getItem(i)).refreshChapter(event);
                    EventBus.getDefault().postSticky(true);*//*
                    return;
                }
            }
        }*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageChapterCompleteEvent event) {
        if (event != null) {

            if (event.isFinishedChapter()) {
                getActivity().finish();
            }

            for (int i = 0; i < courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size(); i++) {
                if (event.getChapterEntity().getId().equals(courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(i).getId())) {


                    event.getCourseEntity().getTrainingEntity().getRelease().getCourse().getChapters().set(i, event.getChapterEntity());
                    viewPager.setCurrentItem(i);
                    pagerAdapter = new CardFragmentPagerAdapter(event.getCourseEntity(), getActivity().getSupportFragmentManager(), dpToPixels(2, getContext()),
                            event.getCourseEntity().getTrainingEntity().getRelease().getCourse().getChapters());

                    viewPager.setAdapter(null);
                    viewPager.setAdapter(pagerAdapter);
                    ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
                    fragmentCardShadowTransformer.enableScaling(true);

                    viewPager.setAdapter(pagerAdapter);
                    viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
                    viewPager.setOffscreenPageLimit(3);

                    tvLightBulb.setText(event.getCourseEntity().getTrainingEntity().getIntellect() + "%");
                    tvAdvance.setText(event.getCourseEntity().getTrainingEntity().getProgress() + "%");
                    tvNumberAdvance.setText(event.getCourseEntity().getTrainingEntity().getPosition() + "%");

                    openNextChapter(event.getCourseEntity(), event.getChapterEntity());
                    // openNextChapter(event.getCourseEntity(), event.getChapterEntity());
                    /*
                    Log.e("EVENT", "---" + i + "--");
                    viewPager.setCurrentItem(i);
                    pagerAdapter.getItem(i).onResume();*/

                    //EventBus.getDefault().postSticky(true);
                    /*viewPager.setCurrentItem(i);
                    ((CardFragment) pagerAdapter.getItem(i)).refreshChapter(event);
                    EventBus.getDefault().postSticky(true);*/
                    return;
                }
            }
        }
    }
   /* @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(MessageChapterCompleteEvent event) {
        Log.e("EVENT", "----");
        if (event != null) {
            pagerAdapter = new CardFragmentPagerAdapter(courseEntity, getActivity().getSupportFragmentManager(), dpToPixels(2, getContext()),
                    event.getCourseEntity().getTrainingEntity().getRelease().getCourse().getChapters());

            openNextChapter(event.getCourseEntity(), event.getChapterEntity());

        }

    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        courseEntity = (CourseEntity) getArguments().getSerializable("course");
        sessionManager = new SessionManager(getContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chapter, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onResume() {
        super.onResume();
        updatePager();
    }


    private void updatePager() {
        ArrayList<CourseEntity> courseEntities = sessionManager.getCoures().getCourseEntities();

        for (int i = 0; i < courseEntities.size(); i++) {

            if (courseEntities.get(i).getId().equals(courseEntity.getId())) {
                this.courseEntity = courseEntities.get(i);
                return;
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btn_save:
                AlertConfirmDialogOff alertConfirmDialog = new AlertConfirmDialogOff(getActivity(), this,
                        "Aceptar", courseEntity.getId());
                alertConfirmDialog.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chapter, container, false);
        ButterKnife.bind(this, root);
        setHasOptionsMenu(true);

        return root;
    }

    /**
     * Change value in dp to pixels
     *
     * @param dp
     * @param context
     * @return
     */
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItems.setLayoutManager(layoutManager);

        adapter = new ChapterAdapter(new ArrayList<ChapterEntity>(), (CommunicatorChapterItem) presenter);
        rvItems.setAdapter(adapter);
        getChapter(courseEntity.getTrainingEntity().getRelease().getCourse().getChapters());
        tvDescription.setText(courseEntity.getDescription());

        pagerAdapter =
                new CardFragmentPagerAdapter(courseEntity, getActivity().getSupportFragmentManager(), dpToPixels(2, getContext()),
                        courseEntity.getTrainingEntity().getRelease().getCourse().getChapters());

        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);


        tvLightBulb.setText(courseEntity.getTrainingEntity().getIntellect() + "%");
        tvAdvance.setText(courseEntity.getTrainingEntity().getProgress() + "%");
        tvNumberAdvance.setText(courseEntity.getTrainingEntity().getPosition() + "%");


        int count = 0;
        for (int i = 0; i < courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size(); i++) {
            if (courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(i).isFinished()) {
                //viewPager.setCurrentItem(i);
                count++;
            }
        }

        if (count == courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size()) {
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setCurrentItem(count);
        }

       /* viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                //Not used
            }

            @Override
            public void onPageSelected(int position) {
                //Force the fragment to reload its data
                Fragment f = mAdapter.getItem(position);
                f.onResume();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //Not used
            }
        });*/
    }


    @Override
    public void getChapter(ArrayList<ChapterEntity> list) {
        adapter.setItems(list);
        if (list.size() != 0) {
            llNoItems.setVisibility(View.GONE);
        }
    }

    @Override
    public void detailChapter(ChapterEntity chapterEntity) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("course", courseEntity);
        bundle.putSerializable("chapter", chapterEntity);
        nextActivity(getActivity(), bundle, QuestionActivity.class, false, REQUEST_FRAGMENT);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void sendDoubt(String s, String idTraining) {
        presenter.sendDoubt(s, idTraining);
    }

    @Override
    public void setPresenter(ChapterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(() -> srl.setRefreshing(active));

    }

    @Override
    public void showMessage(String msg) {
        ((BaseActivity) getActivity()).showMessage(msg);
    }

    @Override
    public void showErrorMessage(String message) {
        ((BaseActivity) getActivity()).showMessageError(message);
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_FRAGMENT) {

                getActivity().setResult(Activity.RESULT_OK, data);

                CourseEntity courseEntity = (CourseEntity) data.getSerializableExtra("course");
                ChapterEntity chapterEntity = (ChapterEntity) data.getSerializableExtra("chapter");


                pagerAdapter =
                        new CardFragmentPagerAdapter(courseEntity, getActivity().getSupportFragmentManager(), dpToPixels(2, getContext()),
                                courseEntity.getTrainingEntity().getRelease().getCourse().getChapters());
                openNextChapter(courseEntity, chapterEntity);
            }

        }
    }*/

    private void openNextChapter(CourseEntity courseEntity, ChapterEntity chapterEntity) {

        for (int i = 0; i < courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size(); i++) {

            if (courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(i).getId().
                    equals(chapterEntity.getId())) {

                if (i == courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size() - 1) {
                    for (int j = 0; j < i; j++) {
                        if (!courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(j).isFinished()) {
                            openFragmentActivity(courseEntity, courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(j));
                            viewPager.setCurrentItem(j);
                            return;
                        }
                    }
                } else {
                    for (int j = i + 1; j < courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size(); j++) {

                        if (!courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(j).isFinished()) {
                            openFragmentActivity(courseEntity, courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(j));
                            viewPager.setCurrentItem(j);
                            return;
                        } else {
                            if (j == courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size() - 1) {

                                for (int k = 0; k <= i; k++) {
                                    if (!courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(k).isFinished()) {
                                        openFragmentActivity(courseEntity, courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(k));
                                        viewPager.setCurrentItem(k);
                                        return;
                                    }
                                }

                            }
                        }


                    }
                }


            }

        }
    }


    private void openFragmentActivity(CourseEntity courseEntity, ChapterEntity chapterEntity) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("chapter", chapterEntity);
        bundle.putSerializable("chapters", courseEntity.getTrainingEntity().getRelease().getCourse().getChapters());
        bundle.putSerializable("course", courseEntity);
        Intent intent = new Intent(getActivity(), FragmentsActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, REQUEST_FRAGMENT);

    }
}
