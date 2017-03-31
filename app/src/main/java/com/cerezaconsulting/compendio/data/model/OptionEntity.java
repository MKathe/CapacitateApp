package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;

/**
 * Created by junior on 28/03/17.
 */

public class OptionEntity implements Serializable {
    private String detail;
    private boolean correct;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
