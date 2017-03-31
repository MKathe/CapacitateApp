package com.cerezaconsulting.compendio.presentation.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseFragment;
import com.cerezaconsulting.compendio.data.events.MessageActivityEvent;
import com.cerezaconsulting.compendio.data.local.SessionManager;
import com.cerezaconsulting.compendio.data.model.QuestionEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QuestionPageFragment extends BaseFragment {

    QuestionEntity questionEntity;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_answer_1)
    TextView tvAnswer1;
    @BindView(R.id.iv_answer_1)
    ImageView ivAnswer1;
    @BindView(R.id.ll_answer_1)
    LinearLayout llAnswer1;
    @BindView(R.id.tv_answer_2)
    TextView tvAnswer2;
    @BindView(R.id.iv_answer_2)
    ImageView ivAnswer2;
    @BindView(R.id.ll_answer_2)
    LinearLayout llAnswer2;
    @BindView(R.id.tv_answer_3)
    TextView tvAnswer3;
    @BindView(R.id.iv_answer_3)
    ImageView ivAnswer3;
    @BindView(R.id.ll_answer_3)
    LinearLayout llAnswer3;
    @BindView(R.id.tv_answer_4)
    TextView tvAnswer4;
    @BindView(R.id.iv_answer_4)
    ImageView ivAnswer4;
    @BindView(R.id.ll_answer_4)
    LinearLayout llAnswer4;
    @BindView(R.id.tv_answer_5)
    TextView tvAnswer5;
    @BindView(R.id.iv_answer_5)
    ImageView ivAnswer5;
    @BindView(R.id.ll_answer_5)
    LinearLayout llAnswer5;


    final Handler handler = new Handler();
    private MessageActivityEvent mMessageActivityEvent;
    private SessionManager sessionManager;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            EventBus.getDefault().postSticky(mMessageActivityEvent);
        }
    };


    public static QuestionPageFragment newInstance(QuestionEntity questionEntity) {
        QuestionPageFragment fragment = new QuestionPageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", questionEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    public QuestionPageFragment() {

    }

    @Subscribe
    public void onMessageEvent(MessageActivityEvent event) {/* Do something */}

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
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(mRunnable);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        questionEntity = (QuestionEntity) arg.getSerializable("question");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_question, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvQuestion.setText(questionEntity.getDetail());

        for (int i = 0; i < questionEntity.getOptions().size(); i++) {
            switch (i) {
                case 0:
                    tvAnswer1.setText(questionEntity.getOptions().get(i).getDetail());
                    break;
                case 1:
                    tvAnswer2.setText(questionEntity.getOptions().get(i).getDetail());
                    break;
                case 2:
                    tvAnswer3.setText(questionEntity.getOptions().get(i).getDetail());
                    break;
                case 3:
                    tvAnswer4.setText(questionEntity.getOptions().get(i).getDetail());
                    break;
                case 4:
                    tvAnswer5.setText(questionEntity.getOptions().get(i).getDetail());
                    break;

            }

        }


        llAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickQuestion(ivAnswer1, questionEntity, 0);
            }
        });
        llAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickQuestion(ivAnswer2, questionEntity, 1);
            }
        });

        llAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickQuestion(ivAnswer3, questionEntity, 2);
            }
        });

        llAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickQuestion(ivAnswer4, questionEntity, 3);
            }
        });

        llAnswer5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickQuestion(ivAnswer5, questionEntity, 4);
            }
        });


    }

    private void clickQuestion(ImageView imageView, QuestionEntity questionEntity, int optionPosition) {
        if (!questionEntity.isContest()) {
            imageView.setBackgroundResource(
                    questionEntity.getOptions().get(optionPosition).isCorrect() ? R.drawable.check : R.drawable.equis);
            questionEntity.setContest(true);
            selectedCorrectQuestion();

            mMessageActivityEvent = new MessageActivityEvent(questionEntity.getOptions().get(optionPosition).isCorrect(),
                    questionEntity.getFragment());

            handler.postDelayed(mRunnable, 1000);
        }
    }


    private void selectedCorrectQuestion() {

        for (int i = 0; i < questionEntity.getOptions().size(); i++) {
            if (questionEntity.getOptions().get(i).isCorrect()) {
                switch (i) {
                    case 0:
                        ivAnswer1.setBackgroundResource(R.drawable.check);
                        break;
                    case 1:
                        ivAnswer2.setBackgroundResource(R.drawable.check);
                        break;
                    case 2:
                        ivAnswer3.setBackgroundResource(R.drawable.check);
                        break;
                    case 3:
                        ivAnswer4.setBackgroundResource(R.drawable.check);
                        break;
                    case 4:
                        ivAnswer5.setBackgroundResource(R.drawable.check);
                        break;

                }
            }
        }

    }


}
