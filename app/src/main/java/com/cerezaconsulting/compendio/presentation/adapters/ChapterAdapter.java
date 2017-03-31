package com.cerezaconsulting.compendio.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.data.model.ChapterEntity;
import com.cerezaconsulting.compendio.presentation.adapters.listener.OnClickListListener;
import com.cerezaconsulting.compendio.presentation.presenters.communicator.CommunicatorChapterItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 16/03/17.
 */

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> implements OnClickListListener {

    private ArrayList<ChapterEntity> list;
    private CommunicatorChapterItem communicatorChapterItem;

    public ChapterAdapter(ArrayList<ChapterEntity> list, CommunicatorChapterItem communicatorChapterItem) {
        this.list = list;
        this.communicatorChapterItem = communicatorChapterItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new ViewHolder(root, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChapterEntity chapterEntity = list.get(position);

        holder.tvTitle.setText(chapterEntity.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(ArrayList<ChapterEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(int position) {
        ChapterEntity chapterEntity = list.get(position);
        communicatorChapterItem.onClick(chapterEntity);
    }



    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv_check)
        ImageView ivCheck;
        private OnClickListListener onClickListListener;

        ViewHolder(View itemView, OnClickListListener onClickListListener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.onClickListListener = onClickListListener;
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListListener.onClick(getAdapterPosition());
        }
    }
}
