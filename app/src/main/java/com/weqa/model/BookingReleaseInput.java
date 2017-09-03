package com.weqa.model;

/**
 * Created by Manish Ballav on 9/2/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingReleaseInput {

    @SerializedName("ActionCode")
    @Expose
    private String actionCode;
    @SerializedName("Uuid")
    @Expose
    private String uuid;
    @SerializedName("QrCode")
    @Expose
    private String qrCode;

    /**
     * No args constructor for use in serialization
     *
     */
    public BookingReleaseInput() {
    }

    /**
     *
     * @param actionCode
     * @param qrCode
     * @param uuid
     */
    public BookingReleaseInput(String actionCode, String uuid, String qrCode) {
        super();
        this.actionCode = actionCode;
        this.uuid = uuid;
        this.qrCode = qrCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

}
