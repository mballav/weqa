package com.weqa.model.json;

/**
 * Created by Manish Ballav on 9/21/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateTeamInput {

    @SerializedName("orgid")
    @Expose
    private Integer orgid;
    @SerializedName("TeamName")
    @Expose
    private String teamName;
    @SerializedName("TeamDescription")
    @Expose
    private String teamDescription;
    @SerializedName("CreatedByUUID")
    @Expose
    private String createdByUUID;
    @SerializedName("UuidList")
    @Expose
    private List<UuidList> uuidList = null;

    public Integer getOrgid() {
        return orgid;
    }

    public void setOrgid(Integer orgid) {
        this.orgid = orgid;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public String getCreatedByUUID() {
        return createdByUUID;
    }

    public void setCreatedByUUID(String createdByUUID) {
        this.createdByUUID = createdByUUID;
    }

    public List<UuidList> getUuidList() {
        return uuidList;
    }

    public void setUuidList(List<UuidList> uuidList) {
        this.uuidList = uuidList;
    }

}
