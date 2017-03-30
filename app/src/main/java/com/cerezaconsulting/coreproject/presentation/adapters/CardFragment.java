package com.cerezaconsulting.coreproject.presentation.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cerezaconsulting.coreproject.R;
import com.cerezaconsulting.coreproject.core.BaseActivity;
import com.cerezaconsulting.coreproject.data.events.MessageChapterCompleteEvent;
import com.cerezaconsulting.coreproject.data.model.ChapterEntity;
import com.cerezaconsulting.coreproject.data.model.CourseEntity;
import com.cerezaconsulting.coreproject.presentation.activities.ChapterActivity;
import com.cerezaconsulting.coreproject.presentation.activities.FragmentsActivity;


import java.util.ArrayList;


public class CardFragment extends Fragment {

    private CardView cardView;



    public static Fragment getInstance(CourseEntity courseEntity, ChapterEntity chapterEntity, ArrayList<ChapterEntity> chapterEntities) {

        CardFragment f = new CardFragment();
        Bundle args = new Bundle();
        args.putSerializable("position", chapterEntity);
        args.putSerializable("chapters", chapterEntities);
        args.putSerializable("course", courseEntity);

        f.setArguments(args);

        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_chapter, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        TextView description = (TextView) view.findViewById(R.id.tv_detail);
        ImageView imageView = (ImageView) view.findViewById(R.id.tv_status_chapter);


        Button button = (Button) view.findViewById(R.id.button);

        final ChapterEntity chapterEntity = (ChapterEntity) getArguments().getSerializable("position");
        final ArrayList<ChapterEntity> chapterEntities = (ArrayList<ChapterEntity>) getArguments().getSerializable("chapters");
        final CourseEntity courseEntity = (CourseEntity) getArguments().getSerializable("course");

        imageView.setVisibility(chapterEntity.isFinished() ? View.VISIBLE : View.INVISIBLE);

        title.setText(chapterEntity.getName());
        description.setText("");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("chapter", chapterEntity);
                bundle.putSerializable("chapters", chapterEntities);
                bundle.putSerializable("course", courseEntity);
                Intent intent = new Intent(getActivity(), FragmentsActivity.class);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                startActivityForResult(intent, 200);

            }
        });

        return view;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 200) {
                CourseEntity courseEntity = (CourseEntity) data.getSerializableExtra("course");
                ChapterEntity chapterEntity = (ChapterEntity) data.getSerializableExtra("chapter");

                openNextChapter(courseEntity, chapterEntity);
             }

        }
    }

    private void openNextChapter(CourseEntity courseEntity, ChapterEntity chapterEntity) {

        for (int i = 0; i < courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size(); i++) {

            if (courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(i).getId().
                    equals(chapterEntity.getId())) {

                if (i == courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size() - 1) {
                    for (int j = 0; j < i; j++) {
                        if (!courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(j).isFinished()) {
                            openFragmentActivity(courseEntity, courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(j));
                            return;
                        }
                    }
                } else {
                    for (int j = i + 1; j < courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().size(); j++) {
                        if (!courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(j).isFinished()) {
                            openFragmentActivity(courseEntity, courseEntity.getTrainingEntity().getRelease().getCourse().getChapters().get(j));
                            return;
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
        startActivityForResult(intent, 200);
    }
}