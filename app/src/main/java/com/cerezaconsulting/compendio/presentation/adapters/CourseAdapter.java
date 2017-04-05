package com.cerezaconsulting.compendio.presentation.adapters;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.presentation.adapters.listener.OnClickListListener;
import com.cerezaconsulting.compendio.presentation.presenters.communicator.CommunicatorCourseItem;
import com.cerezaconsulting.compendio.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 15/03/17.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> implements OnClickListListener {


    private ArrayList<CourseEntity> list;
    private CommunicatorCourseItem communicatorCourseItem;

    public CourseAdapter(ArrayList<CourseEntity> list, CommunicatorCourseItem communicatorCourseItem) {
        this.list = list;
        this.communicatorCourseItem = communicatorCourseItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new ViewHolder(root, this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CourseEntity courseEntity = list.get(position);
        holder.tvTitle.setText(courseEntity.getRelease().getCourse());

        Date dateStart = new Date(courseEntity.getRelease().getStart());
        Date dateEnd = new Date(courseEntity.getRelease().getEnd());
        holder.tvSubtitle.setText(DateUtils.getFormant(dateStart) + " - " + DateUtils.getFormant(dateEnd));

        // holder.tvSubtitle.setText(courseEntity.getDescription());

        if (courseEntity.isOffLineDisposed()) {
            holder.onlineDisposed.setBackgroundResource(R.drawable.offline_verde);
        } else {
            holder.onlineDisposed.setBackgroundResource(R.drawable.offline_gris);
        }

        if (courseEntity.getTrainingEntity() != null) {

         /*   if(courseEntity.getTrainingEntity().isFinished()) {*/

            if (courseEntity.getTrainingEntity().getReviewEntities() != null) {
                holder.ivCheck.setVisibility(View.VISIBLE);

                if (courseEntity.getTrainingEntity().getReviewEntities().size() > 0) {
                    if (DateUtils
                            .dateIsCurrent(courseEntity.getTrainingEntity()
                                    .getReviewEntities()
                                    .get(courseEntity.getTrainingEntity()
                                            .getReviewEntities().size() - 1).getDate())) {

                        holder.ivCheck.setBackgroundResource(R.drawable.alarma);
                    } else {


                        holder.ivCheck.setBackgroundResource(R.drawable.doble_check);
                    }
                }


            } else {


                if (courseEntity.isFinished()) {
                    holder.ivCheck.setBackgroundResource(R.drawable.doble_check);
                    holder.ivCheck.setVisibility(View.VISIBLE);
                }else{
                    holder.ivCheck.setVisibility(View.GONE);
                }
            }
            /*}else{
                holder.ivCheck.setBackgroundResource(R.drawable.doble_check);
            }*/

        } else {

            if (courseEntity.isFinished()) {
                holder.ivCheck.setBackgroundResource(R.drawable.doble_check);
                holder.ivCheck.setVisibility(View.VISIBLE);
            }else{
                holder.ivCheck.setVisibility(View.GONE);
            }

        }


        holder.ivOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.ivOptions,courseEntity);
            }
        });
    }

    private void showPopupMenu(View view, final CourseEntity courseEntity) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_course, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_send_question:

                        return true;
                    case R.id.action_download:
                        communicatorCourseItem.openDialogDoubt(courseEntity.getId());
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    public void setItems(ArrayList<CourseEntity> list) {
        this.list = list;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(int position) {
        CourseEntity courseEntity = list.get(position);
        communicatorCourseItem.onClick(courseEntity);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_subtitle)
        TextView tvSubtitle;
        @BindView(R.id.iv_check)
        ImageView ivCheck;
        @BindView(R.id.iv_options)
        ImageView ivOptions;
        @BindView(R.id.online_disposed)
        ImageView onlineDisposed;

        private OnClickListListener onClickListListener;

        ViewHolder(View itemView, OnClickListListener onClickListListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onClickListListener = onClickListListener;
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListListener.onClick(getAdapterPosition());
        }
    }
}
