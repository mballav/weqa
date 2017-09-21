package com.weqa.model.json;

/**
 * Created by Manish Ballav on 9/21/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UuidList {

    @SerializedName("UUID")
    @Expose
    private String uUID;

    public String getUUID() {
        return uUID;
    }

    public void setUUID(String uUID) {
        this.uUID = uUID;
    }

}
