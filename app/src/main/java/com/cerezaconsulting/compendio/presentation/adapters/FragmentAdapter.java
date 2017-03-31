package com.cerezaconsulting.compendio.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.data.model.FragmentEntity;
import com.cerezaconsulting.compendio.presentation.presenters.communicator.CommunicatorChapterItem;

import java.util.ArrayList;

/**
 * Created by miguel on 16/03/17.
 */

public class FragmentAdapter extends RecyclerView.Adapter<FragmentAdapter.ViewHolder> {


    private ArrayList<FragmentEntity> list;
    private CommunicatorChapterItem communicatorChapterItem;
    private Context context;

    public FragmentAdapter(ArrayList<FragmentEntity> list, Context context,
                           CommunicatorChapterItem communicatorChapterItem) {
        this.list = list;
        this.context = context;
        this.communicatorChapterItem = communicatorChapterItem;
    }


    @Override
    public int getItemViewType(int position) {
        return (position == list.size()) ? R.layout.item_button : R.layout.item_fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment, parent, false);
        return new ViewHolder(root);

*/
        View itemView;

        if (viewType == R.layout.item_fragment) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment, parent, false);
            return new ViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
            return new ViewHolder(itemView,0);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        if (position == list.size()) {
            holder.nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    communicatorChapterItem.openQuestions();
                }
            });
        } else {
            FragmentEntity fragmentEntity = list.get(position);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";

            String html = fragmentEntity.getContent();
            holder.fragmentContent.loadDataWithBaseURL("", html, mimeType, encoding, "");
            holder.titleFragment.setText(fragmentEntity.getTitle());
        }


    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    public void setItems(ArrayList<FragmentEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleFragment;
        WebView fragmentContent;
        Button nextButton;

        ViewHolder(View itemView) {
            super(itemView);
            titleFragment = (TextView) itemView.findViewById(R.id.title_fragment);
            fragmentContent = (WebView) itemView.findViewById(R.id.fragment_content);
        }
        ViewHolder(View itemView,int off) {
            super(itemView);
            nextButton = (Button) itemView.findViewById(R.id.next_button);
        }


    }

}
