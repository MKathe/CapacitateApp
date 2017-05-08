package com.cerezaconsulting.compendio.data.events;

import com.cerezaconsulting.compendio.data.model.ChapterEntity;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.data.model.CoursesEntity;

/**
 * Created by junior on 30/03/17.
 */

public class MessageChapterCompleteEvent {
    private ChapterEntity chapterEntity;
    private CoursesEntity coursesEntity;
    private CourseEntity courseEntity;
    private boolean isFinishedChapter = false;


    public boolean isFinishedChapter() {
        return isFinishedChapter;
    }

    public void setFinishedChapter(boolean finishedChapter) {
        isFinishedChapter = finishedChapter;
    }

    public MessageChapterCompleteEvent(ChapterEntity chapterEntity, CoursesEntity coursesEntity, CourseEntity courseEntity,
                                       boolean  isFinishedChapter) {
        this.chapterEntity = chapterEntity;
        this.coursesEntity = coursesEntity;
        this.courseEntity = courseEntity;
        this.isFinishedChapter = isFinishedChapter;
    }

    public MessageChapterCompleteEvent(ChapterEntity chapterEntity, CoursesEntity coursesEntity, CourseEntity courseEntity) {
        this.chapterEntity = chapterEntity;
        this.coursesEntity = coursesEntity;
        this.courseEntity = courseEntity;
    }
    public MessageChapterCompleteEvent(ChapterEntity chapterEntity, CourseEntity courseEntity) {
        this.chapterEntity = chapterEntity;
        this.courseEntity = courseEntity;
    }

    public CourseEntity getCourseEntity() {
        return courseEntity;
    }

    public void setCourseEntity(CourseEntity courseEntity) {
        this.courseEntity = courseEntity;
    }

    public ChapterEntity getChapterEntity() {
        return chapterEntity;
    }

    public void setChapterEntity(ChapterEntity chapterEntity) {
        this.chapterEntity = chapterEntity;
    }

    public CoursesEntity getCoursesEntity() {
        return coursesEntity;
    }

    public void setCoursesEntity(CoursesEntity coursesEntity) {
        this.coursesEntity = coursesEntity;
    }
}
