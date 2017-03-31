package com.cerezaconsulting.coreproject.utils;


import com.cerezaconsulting.coreproject.data.model.ActivityEntity;
import com.cerezaconsulting.coreproject.data.model.ChapterEntity;
import com.cerezaconsulting.coreproject.data.model.FragmentEntity;
import com.cerezaconsulting.coreproject.data.model.OptionEntity;
import com.cerezaconsulting.coreproject.data.model.QuestionEntity;
import com.cerezaconsulting.coreproject.data.model.TrainingEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
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
/*
    public ArrayList<QuestionEntity> getQuestions(TrainingEntity training) {
        ArrayList<QuestionEntity> questions = new ArrayList<>();
        Activity last_review_activity = trainingActivityService.findLastReviewActivity(training);

        if (last_review_activity != null) {
            List<Fragment> total_fragments = new ArrayList<>();

            String[] split = last_review_activity.getPoorly().split(",");

            ArrayList<Long> ids = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                if (!split[i].equals("") && !split[i].isEmpty() && split[i] != null) {
                    ids.add(Long.parseLong(split[i]));
                }
            }

            //Get fragment poorly
            ArrayList<FragmentEntity> incorrects_fragments = trainingFragmentService.findByIdIn(training.getRelease().getCourse(), ids);
            Collections.shuffle(incorrects_fragments, new Random());


            incorrects_fragments = incorrects_fragments.subList(0, (int) Math.ceil((double) incorrects_fragments.size() / 2.0));
            total_fragments.addAll(incorrects_fragments);

            //Get fragment good
            List<Fragment> corrects_fragments = trainingFragmentService.findByIdNotIn(training.getRelease().getCourse(), ids);
            Collections.shuffle(corrects_fragments, new Random());
            corrects_fragments = corrects_fragments.subList(0, (int) Math.ceil((double) corrects_fragments.size() / 2.0));
            total_fragments.addAll(corrects_fragments);

            Collections.shuffle(total_fragments, new Random());

            for (Fragment fragment : total_fragments) {
                Question question = this.getQuestion(fragment);
                if (question.getDetail() != null && !question.getDetail().equals("")) {
                    questions.add(question);
                }
            }
        } else {
            List<Activity> activities = trainingActivityService.findChallengeActivities(training);
            List<Fragment> total_fragments = new ArrayList<>();

            //Get all poorly in training
            String poorly = activities.get(0).getPoorly();
            for (int i = 1; i < activities.size(); i++) {
                poorly += "," + activities.get(i).getPoorly();
            }

            String[] split = poorly.split(",");
            List<Long> ids = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                if (!split[i].equals("") && !split[i].isEmpty() && split[i] != null) {
                    ids.add(Long.parseLong(split[i]));
                }
            }

            //Get fragment poorly
            List<Fragment> incorrects_fragments = trainingFragmentService.findByIdIn(training.getRelease().getCourse(), ids);
            Collections.shuffle(incorrects_fragments, new Random());
            incorrects_fragments = incorrects_fragments.subList(0, (int) Math.ceil((double) incorrects_fragments.size() / 2.0));
            total_fragments.addAll(incorrects_fragments);

            //Get fragment good
            List<Fragment> corrects_fragments = trainingFragmentService.findByIdNotIn(training.getRelease().getCourse(), ids);
            Collections.shuffle(corrects_fragments, new Random());
            corrects_fragments = corrects_fragments.subList(0, (int) Math.ceil((double) corrects_fragments.size() / 2.0));
            total_fragments.addAll(corrects_fragments);

            Collections.shuffle(total_fragments, new Random());

            for (Fragment fragment : total_fragments) {
                Question question = this.getQuestion(fragment);
                if (question.getDetail() != null && !question.getDetail().equals("")) {
                    questions.add(question);
                }
            }
        }

        return questions;
    }*/

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

    private static ArrayList<FragmentEntity> findIncorrectOptions(ArrayList<FragmentEntity> fragmentEntities,
                                                                  String title) {


        ArrayList<FragmentEntity> fragmentEntitiesTemp = new ArrayList<>();
        for (FragmentEntity fragment : fragmentEntities) {
            if (!fragmentEntities.equals(title))
                fragmentEntitiesTemp.add(fragment);
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
}
