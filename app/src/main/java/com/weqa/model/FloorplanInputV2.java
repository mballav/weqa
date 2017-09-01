package com.weqa.model;

/**
 * Created by Manish Ballav on 8/31/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloorplanInputV2 {

    @SerializedName("BuildingId")
    @Expose
    private String buildingId;
    @SerializedName("Uuid")
    @Expose
    private String uuid;
    @SerializedName("ActionCodeItemType")
    @Expose
    private String actionCodeItemType;
    @SerializedName("ActionCodeFloorPlan")
    @Expose
    private String actionCodeFloorPlan;
    @SerializedName("FloorPlan")
    @Expose
    private List<FloorPlan> floorPlan = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public FloorplanInputV2() {
    }

    /**
     *
     * @param actionCodeItemType
     * @param actionCodeFloorPlan
     * @param uuid
     * @param floorPlan
     * @param buildingId
     */
    public FloorplanInputV2(String buildingId, String uuid, String actionCodeItemType, String actionCodeFloorPlan, List<FloorPlan> floorPlan) {
        super();
        this.buildingId = buildingId;
        this.uuid = uuid;
        this.actionCodeItemType = actionCodeItemType;
        this.actionCodeFloorPlan = actionCodeFloorPlan;
        this.floorPlan = floorPlan;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getActionCodeItemType() {
        return actionCodeItemType;
    }

    public void setActionCodeItemType(String actionCodeItemType) {
        this.actionCodeItemType = actionCodeItemType;
    }

    public String getActionCodeFloorPlan() {
        return actionCodeFloorPlan;
    }

    public void setActionCodeFloorPlan(String actionCodeFloorPlan) {
        this.actionCodeFloorPlan = actionCodeFloorPlan;
    }

    public List<FloorPlan> getFloorPlan() {
        return floorPlan;
    }

    public void setFloorPlan(List<FloorPlan> floorPlan) {
        this.floorPlan = floorPlan;
    }

}
