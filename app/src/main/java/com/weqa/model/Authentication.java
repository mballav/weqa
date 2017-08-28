package com.weqa.model;

/**
 * Created by Manish Ballav on 8/10/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Authentication {

    public static final String EMPLOYEE_NAME = "EN";
    public static final String EMPLOYEE_MOBILE = "EM";
    public static final String ORG_INFO = "O";
    public static final String PRIVILEGE = "P";
    public static final String AUTH_TIME = "OT";

    @SerializedName("employeeName")
    @Expose
    private String employeeName;
    @SerializedName("organization")
    @Expose
    private List<String> organization = null;
    @SerializedName("privilege")
    @Expose
    private List<String> privilege = null;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;

    /**
     * No args constructor for use in serialization
     */
    public Authentication() {
    }

    /**
     * @param organization
     * @param privilege
     * @param mobileNo
     * @param employeeName
     */
    public Authentication(String employeeName, List<String> organization, List<String> privilege, String mobileNo) {
        super();
        this.employeeName = employeeName;
        this.organization = organization;
        this.privilege = privilege;
        this.mobileNo = mobileNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public List<String> getOrganization() {
        return organization;
    }

    public void setOrganization(List<String> organization) {
        this.organization = organization;
    }

    public List<String> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<String> privilege) {
        this.privilege = privilege;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

}
