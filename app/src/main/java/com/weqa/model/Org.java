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

    /**
     * No args constructor for use in serialization
     *
     */
    public Org() {
    }

    /**
     *
     * @param organizationName
     * @param organizationId
     */
    public Org(String organizationName, Integer organizationId) {
        super();
        this.organizationName = organizationName;
        this.organizationId = organizationId;
    }

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

}

