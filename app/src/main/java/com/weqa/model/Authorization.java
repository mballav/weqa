package com.weqa.model;

/**
 * Created by Manish Ballav on 8/10/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Authorization {

    public static final String BUILDING_INFO = "B";
    public static final String AUTH_TIME = "OT";

    @SerializedName("buildingId")
    @Expose
    private String buildingId;
    @SerializedName("buildingName")
    @Expose
    private String buildingName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("floorName")
    @Expose
    private String floorName;
    @SerializedName("floorPlanId")
    @Expose
    private String floorPlanId;
    @SerializedName("floorLevel")
    @Expose
    private String floorLevel;
    @SerializedName("floorPlanDescription")
    @Expose
    private String floorPlanDescription;

    /**
     * No args constructor for use in serialization
     *
     */
    public Authorization() {
    }

    /**
     *
     * @param floorName
     * @param buildingName
     * @param address
     * @param floorPlanDescription
     * @param floorLevel
     * @param floorPlanId
     * @param longitude
     * @param latitude
     */
    public Authorization(String buildingId, String buildingName, String address, String latitude, String longitude, String floorName, String floorPlanId, String floorLevel, String floorPlanDescription) {
        super();
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.floorName = floorName;
        this.floorPlanId = floorPlanId;
        this.floorLevel = floorLevel;
        this.floorPlanDescription = floorPlanDescription;
    }

    public String getBuildingId() { return buildingId; }

    public void setBuildingId(String buildingId) { this.buildingId = buildingId; }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getFloorPlanId() {
        return floorPlanId;
    }

    public void setFloorPlanId(String floorPlanId) {
        this.floorPlanId = floorPlanId;
    }

    public String getFloorLevel() {
        return floorLevel;
    }

    public void setFloorLevel(String floorLevel) {
        this.floorLevel = floorLevel;
    }

    public String getFloorPlanDescription() {
        return floorPlanDescription;
    }

    public void setFloorPlanDescription(String floorPlanDescription) {
        this.floorPlanDescription = floorPlanDescription;
    }

    public String toString() {
        return "Building: " + buildingName + ", Floor: " + floorName + ", Lat: " + latitude + ", Lon: " + longitude;
    }

    public static Comparator<Authorization> BuildingFloorNameComparator
            = new Comparator<Authorization>() {

        public int compare(Authorization a1, Authorization a2) {

            String n1 = a1.getBuildingName().toUpperCase() + "_" + a1.getFloorName().toUpperCase();
            String n2 = a2.getBuildingName().toUpperCase() + "_" + a2.getFloorName().toUpperCase();

            //ascending order
            return n1.compareTo(n2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };
}
