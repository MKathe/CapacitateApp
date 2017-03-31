package com.cerezaconsulting.compendio.data.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by junior on 30/03/17.
 */

public class ActivitiesGroupEntity  implements Serializable{

    private ArrayList<ActivityEntity> activityEntities;

    public ArrayList<ActivityEntity> getActivityEntities() {
        return activityEntities;
    }

    public void setActivityEntities(ArrayList<ActivityEntity> activityEntities) {
        this.activityEntities = activityEntities;
    }
}
