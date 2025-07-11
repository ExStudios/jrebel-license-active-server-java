package com.jrebel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 服务器配置类
 */
@Component
@ConfigurationProperties(prefix = "jrebel.server")
public class ServerConfig {
    
    /**
     * 服务器端口，默认12345
     */
    private int port = 12345;
    
    /**
     * 离线天数，默认30天
     */
    private int offlineDays = 30;
    
    /**
     * 日志级别，1-4
     */
    private int logLevel = 2;
    
    /**
     * 导出协议，http或https
     */
    private String exportSchema = "http";
    
    /**
     * 导出主机，默认为请求IP
     */
    private String exportHost = "";
    
    /**
     * 是否使用新索引页面
     */
    private boolean newIndex = true;
    
    /**
     * JRebel工作模式
     * 0: 自动, 1: 强制离线模式, 2: 强制在线模式, 3: oldGuid
     */
    private int jrebelWorkMode = 0;

    // Getters and Setters
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getOfflineDays() {
        return offlineDays;
    }

    public void setOfflineDays(int offlineDays) {
        this.offlineDays = offlineDays;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public String getExportSchema() {
        return exportSchema;
    }

    public void setExportSchema(String exportSchema) {
        this.exportSchema = exportSchema;
    }

    public String getExportHost() {
        return exportHost;
    }

    public void setExportHost(String exportHost) {
        this.exportHost = exportHost;
    }

    public boolean isNewIndex() {
        return newIndex;
    }

    public void setNewIndex(boolean newIndex) {
        this.newIndex = newIndex;
    }

    public int getJrebelWorkMode() {
        return jrebelWorkMode;
    }

    public void setJrebelWorkMode(int jrebelWorkMode) {
        this.jrebelWorkMode = jrebelWorkMode;
    }
} 