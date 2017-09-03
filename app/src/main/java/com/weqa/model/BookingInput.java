package com.weqa.model;

/**
 * Created by Manish Ballav on 9/2/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingInput {

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
    public BookingInput() {
    }

    /**
     *
     * @param qrCode
     * @param uuid
     */
    public BookingInput(String uuid, String qrCode) {
        super();
        this.uuid = uuid;
        this.qrCode = qrCode;
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
