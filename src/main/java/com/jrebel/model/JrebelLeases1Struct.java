package com.jrebel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JRebel许可证响应结构1
 */
public class JrebelLeases1Struct {
    
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
    
    @JsonProperty("msg")
    private Object msg = null;
    
    @JsonProperty("statusMessage")
    private Object statusMessage = null;
    
    @JsonProperty("signature")
    private String signature = "dGVzdA==";

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

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Object getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(Object statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
} 