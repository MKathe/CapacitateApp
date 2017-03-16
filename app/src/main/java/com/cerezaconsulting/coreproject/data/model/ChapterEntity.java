package com.cerezaconsulting.coreproject.data.model;

import java.io.Serializable;

/**
 * Created by miguel on 16/03/17.
 */

public class ChapterEntity implements Serializable {
    private String id;
    private String name;
    private String order;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if(name!=null) {
            return name;
        }
        else{
            return "";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
