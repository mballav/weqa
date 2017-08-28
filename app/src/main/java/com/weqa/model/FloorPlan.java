package com.weqa.model;

/**
 * Created by Manish Ballav on 8/22/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloorPlan {

    @SerializedName("FloorPlanId")
    @Expose
    private Integer floorPlanId;
    @SerializedName("ImageStatus")
    @Expose
    private Boolean imageStatus;

    /**
     * No args constructor for use in serialization
     *
     */
    public FloorPlan() {
    }

    /**
     *
     * @param imageStatus
     * @param floorPlanId
     */
    public FloorPlan(Integer floorPlanId, Boolean imageStatus) {
        super();
        this.floorPlanId = floorPlanId;
        this.imageStatus = imageStatus;
    }

    public Integer getFloorPlanId() {
        return floorPlanId;
    }

    public void setFloorPlanId(Integer floorPlanId) {
        this.floorPlanId = floorPlanId;
    }

    public Boolean getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(Boolean imageStatus) {
        this.imageStatus = imageStatus;
    }

}

