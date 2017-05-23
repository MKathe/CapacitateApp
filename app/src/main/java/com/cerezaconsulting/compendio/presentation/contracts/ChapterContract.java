package com.cerezaconsulting.compendio.presentation.contracts;

import com.cerezaconsulting.compendio.core.BasePresenter;
import com.cerezaconsulting.compendio.core.BaseView;
import com.cerezaconsulting.compendio.data.model.ChapterEntity;

import java.util.ArrayList;

/**
 * Created by miguel on 16/03/17.
 */

public interface ChapterContract {
    interface View extends BaseView<Presenter>{
        void getChapter(ArrayList<ChapterEntity> list);
        void detailChapter(ChapterEntity chapterEntity);
        boolean isActive();

        void sendDoubt(String s, String idTraining);
    }
    interface Presenter extends BasePresenter{
        void getChapter(String id);

        void sendDoubt(String s, String idTraining);
    }
}
