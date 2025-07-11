package com.jrebel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * JRebel许可证响应结构
 */
public class JRebelLeasesStruct {
    
    @JsonProperty("serverVersion")
    private String serverVersion = "3.2.4";
    
    @JsonProperty("serverProtocolVersion")
    private String serverProtocolVersion = "1.1";
    
    @JsonProperty("serverGuid")
    private String serverGuid = "a1b4aea8-b031-4302-b602-670a990272cb";
    
    @JsonProperty("groupType")
    private String groupType = "managed";
    
    @JsonProperty("id")
    private int id = 1;
    
    @JsonProperty("licenseType")
    private int licenseType = 1;
    
    @JsonProperty("evaluationLicense")
    private boolean evaluationLicense = false;
    
    @JsonProperty("signature")
    private String signature = "OJE9wGg2xncSb+VgnYT+9HGCFaLOk28tneMFhCbpVMKoC/Iq4LuaDKPirBjG4o394/UjCDGgTBpIrzcXNPdVxVr8PnQzpy7ZSToGO8wv/KIWZT9/ba7bDbA8/RZ4B37YkCeXhjaixpmoyz/CIZMnei4q7oWR7DYUOlOcEWDQhiY=";
    
    @JsonProperty("serverRandomness")
    private String serverRandomness = "H2ulzLlh7E0=";
    
    @JsonProperty("seatPoolType")
    private String seatPoolType = "standalone";
    
    @JsonProperty("statusCode")
    private String statusCode = "SUCCESS";
    
    @JsonProperty("offline")
    private boolean offline = false;
    
    @JsonProperty("validFrom")
    private Long validFrom = null;
    
    @JsonProperty("validUntil")
    private Long validUntil = null;
    
    @JsonProperty("company")
    private String company = "Administrator";
    
    @JsonProperty("orderId")
    private String orderId = "";
    
    @JsonProperty("zeroIds")
    private List<Object> zeroIds = List.of();
    
    @JsonProperty("licenseValidFrom")
    private Long licenseValidFrom = 1490544001000L;
    
    @JsonProperty("licenseValidUntil")
    private Long licenseValidUntil = 1891839999000L;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getServerRandomness() {
        return serverRandomness;
    }

    public void setServerRandomness(String serverRandomness) {
        this.serverRandomness = serverRandomness;
    }

    public String getSeatPoolType() {
        return seatPoolType;
    }

    public void setSeatPoolType(String seatPoolType) {
        this.seatPoolType = seatPoolType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public Long getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Long validFrom) {
        this.validFrom = validFrom;
    }

    public Long getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Long validUntil) {
        this.validUntil = validUntil;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<Object> getZeroIds() {
        return zeroIds;
    }

    public void setZeroIds(List<Object> zeroIds) {
        this.zeroIds = zeroIds;
    }

    public Long getLicenseValidFrom() {
        return licenseValidFrom;
    }

    public void setLicenseValidFrom(Long licenseValidFrom) {
        this.licenseValidFrom = licenseValidFrom;
    }

    public Long getLicenseValidUntil() {
        return licenseValidUntil;
    }

    public void setLicenseValidUntil(Long licenseValidUntil) {
        this.licenseValidUntil = licenseValidUntil;
    }
} 