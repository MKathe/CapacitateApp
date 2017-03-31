package com.cerezaconsulting.compendio.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.cerezaconsulting.compendio.data.model.AccessTokenEntity;
import com.cerezaconsulting.compendio.data.model.ActivitiesGroupEntity;
import com.cerezaconsulting.compendio.data.model.CoursesEntity;
import com.cerezaconsulting.compendio.data.model.UserEntity;
import com.google.gson.Gson;

/**
 * Created by junior on 13/10/16.
 */
public class SessionManager {


    public static final String PREFERENCE_NAME = "SymbiosisClient";
    public static int PRIVATE_MODE = 0;

    /**
     * USUARIO DATA SESSION - JSON
     */
    public static final String USER_TOKEN = "user_code";
    public static final String USER_JSON = "user_json";
    public static final String IS_LOGIN = "user_login";
    public static final String EXPLANATION_SCHEDULE = "schedule_explanation";
    public static final String COURSES_WORKING = "courses";
    public static final String ACTIVTIES_CHALLENGE = "activities_challengue";
    public static final String ACTIVITIES_REVIEW = "activities_review";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public boolean isLogin() {
        return preferences.getBoolean(IS_LOGIN, false);
    }


    public void openSession(AccessTokenEntity token) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_TOKEN, token.getAccessToken());
        editor.commit();
    }


    public boolean isExplanationSchedule() {
        return preferences.getBoolean(EXPLANATION_SCHEDULE, false);
    }

    public void setExplanationSchedle(boolean active) {
        editor.putBoolean(EXPLANATION_SCHEDULE, active);
        editor.commit();
    }


    public void closeSession() {
        editor.putBoolean(IS_LOGIN, false);
        editor.putString(USER_TOKEN, null);
        editor.putString(USER_JSON, null);
        editor.putString(COURSES_WORKING, null);
        editor.commit();
    }


    public String getUserToken() {
        if (isLogin()) {
            return preferences.getString(USER_TOKEN, "");
        } else {
            return "";
        }
    }

    public void setUser(UserEntity userEntity) {
        if (userEntity != null) {
            Gson gson = new Gson();
            String user = gson.toJson(userEntity);
            editor.putString(USER_JSON, user);
        }
        editor.commit();
    }

    public UserEntity getUserEntity() {
        String userData = preferences.getString(USER_JSON, null);
        return new Gson().fromJson(userData, UserEntity.class);
    }


    public void setCourses(CoursesEntity courseEntities) {
        if (courseEntities != null) {
            Gson gson = new Gson();
            String user = gson.toJson(courseEntities);
            editor.putString(COURSES_WORKING, user);
        }
        editor.commit();
    }

    public CoursesEntity getCoures() {
        String userData = preferences.getString(COURSES_WORKING, null);
        return new Gson().fromJson(userData, CoursesEntity.class);

    }
    public void setChallengueActivities(ActivitiesGroupEntity challengueActivities ) {
        if (challengueActivities != null) {
            Gson gson = new Gson();
            String user = gson.toJson(challengueActivities);
            editor.putString(ACTIVTIES_CHALLENGE, user);
        }
        editor.commit();
    }

    public ActivitiesGroupEntity getChallengueActivities() {
        String userData = preferences.getString(ACTIVTIES_CHALLENGE, null);
        return new Gson().fromJson(userData, ActivitiesGroupEntity.class);
    }

    public void setReviewActivities(ActivitiesGroupEntity challengueActivities ) {
        if (challengueActivities != null) {
            Gson gson = new Gson();
            String user = gson.toJson(challengueActivities);
            editor.putString(ACTIVITIES_REVIEW, user);
        }
        editor.commit();
    }

    public ActivitiesGroupEntity getReviewActivities() {
        String userData = preferences.getString(ACTIVITIES_REVIEW, null);
        return new Gson().fromJson(userData, ActivitiesGroupEntity.class);
    }

}
