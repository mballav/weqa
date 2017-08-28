package com.weqa.model;

/**
 * Created by Manish Ballav on 8/22/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FloorplanResponse {

    @SerializedName("actionCode")
    @Expose
    private String actionCode;
    @SerializedName("floorPlanDetails")
    @Expose
    private List<FloorPlanDetail> floorPlanDetails = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public FloorplanResponse() {
    }

    /**
     *
     * @param actionCode
     * @param floorPlanDetails
     */
    public FloorplanResponse(String actionCode, List<FloorPlanDetail> floorPlanDetails) {
        super();
        this.actionCode = actionCode;
        this.floorPlanDetails = floorPlanDetails;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public List<FloorPlanDetail> getFloorPlanDetails() {
        return floorPlanDetails;
    }

    public void setFloorPlanDetails(List<FloorPlanDetail> floorPlanDetails) {
        this.floorPlanDetails = floorPlanDetails;
    }

}

