package com.weqa.model;

/**
 * Created by Manish Ballav on 8/10/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configuration {

    public static final String GEO_FENCE = "G";
    public static final String UPDATE_CHECK_LIMIT = "U";
    public static final String AUTH_TIME = "OT";

    @SerializedName("geoFence")
    @Expose
    private Integer geoFence;
    @SerializedName("updateCheckLimit")
    @Expose
    private Integer updateCheckLimit;

    /**
     * No args constructor for use in serialization
     *
     */
    public Configuration() {
    }

    /**
     *
     * @param geoFence
     * @param updateCheckLimit
     */
    public Configuration(Integer geoFence, Integer updateCheckLimit) {
        super();
        this.geoFence = geoFence;
        this.updateCheckLimit = updateCheckLimit;
    }

    public Integer getGeoFence() {
        return geoFence;
    }

    public void setGeoFence(Integer geoFence) {
        this.geoFence = geoFence;
    }

    public Integer getUpdateCheckLimit() {
        return updateCheckLimit;
    }

    public void setUpdateCheckLimit(Integer updateCheckLimit) {
        this.updateCheckLimit = updateCheckLimit;
    }

}
