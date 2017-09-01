package com.weqa.model;

/**
 * Created by Manish Ballav on 8/31/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemTypeDetailV2 {

    @SerializedName("itemCount")
    @Expose
    private Integer itemCount;
    @SerializedName("itemTypeId")
    @Expose
    private Integer itemTypeId;
    @SerializedName("imageLocation")
    @Expose
    private String imageLocation;

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

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

}

