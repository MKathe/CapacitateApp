package com.cerezaconsulting.compendio.utils;


import android.app.Activity;

import com.cerezaconsulting.compendio.data.model.ActivityEntity;
import com.cerezaconsulting.compendio.data.model.ChapterEntity;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.data.model.CoursesEntity;
import com.cerezaconsulting.compendio.data.model.FragmentEntity;
import com.cerezaconsulting.compendio.data.model.OptionEntity;
import com.cerezaconsulting.compendio.data.model.QuestionEntity;
import com.cerezaconsulting.compendio.data.model.TrainingEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by junior on 28/03/17.
 */

public class CompendioUtils {

    public static ArrayList<QuestionEntity> getQuestions(ChapterEntity chapter, ArrayList<FragmentEntity> fragmentEntities) {
        ArrayList<QuestionEntity> questions = new ArrayList<>();

        QuestionEntity question = null;
        for (FragmentEntity fragment : chapter.getFragments()) {
            question = getQuestion(fragment, fragmentEntities);

            if (question.getDetail() != null && !question.getDetail().equals("")) {
                questions.add(question);
            }
        }

        Collections.shuffle(questions, new Random());

        return questions;
    }

    public static ArrayList<QuestionEntity> getQuestions(TrainingEntity training) {
        ArrayList<QuestionEntity> questions = new ArrayList<>();
        ActivityEntity last_review_activity = findLastReviewActivity(training);

        if (last_review_activity != null) {
            ArrayList<FragmentEntity> total_fragments = new ArrayList<>();

            String[] split = last_review_activity.getPoorly().split(",");

            ArrayList<String> ids = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                if (!split[i].equals("") && !split[i].isEmpty() && split[i] != null) {
                    ids.add(split[i]);
                }
            }

            //Get fragment poorly
            ArrayList<FragmentEntity> incorrects_fragments = findFragmentsByIdInCourse(training.getRelease().getCourse(), ids);
            Collections.shuffle(incorrects_fragments, new Random());


            incorrects_fragments = new ArrayList<FragmentEntity>(incorrects_fragments.subList(0, (int) Math.ceil((double) incorrects_fragments.size() / 2.0)));


            total_fragments.addAll(incorrects_fragments);

            //Get fragment good
            ArrayList<FragmentEntity> corrects_fragments = findFragmentsNotIdInCourse(training.getRelease().getCourse(), ids);
            Collections.shuffle(corrects_fragments, new Random());
            corrects_fragments = new ArrayList<FragmentEntity>(corrects_fragments.subList(0, (int) Math.ceil((double) corrects_fragments.size() / 2.0)));
            total_fragments.addAll(corrects_fragments);

            Collections.shuffle(total_fragments, new Random());

            for (FragmentEntity fragment : total_fragments) {
                QuestionEntity question = getQuestion(fragment, training);
                if (question.getDetail() != null && !question.getDetail().equals("")) {
                    questions.add(question);
                }
            }
        } else {
            ArrayList<ActivityEntity> activities = findChallengeActivities(training);
            ArrayList<FragmentEntity> total_fragments = new ArrayList<>();

            //Get all poorly in training
            String poorly = activities.get(0).getPoorly();
            for (int i = 1; i < activities.size(); i++) {
                poorly += "," + activities.get(i).getPoorly();
            }

            String[] split = poorly.split(",");
            ArrayList<String> ids = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                if (!split[i].equals("") && !split[i].isEmpty() && split[i] != null) {
                    ids.add(split[i]);
                }
            }

            //Get fragment poorly
            ArrayList<FragmentEntity> incorrects_fragments = findFragmentsByIdInCourse(training.getRelease().getCourse(), ids);
            Collections.shuffle(incorrects_fragments, new Random());
            incorrects_fragments = new ArrayList<FragmentEntity>(incorrects_fragments.subList(0, (int) Math.ceil((double) incorrects_fragments.size() / 2.0)));
            total_fragments.addAll(incorrects_fragments);

            //Get fragment good
            ArrayList<FragmentEntity> corrects_fragments = findFragmentsNotIdInCourse(training.getRelease().getCourse(), ids);
            Collections.shuffle(corrects_fragments, new Random());
            corrects_fragments = new ArrayList<FragmentEntity>(corrects_fragments.subList(0, (int) Math.ceil((double) corrects_fragments.size() / 2.0)));
            total_fragments.addAll(corrects_fragments);

            Collections.shuffle(total_fragments, new Random());

