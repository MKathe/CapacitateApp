package com.cerezaconsulting.coreproject.presentation.contracts;

import com.cerezaconsulting.coreproject.core.BasePresenter;
import com.cerezaconsulting.coreproject.core.BaseView;
import com.cerezaconsulting.coreproject.data.model.ChapterEntity;

import java.util.ArrayList;

/**
 * Created by miguel on 16/03/17.
 */

public interface ChapterContract {
    interface View extends BaseView<Presenter>{
        void getChapter(ArrayList<ChapterEntity> list);
        void detailChapter(ChapterEntity chapterEntity);
        boolean isActive();

    }
    interface Presenter extends BasePresenter{
        void getChapter(String id);
    }
}
