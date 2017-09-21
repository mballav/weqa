package com.weqa.model;

/**
 * Created by Manish Ballav on 9/3/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Org {

    @SerializedName("organizationName")
    @Expose
    private String organizationName;
    @SerializedName("organizationId")
    @Expose
    private Integer organizationId;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("designation")
    @Expose
    private String designation;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}

