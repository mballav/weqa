package com.weqa.model;

/**
 * Created by Manish Ballav on 8/31/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseOrgBasedItemType {

    @SerializedName("itemTypeId")
    @Expose
    private Integer itemTypeId;
    @SerializedName("itemType")
    @Expose
    private String itemType;

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

}
