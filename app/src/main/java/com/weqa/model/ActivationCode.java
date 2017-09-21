package com.weqa.model;

/**
 * Created by Manish Ballav on 9/18/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivationCode {

    @SerializedName("activationCode")
    @Expose
    private String activationCode;

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

}
