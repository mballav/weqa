package com.weqa.model.json;

/**
 * Created by Manish Ballav on 9/21/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AddTeamMemberInput {

    @SerializedName("TeamId")
    @Expose
    private Integer teamId;
    @SerializedName("OrgId")
    @Expose
    private Integer orgId;
    @SerializedName("UuidList")
    @Expose
    private List<UuidList> uuidList = null;


    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public List<UuidList> getUuidList() {
        return uuidList;
    }

    public void setUuidList(List<UuidList> uuidList) {
        this.uuidList = uuidList;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
}
