package com.weqa.model;

/**
 * Created by Manish Ballav on 8/17/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Availability {

    @SerializedName("buildingId")
    @Expose
    private Integer buildingId;
    @SerializedName("itemType")
    @Expose
    private String itemType;
    @SerializedName("itemCount")
    @Expose
    private Integer itemCount;
    @SerializedName("itemTypeId")
    @Expose
    private Integer itemTypeId;

    /**
     * No args constructor for use in serialization
     *
     */
    public Availability() {
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }
}

