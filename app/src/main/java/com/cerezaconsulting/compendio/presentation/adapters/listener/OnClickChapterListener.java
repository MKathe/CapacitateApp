package com.cerezaconsulting.compendio.presentation.adapters.listener;

import com.cerezaconsulting.compendio.data.model.ChapterEntity;
import com.cerezaconsulting.compendio.data.model.CourseEntity;

import java.util.ArrayList;

/**
 * Created by junior on 30/03/17.
 */

public interface OnClickChapterListener  {

     void onClick(ChapterEntity chapterEntity, ArrayList<ChapterEntity> chapterEntities, CourseEntity courseEntity);
}
