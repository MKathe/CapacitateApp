package com.cerezaconsulting.coreproject.data.model;

import java.io.Serializable;

/**
 * Created by junior on 13/03/17.
 */

public class RoleEntity implements Serializable {


    private String name;
    private String id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
