package com.weqa.model;

/**
 * Created by Manish Ballav on 8/21/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloorplanImage {

    @SerializedName("x")
    @Expose
    private String x;

    /**
     * No args constructor for use in serialization
     *
     */
    public FloorplanImage() {
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

}
