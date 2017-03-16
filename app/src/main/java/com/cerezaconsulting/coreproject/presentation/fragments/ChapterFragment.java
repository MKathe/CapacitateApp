package com.cerezaconsulting.coreproject.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerezaconsulting.coreproject.R;
import com.cerezaconsulting.coreproject.core.BaseFragment;
import com.cerezaconsulting.coreproject.core.ScrollChildSwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 16/03/17.
 */

public class ChapterFragment extends BaseFragment {

    @BindView(R.id.tv_light_bulb)
    TextView tvLightBulb;
    @BindView(R.id.tv_advance)
    TextView tvAdvance;
    @BindView(R.id.tv_number_advance)
    TextView tvNumberAdvance;
    @BindView(R.id.rv_items)
    RecyclerView rvItems;
    @BindView(R.id.ll_items)
    LinearLayout llItems;
    @BindView(R.id.iv_no_items)
    ImageView ivNoItems;
    @BindView(R.id.tv_no_items)
    TextView tvNoItems;
    @BindView(R.id.ll_no_items)
    LinearLayout llNoItems;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    @BindView(R.id.refresh_layout)
    ScrollChildSwipeRefreshLayout refreshLayout;

    public static ChapterFragment newInstance(Bundle bundle) {
        ChapterFragment fragment = new ChapterFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chapter, container, false);
        ButterKnife.bind(this, root);
        return root;
    }
}
