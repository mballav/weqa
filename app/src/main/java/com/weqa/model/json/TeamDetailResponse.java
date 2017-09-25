package com.weqa.model.json;

/**
 * Created by Manish Ballav on 9/21/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamDetailResponse {

    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("teamName")
    @Expose
    private String teamName;
    @SerializedName("orgId")
    @Expose
    private Integer orgId;
    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    @SerializedName("teamDescription")
    @Expose
    private String teamDescription;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("teamCreator")
    @Expose
    private Boolean teamCreator;
    @SerializedName("floorLevel")
    @Expose
    private String floorLevel;
    @SerializedName("buildingAddress")
    @Expose
    private String buildingAddress;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Boolean getTeamCreator() {
        return teamCreator;
    }

    public void setTeamCreator(Boolean teamCreator) {
        this.teamCreator = teamCreator;
    }

    public String getFloorLevel() {
        return floorLevel;
    }

    public void setFloorLevel(String floorLevel) {
        this.floorLevel = floorLevel;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

}
