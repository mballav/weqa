package com.weqa.model;

/**
 * Created by Manish Ballav on 8/10/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthInput {

    @SerializedName("Uuid")
    @Expose
    private String uuid;
    @SerializedName("AuthenticationActionCode")
    @Expose
    private String authenticationActionCode;
    @SerializedName("AuthorizationActionCode")
    @Expose
    private String authorizationActionCode;
    @SerializedName("ConfigurationActionCode")
    @Expose
    private String configurationActionCode;
    @SerializedName("ActivationCode")
    @Expose
    private String activationCode = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public AuthInput() {
    }

    /**
     *
     * @param authenticationCode
     * @param configurationCode
     * @param authorizationCode
     */
    public AuthInput(String uuid, String authenticationCode, String authorizationCode, String configurationCode,
                     String activationCode) {
        super();
        this.uuid = uuid;
        this.authenticationActionCode = authenticationCode;
        this.authorizationActionCode = authorizationCode;
        this.configurationActionCode = configurationCode;
        this.activationCode = activationCode;
    }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAuthenticationCode() {
        return authenticationActionCode;
    }

    public void setAuthenticationCode(String authenticationCode) {
        this.authenticationActionCode = authenticationCode;
    }

    public String getAuthorizationCode() {
        return authorizationActionCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationActionCode = authorizationCode;
    }

    public String getConfigurationCode() {
        return configurationActionCode;
    }

    public void setConfigurationCode(String configurationCode) {
        this.configurationActionCode = configurationCode;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}
