package com.cerezaconsulting.compendio.presentation.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseFragment;
import com.cerezaconsulting.compendio.data.events.MessageActivityEvent;
import com.cerezaconsulting.compendio.data.events.MessageChapterCompleteEvent;
import com.cerezaconsulting.compendio.data.local.SessionManager;
import com.cerezaconsulting.compendio.data.model.ActivityEntity;
import com.cerezaconsulting.compendio.data.model.ChapterEntity;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.data.model.CoursesEntity;
import com.cerezaconsulting.compendio.data.model.QuestionEntity;
import com.cerezaconsulting.compendio.data.model.ReviewEntity;
import com.cerezaconsulting.compendio.presentation.activities.QuestionActivity;
import com.cerezaconsulting.compendio.utils.CompendioUtils;
import com.cerezaconsulting.compendio.utils.CompendioViewPager;
import com.cerezaconsulting.compendio.utils.DateUtils;
import com.viewpagerindicator.CirclePageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by miguel on 16/03/17.
 */

public class QuestionReviewFragment extends BaseFragment {


    CompendioViewPager autScroll;
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
    private boolean questionEnd = false;


    private CoursesEntity coursesEntity;

    public static QuestionReviewFragment newInstance(Bundle bundle) {
        QuestionReviewFragment fragment = new QuestionReviewFragment();
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

                if (mCourseEntity.getTrainingEntity().getActivityEntities() != null) {
                    mCourseEntity.getTrainingEntity().setActivityEntities(new ArrayList<ActivityEntity>());
                }
                mCourseEntity.getTrainingEntity().getActivityEntities().add(mActivityEntity);
                questionEnd = true;
                lyReviewComplete.setVisibility(View.VISIBLE);
                ReviewEntity reviewEntity = mCourseEntity.getTrainingEntity().getReviewEntities().get(0);
                reviewEntity.setOffline(true);
                reviewEntity.setCompleted(true);
                mCourseEntity.getTrainingEntity()
                        .setIntellect(CompendioUtils
                                .round(CompendioUtils.getTotalIntellect(mCourseEntity.getTrainingEntity()), 2));

                saveAndGenerateReview(reviewEntity);
               /* generateAdnSaveReview();
                saveProgressCourse();*/

                /*if (isToEndCourse()) {
                    generateAdnSaveReview();
                    questionEnd = true;
                    lyCourseComplete.setVisibility(View.VISIBLE);
                    titleCourse.setText(mCourseEntity.getName());
                    customToolbarChapterComplete(false, getString(R.string.course_complete));


                } else {
                    questionEnd = true;
                    lyChapterComplete.setVisibility(View.VISIBLE);
                    titleChapter.setText(mChapterEntity.getName());
                    tvIntellect.setText(mCourseEntity.getTrainingEntity().getIntellect() + " %");
                    customToolbarChapterComplete(false, mChapterEntity.getName());

                }*/


            }

        }

    }

    private void saveAndGenerateReview(ReviewEntity reviewEntity) {
        SessionManager sessionManager = new SessionManager(getContext());
        ArrayList<CourseEntity> courseEntities = sessionManager.getCoures().getCourseEntities();

        ReviewEntity nextReviewEntity = new ReviewEntity();
        nextReviewEntity.setOffline(true);
        nextReviewEntity.setDate(CompendioUtils.getReviewDate(mCourseEntity.getTrainingEntity().getIntellect()));


        mCourseEntity.getTrainingEntity().getReviewEntities().set(0, reviewEntity);
        mCourseEntity.getTrainingEntity().getReviewEntities().add(nextReviewEntity);
        tvReviewNext.setText(DateUtils.getFormant(nextReviewEntity.getDate()));

        for (int i = 0; i < courseEntities.size(); i++) {
            if (mCourseEntity.getId().equals(courseEntities.get(i).getId())) {
                courseEntities.set(i, mCourseEntity);

            }

        }
        CoursesEntity coursesEntity = new CoursesEntity(courseEntities);
        sessionManager.setCourses(coursesEntity);
    }


    private void generateAdnSaveReview() {

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setOffline(true);
        reviewEntity.setDate(CompendioUtils.getReviewDate(mCourseEntity.getTrainingEntity().getIntellect()));
        mCourseEntity.getTrainingEntity().setReviewEntities(new ArrayList<ReviewEntity>());
        mCourseEntity.getTrainingEntity().getReviewEntities().add(reviewEntity);

        SessionManager sessionManager = new SessionManager(getContext());
        ArrayList<CourseEntity> courseEntities = sessionManager.getCoures().getCourseEntities();


        for (int i = 0; i < courseEntities.size(); i++) {
            if (mCourseEntity.getId().equals(courseEntities.get(i).getId())) {
                courseEntities.set(i, mCourseEntity);

            }

        }
        CoursesEntity coursesEntity = new CoursesEntity(courseEntities);

        sessionManager.setCourses(coursesEntity);


    }

    private void customToolbarChapterComplete(boolean active, String title) {
        ((QuestionActivity) getActivity()).showDisplayHowEnabled(active, title);
    }

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
        mCourseEntity = (CourseEntity) getArguments().getSerializable("course");
        questionEntities = CompendioUtils.getQuestions(mCourseEntity.getTrainingEntity());


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_questions, container, false);
        autScroll = (CompendioViewPager) root.findViewById(R.id.aut_scroll);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InformationAdapter imagePagerAdapter = new InformationAdapter(getChildFragmentManager(), questionEntities);
        mActivityEntity = new ActivityEntity();
        mActivityEntity.setOffline(true);
        autScroll.setAdapter(imagePagerAdapter);
        indicator.setViewPager(autScroll);
        autScroll.setPagingEnabled(false);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (!questionEnd) {
                        getActivity().onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        @ColorInt int color = typedValue.data;

        buttonReviewComplete.setBackgroundColor(color);


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
              /*  EventBus.getDefault().postSticky(new MessageChapterCompleteEvent(mChapterEntity,
                        coursesEntity, mCourseEntity));
                Intent intent_course = new Intent();
                intent_course.putExtra("courses", coursesEntity);
                intent_course.putExtra("chapter", mChapterEntity);
                intent_course.putExtra("course", mCourseEntity);
                getActivity().setResult(Activity.RESULT_OK, intent_course);*/
                getActivity().finish();
                break;
            case R.id.button_review_complete:
                Intent intent2 = new Intent();
                intent2.putExtra("course", mCourseEntity);
                getActivity().setResult(Activity.RESULT_OK, intent2);

                getActivity().finish();
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
