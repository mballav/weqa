package com.weqa.model;

/**
 * Created by Manish Ballav on 8/31/2017.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloorPlanDetailV2 {

    @SerializedName("floorPlanId")
    @Expose
    private Integer floorPlanId;
    @SerializedName("floorImage")
    @Expose
    private String floorImage;
    @SerializedName("itemTypeDetail")
    @Expose
    private List<ItemTypeDetailV2> itemTypeDetail = null;

    public Integer getFloorPlanId() {
        return floorPlanId;
    }

    public void setFloorPlanId(Integer floorPlanId) {
        this.floorPlanId = floorPlanId;
    }

    public String getFloorImage() {
        return floorImage;
    }

    public void setFloorImage(String floorImage) {
        this.floorImage = floorImage;
    }

    public List<ItemTypeDetailV2> getItemTypeDetail() {
        return itemTypeDetail;
    }

    public void setItemTypeDetail(List<ItemTypeDetailV2> itemTypeDetail) {
        this.itemTypeDetail = itemTypeDetail;
    }

}
