package com.cerezaconsulting.compendio.presentation.presenters.communicator;

import com.cerezaconsulting.compendio.data.model.ChapterEntity;

/**
 * Created by miguel on 16/03/17.
 */

public interface CommunicatorChapterItem {

    void onClick(ChapterEntity chapterEntity);
    void openQuestions();
}
