package com.cerezaconsulting.coreproject.data.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by miguel on 16/03/17.
 */

public class FragmentEntity implements Serializable {

    private String id;
    private String title;
    private String content;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
