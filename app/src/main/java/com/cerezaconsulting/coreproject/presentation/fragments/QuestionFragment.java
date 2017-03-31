package com.cerezaconsulting.coreproject.presentation.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerezaconsulting.coreproject.R;
import com.cerezaconsulting.coreproject.core.BaseFragment;
import com.cerezaconsulting.coreproject.data.events.MessageActivityEvent;
import com.cerezaconsulting.coreproject.data.events.MessageChapterCompleteEvent;
import com.cerezaconsulting.coreproject.data.local.SessionManager;
import com.cerezaconsulting.coreproject.data.model.ActivityEntity;
import com.cerezaconsulting.coreproject.data.model.ChapterEntity;
import com.cerezaconsulting.coreproject.data.model.CourseEntity;
import com.cerezaconsulting.coreproject.data.model.CoursesEntity;
import com.cerezaconsulting.coreproject.data.model.QuestionEntity;
import com.cerezaconsulting.coreproject.utils.CompendioUtils;
import com.viewpagerindicator.CirclePageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by miguel on 16/03/17.
 */

public class QuestionFragment extends BaseFragment {


    @BindView(R.id.aut_scroll)
    AutoScrollViewPager autScroll;
    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;
    @BindView(R.id.title_chapter)
    TextView titleChapter;
    @BindView(R.id.tv_intellect)
    TextView tvIntellect;
    @BindView(R.id.next_button_chapter)
    Button nextButtonChapter;
    @BindView(R.id.title_course)
    TextView titleCourse;
    @BindView(R.id.tv_review)
    TextView tvReview;
    @BindView(R.id.next_course_complete)
    Button nextCourseComplete;
    @BindView(R.id.title_course_review)
    TextView titleCourseReview;
    @BindView(R.id.tv_review_next)
    TextView tvReviewNext;
    @BindView(R.id.button_review_complete)
    Button buttonReviewComplete;
    @BindView(R.id.frame_principal)
    RelativeLayout framePrincipal;
    @BindView(R.id.ly_chapter_complete)
    RelativeLayout lyChapterComplete;
    @BindView(R.id.ly_course_complete)
    RelativeLayout lyCourseComplete;
    @BindView(R.id.ly_review_complete)
    LinearLayout lyReviewComplete;


    private ArrayList<QuestionEntity> questionEntities;
    private ActivityEntity mActivityEntity;
    private int currentItem = 0;
    private ChapterEntity mChapterEntity;
    private CourseEntity mCourseEntity;
    private SessionManager mSessionManager;


    private CoursesEntity coursesEntity;

    public static QuestionFragment newInstance(Bundle bundle) {
        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Subscribe
    public void onCompletedChapterEvent(MessageChapterCompleteEvent event) {

    }

    @Subscribe
    public void onEvent(ChapterEntity chapterEntity) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageActivityEvent event) {

        if (event != null) {

            currentItem++;
            autScroll.setCurrentItem(currentItem);
            if (mActivityEntity != null) {
                if (event.isCorret()) {
                    mActivityEntity.incrementCorrect();
                } else {
                    mActivityEntity.incrementIncorrect();
                    mActivityEntity.addIdFragmentToPoorly(event.getIdFragment());
                }
            }
            if (isEndToQuestionary()) {
                framePrincipal.setVisibility(View.GONE);
                mActivityEntity.calculateIntellect(questionEntities.size());
                saveProgressCourse();

                if (isToEndCourse()) {
                    lyCourseComplete.setVisibility(View.VISIBLE);
                    titleCourse.setText(mCourseEntity.getName());
                } else {
                    lyChapterComplete.setVisibility(View.VISIBLE);
                    titleChapter.setText(mChapterEntity.getName());
                    tvIntellect.setText(mCourseEntity.getTrainingEntity().getIntellect() + " %");

                }


            }

        }
        /* Do something */

    }

    ;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = new SessionManager(getContext());
        questionEntities = (ArrayList<QuestionEntity>) getArguments().getSerializable("question");
        mChapterEntity = (ChapterEntity) getArguments().getSerializable("chapter");
        mCourseEntity = (CourseEntity) getArguments().getSerializable("course");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_questions, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InformationAdapter imagePagerAdapter = new InformationAdapter(getChildFragmentManager(), questionEntities);
        mActivityEntity = new ActivityEntity();
        autScroll.setAdapter(imagePagerAdapter);
        indicator.setViewPager(autScroll);
        mActivityEntity = new ActivityEntity();
        mActivityEntity.setIdChapter(mChapterEntity.getId());
    }


    private boolean isEndToQuestionary() {
        return currentItem == questionEntities.size();
    }

    private boolean isToEndCourse() {
        int count = 0;
        for (int i = 0; i < mCourseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size(); i++) {
            if (mCourseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(i).isFinished()) {
                count++;
            }

        }

        if (count == mCourseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size()) {
            return true;
        } else {
            return false;
        }
    }

    private void saveProgressCourse() {

        SessionManager sessionManager = new SessionManager(getContext());
        coursesEntity = sessionManager.getCoures();

        if (mCourseEntity.getTrainingEntity().getActivityEntities() == null) {
            mCourseEntity.getTrainingEntity().setActivityEntities(new ArrayList<ActivityEntity>());
        }
        mCourseEntity.getTrainingEntity().getActivityEntities().add(mActivityEntity);
        mCourseEntity.getTrainingEntity().
                setIntellect(CompendioUtils.round(CompendioUtils.calculateIntellect(mCourseEntity.getTrainingEntity().getActivityEntities()), 2));
        mCourseEntity.getTrainingEntity().
                setProgress(CompendioUtils.round(CompendioUtils.calculateProgress(mCourseEntity.getTrainingEntity()), 2));
        mChapterEntity.setFinished(true);

        for (int i = 0; i < mCourseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size(); i++) {
            if (mChapterEntity.getId().equals(mCourseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(i).getId())) {
                mCourseEntity.getTrainingEntity().getRelease().getCourse().getChapters().set(i, mChapterEntity);
                break;
            }
        }
        for (int i = 0; i < coursesEntity.getCourseEntities().size(); i++) {
            if (coursesEntity.getCourseEntities().get(i).getId().equals(mCourseEntity.getId())) {
                coursesEntity.getCourseEntities().set(i, mCourseEntity);
                break;
            }
        }
        sessionManager.setCourses(coursesEntity);
    }

    @OnClick({R.id.next_button_chapter, R.id.next_course_complete, R.id.button_review_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button_chapter:
                //EventBus.getDefault().postSticky(mChapterEntity);
                EventBus.getDefault().postSticky(new MessageChapterCompleteEvent(mChapterEntity,
                        coursesEntity, mCourseEntity));
                Intent intent = new Intent();
                intent.putExtra("courses", coursesEntity);
                intent.putExtra("chapter", mChapterEntity);
                intent.putExtra("course", mCourseEntity);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();

                break;
            case R.id.next_course_complete:
                break;
            case R.id.button_review_complete:
                break;
        }
    }

    public static class InformationAdapter extends FragmentPagerAdapter {
        List<QuestionEntity> informationEntities;


        public InformationAdapter(FragmentManager fragmentManager, List<QuestionEntity> informationEntities) {
            super(fragmentManager);
            this.informationEntities = informationEntities;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return informationEntities.size();
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return QuestionPageFragment.newInstance(informationEntities.get(position));
            //return InformationPageFragment.newInstance(informationEntities.get(position), "Page # 1");
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
}
