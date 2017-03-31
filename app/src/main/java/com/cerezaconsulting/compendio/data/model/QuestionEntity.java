package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by junior on 28/03/17.
 */

public class QuestionEntity implements Serializable {

    private String fragment;
    private String detail;
    private ArrayList<OptionEntity> options;
    private boolean contest = false;

    public boolean isContest() {
        return contest;
    }

    public void setContest(boolean contest) {
        this.contest = contest;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<OptionEntity> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<OptionEntity> options) {
        this.options = options;
    }
}
