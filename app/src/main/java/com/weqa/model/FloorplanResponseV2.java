package com.weqa.model;

/**
 * Created by Manish Ballav on 8/31/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloorplanResponseV2 {

    @SerializedName("actionCode")
    @Expose
    private String actionCode;
    @SerializedName("responseItemType")
    @Expose
    private List<ResponseItemType> responseItemType = null;
    @SerializedName("responseOrgBasedItemType")
    @Expose
    private List<ResponseOrgBasedItemType> responseOrgBasedItemType = null;
    @SerializedName("floorPlanDetails")
    @Expose
    private List<FloorPlanDetailV2> floorPlanDetails = null;

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public List<ResponseItemType> getResponseItemType() {
        return responseItemType;
    }

    public void setResponseItemType(List<ResponseItemType> responseItemType) {
        this.responseItemType = responseItemType;
    }

    public List<ResponseOrgBasedItemType> getResponseOrgBasedItemType() {
        return responseOrgBasedItemType;
    }

    public void setResponseOrgBasedItemType(List<ResponseOrgBasedItemType> responseOrgBasedItemType) {
        this.responseOrgBasedItemType = responseOrgBasedItemType;
    }

    public List<FloorPlanDetailV2> getFloorPlanDetails() {
        return floorPlanDetails;
    }

    public void setFloorPlanDetails(List<FloorPlanDetailV2> floorPlanDetails) {
        this.floorPlanDetails = floorPlanDetails;
    }

}
