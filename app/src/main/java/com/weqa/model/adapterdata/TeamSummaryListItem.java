package com.weqa.model.adapterdata;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Manish Ballav on 9/20/2017.
 */

public class TeamSummaryListItem {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

    private int orgId;

    private boolean isOrg;

    private String orgName;

    private String teamName;

    private int numberOfMembers;

    private Date createdDate;

    private int colocated;

    private int distributed;

    private int notFound;

    private int teamId;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(int numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }

    public String getFormattedDate() {
        return dateFormat.format(createdDate);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getColocated() {
        return colocated;
    }

    public void setColocated(int colocated) {
        this.colocated = colocated;
    }

    public int getDistributed() {
        return distributed;
    }

    public void setDistributed(int distributed) {
        this.distributed = distributed;
    }

    public int getNotFound() {
        return notFound;
    }

    public void setNotFound(int notFound) {
        this.notFound = notFound;
    }

    public boolean isOrg() {
        return isOrg;
    }

    public void setOrg(boolean org) {
        isOrg = org;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }
}
