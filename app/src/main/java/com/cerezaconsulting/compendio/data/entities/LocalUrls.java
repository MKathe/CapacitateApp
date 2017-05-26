package com.cerezaconsulting.compendio.data.entities;

import java.io.Serializable;

/**
 * Created by junior on 26/05/17.
 */

public class LocalUrls implements Serializable {

    private String url;
    private String idFragment;


    public LocalUrls(String url, String idFragment) {
        this.url = url;
        this.idFragment = idFragment;
    }

    public String getIdFragment() {
        return idFragment;
    }

    public void setIdFragment(String idFragment) {
        this.idFragment = idFragment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
