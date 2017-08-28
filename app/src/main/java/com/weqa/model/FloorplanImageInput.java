package com.weqa.model;

/**
 * Created by Manish Ballav on 8/21/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloorplanImageInput {

    @SerializedName("FloorPlanId")
    @Expose
    private Integer floorPlanId;

    /**
     * No args constructor for use in serialization
     *
     */
    public FloorplanImageInput() {
    }

    public Integer getFloorPlanId() {
        return floorPlanId;
    }

    public void setFloorPlanId(Integer floorPlanId) {
        this.floorPlanId = floorPlanId;
    }

}
