package com.weqa.model;

/**
 * Created by Manish Ballav on 8/22/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FloorplanInput {

    @SerializedName("ActionCode")
    @Expose
    private String actionCode;
    @SerializedName("ItemTypeId")
    @Expose
    private Integer itemTypeId;
    @SerializedName("FloorPlan")
    @Expose
    private List<FloorPlan> floorPlan = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public FloorplanInput() {
    }

    /**
     *
     * @param actionCode
     * @param floorPlan
     * @param itemTypeId
     */
    public FloorplanInput(String actionCode, Integer itemTypeId, List<FloorPlan> floorPlan) {
        super();
        this.actionCode = actionCode;
        this.itemTypeId = itemTypeId;
        this.floorPlan = floorPlan;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public List<FloorPlan> getFloorPlan() {
        return floorPlan;
    }

    public void setFloorPlan(List<FloorPlan> floorPlan) {
        this.floorPlan = floorPlan;
    }

}