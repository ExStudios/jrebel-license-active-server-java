package com.jrebel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JRebel验证响应结构
 */
public class JrebelValidateStruct {
    
    @JsonProperty("serverVersion")
    private String serverVersion = "3.2.4";
    
    @JsonProperty("serverProtocolVersion")
    private String serverProtocolVersion = "1.1";
    
    @JsonProperty("serverGuid")
    private String serverGuid = "a1b4aea8-b031-4302-b602-670a990272cb";
    
    @JsonProperty("groupType")
    private String groupType = "managed";
    
    @JsonProperty("statusCode")
    private String statusCode = "SUCCESS";
    
    @JsonProperty("company")
    private String company = "Administrator";
    
    @JsonProperty("canGetLease")
    private boolean canGetLease = true;
    
    @JsonProperty("licenseType")
    private int licenseType = 1;
    
    @JsonProperty("evaluationLicense")
    private boolean evaluationLicense = false;
    
    @JsonProperty("seatPoolType")
    private String seatPoolType = "standalone";

    // Getters and Setters
    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getServerProtocolVersion() {
        return serverProtocolVersion;
    }

    public void setServerProtocolVersion(String serverProtocolVersion) {
        this.serverProtocolVersion = serverProtocolVersion;
    }

    public String getServerGuid() {
        return serverGuid;
    }

    public void setServerGuid(String serverGuid) {
        this.serverGuid = serverGuid;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isCanGetLease() {
        return canGetLease;
    }

    public void setCanGetLease(boolean canGetLease) {
        this.canGetLease = canGetLease;
    }

    public int getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(int licenseType) {
        this.licenseType = licenseType;
    }

    public boolean isEvaluationLicense() {
        return evaluationLicense;
    }

    public void setEvaluationLicense(boolean evaluationLicense) {
        this.evaluationLicense = evaluationLicense;
    }

    public String getSeatPoolType() {
        return seatPoolType;
    }

    public void setSeatPoolType(String seatPoolType) {
        this.seatPoolType = seatPoolType;
    }
} 