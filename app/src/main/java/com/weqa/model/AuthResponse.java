package com.weqa.model;

/**
 * Created by Manish Ballav on 8/10/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthResponse {

    @SerializedName("authenticationCode")
    @Expose
    private String authenticationCode;
    @SerializedName("authorizationCode")
    @Expose
    private String authorizationCode;
    @SerializedName("configurationCode")
    @Expose
    private String configurationCode;
    @SerializedName("authentication")
    @Expose
    private Authentication authentication;
    @SerializedName("authorization")
    @Expose
    private List<Authorization> authorization = null;
    @SerializedName("configuration")
    @Expose
    private Configuration configuration;
    @SerializedName("activationCodes")
    @Expose
    private List<ActivationCode> activationCodes = null;

    public String getAuthenticationCode() {
        return authenticationCode;
    }

    public void setAuthenticationCode(String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getConfigurationCode() {
        return configurationCode;
    }

    public void setConfigurationCode(String configurationCode) {
        this.configurationCode = configurationCode;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public List<Authorization> getAuthorization() {
        return authorization;
    }

    public void setAuthorization(List<Authorization> authorization) {
        this.authorization = authorization;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public List<ActivationCode> getActivationCodes() {
        return activationCodes;
    }

    public void setActivationCodes(List<ActivationCode> activationCodes) {
        this.activationCodes = activationCodes;
    }
}
