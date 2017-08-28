package com.weqa.model;

/**
 * Created by Manish Ballav on 8/22/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloorPlanDetail {

    @SerializedName("floorPlanId")
    @Expose
    private Integer floorPlanId;
    @SerializedName("itemCount")
    @Expose
    private Integer itemCount;
    @SerializedName("floorImage")
    @Expose
    private String floorImage;
    @SerializedName("imageLocation")
    @Expose
    private String imageLocation;

    /**
     * No args constructor for use in serialization
     *
     */
    public FloorPlanDetail() {
    }

    /**
     *
     * @param floorPlanId
     * @param imageLocation
     * @param itemCount
     * @param floorImage
     */
    public FloorPlanDetail(Integer floorPlanId, Integer itemCount, String floorImage, String imageLocation) {
        super();
        this.floorPlanId = floorPlanId;
        this.itemCount = itemCount;
        this.floorImage = floorImage;
        this.imageLocation = imageLocation;
    }

    public Integer getFloorPlanId() {
        return floorPlanId;
    }

    public void setFloorPlanId(Integer floorPlanId) {
        this.floorPlanId = floorPlanId;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public String getFloorImage() {
        return floorImage;
    }

    public void setFloorImage(String floorImage) {
        this.floorImage = floorImage;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

}
