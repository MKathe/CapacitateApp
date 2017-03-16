package com.cerezaconsulting.coreproject.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerezaconsulting.coreproject.R;
import com.cerezaconsulting.coreproject.core.BaseActivity;
import com.cerezaconsulting.coreproject.core.BaseFragment;
import com.cerezaconsulting.coreproject.core.ScrollChildSwipeRefreshLayout;
import com.cerezaconsulting.coreproject.data.model.ChapterEntity;
import com.cerezaconsulting.coreproject.data.model.CourseEntity;
import com.cerezaconsulting.coreproject.presentation.adapters.ChapterAdapter;
import com.cerezaconsulting.coreproject.presentation.contracts.ChapterContract;
import com.cerezaconsulting.coreproject.presentation.presenters.communicator.CommunicatorChapterItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 16/03/17.
 */

public class ChapterFragment extends BaseFragment implements ChapterContract.View{

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
    private CourseEntity courseEntity;
    private ChapterContract.Presenter presenter;
    private LinearLayoutManager layoutManager;
    private ChapterAdapter adapter;

    public static ChapterFragment newInstance(Bundle bundle) {
        ChapterFragment fragment = new ChapterFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseEntity = (CourseEntity) getArguments().getSerializable("course");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chapter, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItems.setLayoutManager(layoutManager);

        adapter = new ChapterAdapter(new ArrayList<ChapterEntity>(),(CommunicatorChapterItem) presenter);
        rvItems.setAdapter(adapter);
        presenter.getChapter(courseEntity.getId());
    }

    @Override
    public void getChapter(ArrayList<ChapterEntity> list) {
        adapter.setItems(list);
        if(list.size()!=0){
            llNoItems.setVisibility(View.GONE);
        }
    }

    @Override
    public void detailChapter(ChapterEntity chapterEntity) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("chapter",chapterEntity);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(ChapterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });

    }

    @Override
    public void showMessage(String msg) {
        ((BaseActivity) getActivity()).showMessage(msg);
    }

    @Override
    public void showErrorMessage(String message) {
        ((BaseActivity) getActivity()).showMessageError(message);
    }
}
