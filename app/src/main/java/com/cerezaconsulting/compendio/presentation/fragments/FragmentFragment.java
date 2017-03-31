package com.cerezaconsulting.compendio.presentation.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseFragment;
import com.cerezaconsulting.compendio.data.model.ChapterEntity;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.data.model.FragmentEntity;
import com.cerezaconsulting.compendio.data.model.QuestionEntity;
import com.cerezaconsulting.compendio.presentation.activities.QuestionActivity;
import com.cerezaconsulting.compendio.presentation.adapters.FragmentAdapter;
import com.cerezaconsulting.compendio.presentation.presenters.communicator.CommunicatorChapterItem;
import com.cerezaconsulting.compendio.utils.CompendioUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 15/03/17.
 */

public class FragmentFragment extends BaseFragment implements CommunicatorChapterItem {

    private static final int REQUEST_QUESTIONARY = 300;

    @BindView(R.id.complatins_list)
    RecyclerView complatinsList;
    @BindView(R.id.complatinsLL)
    LinearLayout complatinsLL;
    @BindView(R.id.noComplatinsIcon)
    ImageView noComplatinsIcon;
    @BindView(R.id.noComplatinsMain)
    TextView noComplatinsMain;
    @BindView(R.id.noComplatins)
    LinearLayout noComplatins;
    @BindView(R.id.clinic_container)
    RelativeLayout clinicContainer;


    private FragmentAdapter fragmentAdapter;
    private LinearLayoutManager layoutManager;
    private ChapterEntity chapterEntity;
    private ArrayList<ChapterEntity> chapterEntities;
    private CourseEntity courseEntity;

    public static FragmentFragment newInstance(Bundle bundle) {
        FragmentFragment fragment = new FragmentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.chapterEntities = (ArrayList<ChapterEntity>) getArguments().getSerializable("chapters");
        this.chapterEntity = (ChapterEntity) getArguments().getSerializable("chapter");
        this.courseEntity = (CourseEntity) getArguments().getSerializable("course");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_fragments, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutManager = new LinearLayoutManager(getContext());
        complatinsList.setLayoutManager(layoutManager);

        fragmentAdapter = new FragmentAdapter(chapterEntity.getFragments(), getContext(), this);


        complatinsList.setAdapter(fragmentAdapter);
        noComplatins.setVisibility((chapterEntity.getFragments().size() > 0) ? View.GONE : View.VISIBLE);


    }

    public ArrayList<FragmentEntity> getAllFragments(ArrayList<ChapterEntity> chapterEntities) {
        ArrayList<FragmentEntity> fragmentEntities = new ArrayList<>();
        for (int i = 0; i < chapterEntities.size(); i++) {
            fragmentEntities.addAll(chapterEntities.get(i).getFragments());
        }


        return fragmentEntities;
    }

    @Override
    public void onClick(ChapterEntity chapterEntity) {

    }

    @Override
    public void openQuestions() {
        ArrayList<FragmentEntity> fragmentEntities = getAllFragments(chapterEntities);
        ArrayList<QuestionEntity> questionEntities = CompendioUtils.getQuestions(chapterEntity, fragmentEntities);
        Bundle bundle = new Bundle();
        bundle.putSerializable("chapter", chapterEntity);
        bundle.putSerializable("question", questionEntities);
        bundle.putSerializable("course", courseEntity);
        nextActivity(getActivity(), bundle, QuestionActivity.class, false, REQUEST_QUESTIONARY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_QUESTIONARY){

                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }

        }
    }


}
