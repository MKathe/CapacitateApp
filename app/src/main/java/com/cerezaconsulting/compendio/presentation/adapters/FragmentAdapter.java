package com.cerezaconsulting.compendio.presentation.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.data.model.FragmentEntity;
import com.cerezaconsulting.compendio.presentation.presenters.communicator.CommunicatorChapterItem;
import com.cerezaconsulting.compendio.utils.ProgressDialogCustom;

import java.util.ArrayList;

/**
 * Created by miguel on 16/03/17.
 */

public class FragmentAdapter extends RecyclerView.Adapter<FragmentAdapter.ViewHolder> {


    private ArrayList<FragmentEntity> list;
    private CommunicatorChapterItem communicatorChapterItem;
    private Context context;
    private boolean isFinished;
    private ProgressDialogCustom progressDialogCustom;
    private boolean firstLoad = false;

    public FragmentAdapter(boolean finished, ArrayList<FragmentEntity> list, Context context,
                           CommunicatorChapterItem communicatorChapterItem) {
        this.list = list;
        this.context = context;
        this.communicatorChapterItem = communicatorChapterItem;
        this.isFinished = finished;
        progressDialogCustom = new ProgressDialogCustom(context, "Cargando...");
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
            return new ViewHolder(itemView, 0);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        if (position == list.size()) {

            if (isFinished) {
                holder.itemView.setVisibility(View.GONE);
            } else {


                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
                @ColorInt int color = typedValue.data;

                holder.nextButton.setBackgroundColor(color);
                holder.nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        communicatorChapterItem.openQuestions();
                    }
                });
            }


        } else {
            FragmentEntity fragmentEntity = list.get(position);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";

            if (Build.VERSION.SDK_INT >= 19) {
                holder.fragmentContent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                holder.fragmentContent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            holder.fragmentContent.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {

                    progressDialogCustom.setProgress(progress);
                    if (position == 0) {
                        if (progress == 100) {
                            if (progressDialogCustom.isShowing()) {
                                progressDialogCustom.dismiss();
                            }


                        } else {

                            if (!firstLoad) {
                                firstLoad = true;
                                progressDialogCustom.show();
                            }

                        }
                    }


                }
            });

            String html = fragmentEntity.getContent();
            holder.fragmentContent.getSettings().setJavaScriptEnabled(true);
            holder.fragmentContent.loadData(html, "text/html", "utf-8");
            holder.titleFragment.setText(fragmentEntity.getTitle());
        }


    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
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

        ViewHolder(View itemView, int off) {
            super(itemView);
            nextButton = (Button) itemView.findViewById(R.id.next_button);
        }


    }

}
