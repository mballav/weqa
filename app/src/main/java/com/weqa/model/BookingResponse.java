package com.weqa.model;

/**
 * Created by Manish Ballav on 9/2/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingResponse {

    @SerializedName("actionCode")
    @Expose
    private String actionCode;
    @SerializedName("qtCode")
    @Expose
    private String qtCode;
    @SerializedName("releaseStatus")
    @Expose
    private Boolean releaseStatus;
    @SerializedName("bookedTime")
    @Expose
    private String bookedTime;
    @SerializedName("maxReached")
    @Expose
    private Boolean maxReached;

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getQtCode() {
        return qtCode;
    }

    public void setQtCode(String qtCode) {
        this.qtCode = qtCode;
    }

    public Boolean getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(Boolean releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(String bookedTime) {
        this.bookedTime = bookedTime;
    }

    public Boolean getMaxReached() {
        return maxReached;
    }

    public void setMaxReached(Boolean maxReached) {
        this.maxReached = maxReached;
    }

}
