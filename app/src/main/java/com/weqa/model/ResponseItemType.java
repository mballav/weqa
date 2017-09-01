package com.weqa.model;

/**
 * Created by Manish Ballav on 8/31/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseItemType {

    @SerializedName("itemTypeName")
    @Expose
    private String itemTypeName;
    @SerializedName("itemTypeId")
    @Expose
    private Integer itemTypeId;

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

}
