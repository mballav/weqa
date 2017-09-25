package com.weqa.model.adapterdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Manish Ballav on 9/20/2017.
 */

public class TeamMemberListItem implements Parcelable {

    private String firstName;

    private String lastName;

    private String designation;

    private String mobile;

    private String uuid;

    private long orgId;

    private String location;

    private String floorLevel;

    public String getFloorLevel() {
        return floorLevel;
    }

    public void setFloorLevel(String floorLevel) {
        this.floorLevel = floorLevel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(designation);
        dest.writeString(mobile);
        dest.writeString(uuid);
        dest.writeLong(orgId);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TeamMemberListItem createFromParcel(Parcel in) {
            return new TeamMemberListItem(in);
        }
        public TeamMemberListItem[] newArray(int size) {
            return new TeamMemberListItem[size];
        }
    };

    public TeamMemberListItem() {
    }

    private TeamMemberListItem(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        designation = in.readString();
        mobile = in.readString();
        uuid = in.readString();
        orgId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object c) {
        if(!(c instanceof TeamMemberListItem)) {
            return false;
        }

        TeamMemberListItem that = (TeamMemberListItem) c;
        return this.uuid.equals(that.getUuid());
    }
}
