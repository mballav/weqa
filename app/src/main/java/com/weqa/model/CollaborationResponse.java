package com.weqa.model;

/**
 * Created by Manish Ballav on 9/20/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class CollaborationResponse {

    @SerializedName("orgId")
    @Expose
    private Integer orgId;
    @SerializedName("orgName")
    @Expose
    private String orgName;
    @SerializedName("totalMember")
    @Expose
    private Integer totalMember;
    @SerializedName("teamId")
    @Expose
    private Integer teamId;
    @SerializedName("teamName")
    @Expose
    private String teamName;
    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    @SerializedName("coLocated")
    @Expose
    private Integer coLocated;
    @SerializedName("distributed")
    @Expose
    private Integer distributed;
    @SerializedName("notFound")
    @Expose
    private Integer notFound;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getCoLocated() {
        return coLocated;
    }

    public void setCoLocated(Integer coLocated) {
        this.coLocated = coLocated;
    }

    public Integer getDistributed() {
        return distributed;
    }

    public void setDistributed(Integer distributed) {
        this.distributed = distributed;
    }

    public Integer getNotFound() {
        return notFound;
    }

    public void setNotFound(Integer notFound) {
        this.notFound = notFound;
    }

    public static Comparator<CollaborationResponse> OrgNameComparator
            = new Comparator<CollaborationResponse>() {

        public int compare(CollaborationResponse a1, CollaborationResponse a2) {

            String n1 = a1.getOrgName().toUpperCase();
            String n2 = a2.getOrgName().toUpperCase();

            //ascending order
            return n1.compareTo(n2);
        }
    };
}
