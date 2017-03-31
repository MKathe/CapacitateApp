package com.cerezaconsulting.compendio.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerezaconsulting.compendio.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 16/03/17.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private ArrayList<String> list;

    public QuestionAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
