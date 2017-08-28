package com.weqa.model;

/**
 * Created by Manish Ballav on 8/17/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailabilityInput {

    @SerializedName("Building")
    @Expose
    private String building;
    @SerializedName("Uuid")
    @Expose
    private String uuid;

    /**
     * No args constructor for use in serialization
     *
     */
    public AvailabilityInput() {
    }

    /**
     *
     * @param building
     * @param uuid
     */
    public AvailabilityInput(String building, String uuid) {
        super();
        this.building = building;
        this.uuid = uuid;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