            for (FragmentEntity fragment : total_fragments) {
                QuestionEntity question = getQuestion(fragment, training);
                if (question.getDetail() != null && !question.getDetail().equals("")) {
                    questions.add(question);
                }
            }
        }

        return questions;
    }

    public static QuestionEntity getQuestion(FragmentEntity fragment, TrainingEntity trainingEntity) {
        QuestionEntity question = new QuestionEntity();

        Document doc = Jsoup.parse(fragment.getContent());
        Elements bolds = doc.getElementsByTag("b");

        if (!bolds.isEmpty()) {
            question.setFragment(fragment.getId());
            question.setDetail("");

            for (Element bold : bolds) {
                if (!bold.text().equals(" ") && !bold.text().equals("") && bold.text() != null) {
                    question.setDetail(question.getDetail() + bold.text() + "...");
                }
            }

            question.setOptions(getOptions(fragment, trainingEntity));
        }

        return question;
    }

    public static ArrayList<OptionEntity> getOptions(FragmentEntity fragment, TrainingEntity trainingEntity) {
        ArrayList<OptionEntity> options = new ArrayList<>();

        OptionEntity option = new OptionEntity();
        option.setDetail(fragment.getTitle());
        option.setCorrect(true);
        options.add(option);

        ArrayList<FragmentEntity> fragment_incorrects = findFragmentsExcludenFromFragment(fragment, trainingEntity);
        Collections.shuffle(fragment_incorrects, new Random());

        int total_option = fragment_incorrects.size() > 4 ? 4 : fragment_incorrects.size();

        for (int i = 0; i < total_option; i++) {
            option = new OptionEntity();
            option.setDetail(fragment_incorrects.get(i).getTitle());
            option.setCorrect(false);
            options.add(option);
        }

        Collections.shuffle(options, new Random());

        return options;
    }

    private static ArrayList<FragmentEntity>
    findFragmentsExcludenFromFragment(FragmentEntity fragmentEntity, TrainingEntity trainingEntity) {
        ArrayList<FragmentEntity> fragmentEntities = new ArrayList<>();
        for (int i = 0; i < trainingEntity.getRelease().getCourse().getChapters().size(); i++) {
            for (int j = 0; j < trainingEntity.getRelease().getCourse().getChapters().get(i).getFragments().size(); j++) {
                if (!trainingEntity.getRelease().getCourse().getChapters().get(i).getFragments().get(j).getId()
                        .equals(fragmentEntity.getId())) {
                    fragmentEntities.add(trainingEntity.getRelease().getCourse().getChapters().get(i).getFragments().get(j));
                }
            }

        }

        return fragmentEntities;

    }


    private static ArrayList<ActivityEntity> findChallengeActivities(TrainingEntity training) {
        ArrayList<ActivityEntity> activityEntities = new ArrayList<>();

        for (int i = 0; i < training.getActivityEntities().size(); i++) {
            if (!training.getActivityEntities().get(i).getIdChapter().equals("")) {
                activityEntities.add(training.getActivityEntities().get(i));
            }
        }

        return activityEntities;
    }

    private static ArrayList<FragmentEntity> findFragmentsNotIdInCourse(CourseEntity course, ArrayList<String> ids) {
        ArrayList<FragmentEntity> fragmentEntities = new ArrayList<>();
        for (int i = 0; i < course.getChapters().size(); i++) {
            for (int j = 0; j < course.getChapters().get(i).getFragments().size(); j++) {
                for (int k = 0; k < ids.size(); k++) {
                    if (!ids.get(k).equals(course.getChapters().get(i).getFragments().get(j).getId())) {
                        fragmentEntities.add(course.getChapters().get(i).getFragments().get(j));
                        break;
                    }
                }
            }
        }


        return fragmentEntities;
    }

    private static ArrayList<FragmentEntity> findFragmentsByIdInCourse(CourseEntity course, ArrayList<String> ids) {

        ArrayList<FragmentEntity> fragmentEntities = new ArrayList<>();
        for (int i = 0; i < course.getChapters().size(); i++) {
            for (int j = 0; j < course.getChapters().get(i).getFragments().size(); j++) {
                for (int k = 0; k < ids.size(); k++) {
                    if (ids.get(k).equals(course.getChapters().get(i).getFragments().get(j).getId())) {
                        fragmentEntities.add(course.getChapters().get(i).getFragments().get(j));
                        break;
                    }
                }
            }
        }


        return fragmentEntities;
    }

    private static ActivityEntity findLastReviewActivity(TrainingEntity training) {

        return training.getActivityEntities().get(training.getActivityEntities().size() - 1);
    }

    private static QuestionEntity getQuestion(FragmentEntity fragment, ArrayList<FragmentEntity> fragmentEntitiesTotal) {
        QuestionEntity question = new QuestionEntity();

        Document doc = Jsoup.parse(fragment.getContent());
        Elements bolds = doc.getElementsByTag("b");

        if (!bolds.isEmpty()) {
            question.setFragment(fragment.getId());
            question.setDetail("");

            for (Element bold : bolds) {
                if (!bold.text().equals(" ") && !bold.text().equals("") && bold.text() != null) {
                    question.setDetail(question.getDetail() + bold.text() + "...");
                }
            }

            question.setOptions(getOptions(fragment, fragmentEntitiesTotal));
        }

        return question;
    }

    private static ArrayList<OptionEntity> getOptions(FragmentEntity fragment, ArrayList<FragmentEntity> fragmentEntitiesTotal) {
        ArrayList<OptionEntity> options = new ArrayList<>();

        OptionEntity option = new OptionEntity();
        option.setDetail(fragment.getTitle());
        option.setCorrect(true);
        options.add(option);

        ArrayList<FragmentEntity> fragment_incorrects = findIncorrectOptions(fragmentEntitiesTotal,
                fragment.getTitle());
        Collections.shuffle(fragment_incorrects, new Random());

        int total_option = fragment_incorrects.size() > 4 ? 4 : fragment_incorrects.size();

        for (int i = 0; i < total_option; i++) {
            option = new OptionEntity();
            option.setDetail(fragment_incorrects.get(i).getTitle());
            option.setCorrect(false);
            options.add(option);
        }

        Collections.shuffle(options, new Random());

        return options;
    }

    private static ArrayList<FragmentEntity> findIncorrectOptionsandNotInclude(ArrayList<FragmentEntity> fragmentEntities,
                                                                  String title) {


        ArrayList<FragmentEntity> fragmentEntitiesTemp = new ArrayList<>();
        for (FragmentEntity fragment : fragmentEntities) {
            if (!fragmentEntities.equals(title))
                fragmentEntitiesTemp.add(fragment);
        }


        return fragmentEntitiesTemp;
    }

    private static ArrayList<FragmentEntity> findIncorrectOptions(ArrayList<FragmentEntity> fragmentEntities,
                                                                  String title) {


        ArrayList<FragmentEntity> fragmentEntitiesTemp = new ArrayList<>();
      /*  for (FragmentEntity fragment : fragmentEntities) {
            if (!fragment.equals(title)) {
                fragmentEntitiesTemp.add(fragment);
            }

        }*/

        for (int i = 0; i < fragmentEntities.size(); i++) {

            if(!fragmentEntities.get(i).getTitle().equals(title)){
                fragmentEntitiesTemp.add(fragmentEntities.get(i));
            }
        }

        return fragmentEntitiesTemp;
    }

    public static Double calculateIntellect(ArrayList<ActivityEntity> activities) {
        double result = 0;
        double total_intellect = 0;
        double total_question = 0;

        if (activities != null && !activities.isEmpty()) {
            for (ActivityEntity activity : activities) {
                total_intellect += activity.getIntellect() * (activity.getCorrect() + activity.getIncorrect());
                total_question += activity.getCorrect() + activity.getIncorrect();
            }

            result = total_intellect / total_question;
        }

        return result;
    }

    public static Double calculateProgress(TrainingEntity training) {
        Double progress;

        Double total_activities = (double) training.getActivityEntities().size();
        Double total_chapter = (double) training.getRelease().getCourse().getChapters().size();

        if (total_activities == 0) {
            progress = 0.0;
        } else if (total_activities >= total_chapter) {
            progress = 100.0;
        } else {
            progress = (total_activities / total_chapter) * 100.0;
        }

        return progress;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static Date getReviewDate(Double totalIntellect) {
        double base = 271828182845904.0;
        double time = 1;
        double s = totalIntellect;
        double x = -1 * (time / s);
        double fx = Math.pow(base, x); //retención

        while (fx >= 0.5) {
            time++;
            x = -1 * (time / s);
            fx = Math.pow(base, x);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, (int) time);

        return calendar.getTime();
    }

    public static Double getTotalIntellect(TrainingEntity training) {
        double total_intellect_challenge = getIntellectChallengeActivity(training);
        double total_intellect_review = 0;

        ArrayList<ActivityEntity> activities = findReviewActivities(training);

        if (activities != null) {
            for (ActivityEntity activity : activities) {
                total_intellect_review += activity.getIntellect();
            }
        }

        return total_intellect_challenge + total_intellect_review;
    }

    private static Double getIntellectChallengeActivity(TrainingEntity training) {
        ArrayList<ActivityEntity> activities = new ArrayList<>();
        for (int i = 0; i < training.getActivityEntities().size(); i++) {
            if (!training.getActivityEntities().get(i).getIdChapter().isEmpty()) {
                activities.add(training.getActivityEntities().get(i));
            }

        }
        return calculateIntellect(activities);
    }

    private static ArrayList<ActivityEntity> findReviewActivities(TrainingEntity training) {
        ArrayList<ActivityEntity> activities = new ArrayList<>();
        for (int i = 0; i < training.getActivityEntities().size(); i++) {
            if (training.getActivityEntities().get(i).getIdChapter().isEmpty()) {
                activities.add(training.getActivityEntities().get(i));
            }

        }
        return activities;
    }
}
