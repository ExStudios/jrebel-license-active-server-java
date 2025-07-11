package com.jrebel.controller;

import com.jrebel.config.ServerConfig;
import com.jrebel.model.JRebelLeasesStruct;
import com.jrebel.model.JrebelLeases1Struct;
import com.jrebel.model.JrebelValidateStruct;
import com.jrebel.service.LicenseService;
import com.jrebel.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * JRebel控制器
 */
@RestController
public class JRebelController {

    private static final Logger logger = LoggerFactory.getLogger(JRebelController.class);

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private UuidUtil uuidUtil;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 主页面
     */
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index(HttpServletRequest request) {
        String host = getHost(request);
        String uuid = uuidUtil.newUUIDV4String();
        
        logger.info("Index page accessed from: {}", request.getRemoteAddr());
        
        if (serverConfig.isNewIndex()) {
            return generateNewIndexHtml(host, uuid);
        } else {
            return generateSimpleIndexHtml(host, uuid);
        }
    }

    /**
     * JRebel许可证接口
     */
    @PostMapping(value = {"/jrebel/leases", "/agent/leases"}, 
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JRebelLeasesStruct> jrebelLeases(@RequestBody String body, 
                                                          HttpServletRequest request) {
        logRequest(request, body);
        
        try {
            Map<String, String> parameters = parseParameters(body);
            
            // 验证必需参数
            if (!validateRequiredParameters(parameters, "randomness", "username", "guid")) {
                return ResponseEntity.status(403).build();
            }
            
            JRebelLeasesStruct response = licenseService.createLeaseResponse(parameters);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing jrebel leases request", e);
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * JRebel许可证接口1
     */
    @PostMapping(value = {"/jrebel/leases/1", "/agent/leases/1"}, 
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JrebelLeases1Struct> jrebelLeases1(@RequestBody String body, 
                                                            HttpServletRequest request) {
        logRequest(request, body);
        
        try {
            Map<String, String> parameters = parseParameters(body);
            JrebelLeases1Struct response = licenseService.createLease1Response(parameters);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing jrebel leases1 request", e);
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * JRebel验证连接接口
     */
    @PostMapping(value = "/jrebel/validate-connection", 
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JrebelValidateStruct> jrebelValidate(HttpServletRequest request) {
        logRequest(request, null);
        
        try {
            JrebelValidateStruct response = licenseService.createValidateResponse();
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing jrebel validate request", e);
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * Ping接口
     */
    @PostMapping(value = "/rpc/ping.action", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> ping(@RequestBody String body, HttpServletRequest request) {
        logRequest(request, body);
        
        try {
            Map<String, String> parameters = parseParameters(body);
            String salt = parameters.get("salt");
            
            if (salt == null || salt.isEmpty()) {
                return ResponseEntity.status(403).build();
            }
            
            String response = licenseService.createPingResponse(salt);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing ping request", e);
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * 获取票据接口
     */
    @PostMapping(value = "/rpc/obtainTicket.action", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> obtainTicket(@RequestBody String body, HttpServletRequest request) {
        logRequest(request, body);
        
        try {
            Map<String, String> parameters = parseParameters(body);
            String salt = parameters.get("salt");
            String username = parameters.get("userName");
            
            if (salt == null || salt.isEmpty() || username == null || username.isEmpty()) {
                return ResponseEntity.status(403).build();
            }
            
            String response = licenseService.createObtainTicketResponse(salt, username);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing obtainTicket request", e);
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * 释放票据接口
     */
    @PostMapping(value = "/rpc/releaseTicket.action", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> releaseTicket(@RequestBody String body, HttpServletRequest request) {
        logRequest(request, body);
        
        try {
            Map<String, String> parameters = parseParameters(body);
            String salt = parameters.get("salt");
            
            if (salt == null || salt.isEmpty()) {
                return ResponseEntity.status(403).build();
            }
            
            String response = licenseService.createReleaseTicketResponse(salt);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing releaseTicket request", e);
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * 记录请求日志
     */
    private void logRequest(HttpServletRequest request, String body) {
        String query = request.getQueryString();
        String queryString = query != null ? "?" + query : "";
        
        logger.info("--> {} {}{}. [{}] [{}]", 
                   request.getMethod(), 
                   request.getRequestURI(), 
                   queryString, 
                   request.getRemoteAddr(), 
                   request.getHeader("User-Agent"));
        
        if (body != null && logger.isDebugEnabled()) {
            logger.debug("--> Request Body: {}", body);
        }
    }

    /**
     * 解析请求参数
     */
    private Map<String, String> parseParameters(String body) {
        Map<String, String> parameters = new HashMap<>();
        
        if (body != null && !body.isEmpty()) {
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    try {
                        String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                        String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                        parameters.put(key, value);
                    } catch (Exception e) {
                        // 如果解码失败，使用原始值
                        parameters.put(keyValue[0], keyValue[1]);
                    }
                }
            }
        }
        
        return parameters;
    }

    /**
     * 验证必需参数
     */
    private boolean validateRequiredParameters(Map<String, String> parameters, String... requiredParams) {
        for (String param : requiredParams) {
            String value = parameters.get(param);
            if (value == null || value.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取主机地址
     */
    private String getHost(HttpServletRequest request) {
        if (serverConfig.getExportHost() != null && !serverConfig.getExportHost().isEmpty()) {
            return serverConfig.getExportSchema() + "://" + serverConfig.getExportHost();
        } else {
            return serverConfig.getExportSchema() + "://" + request.getServerName() + 
                   (request.getServerPort() != 80 ? ":" + request.getServerPort() : "");
        }
    }

    /**
     * 生成新索引页面HTML
     */
    private String generateNewIndexHtml(String host, String uuid) {
        String html = """
            <!DOCTYPE html>
            <html lang=\"en\">
            <head>
                <meta charset=\"UTF-8\">
                <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
                <title>JRebel License Server</title>
                <style>
                    :root {
                        --primary-color: #4a6bff;
                        --secondary-color: #f5f5f5;
                        --accent-color: #ff5252;
                        --text-color: #333;
                        --light-text: #666;
                        --border-radius: 8px;
                        --box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                        --transition: all 0.3s ease;
                    }
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        line-height: 1.6;
                        color: var(--text-color);
                        background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
                        min-height: 100vh;
                        padding: 20px;
                    }
                    .container {
                        max-width: 900px;
                        margin: 40px auto;
                        background-color: white;
                        border-radius: var(--border-radius);
                        box-shadow: var(--box-shadow);
                        overflow: hidden;
                    }
                    header {
                        background-color: var(--primary-color);
                        color: white;
                        padding: 20px 30px;
                        position: relative;
                    }
                    h1 { font-size: 28px; margin-bottom: 10px; }
                    .content { padding: 30px; }
                    .info-card {
                        background-color: var(--secondary-color);
                        border-radius: var(--border-radius);
                        padding: 20px;
                        margin-bottom: 20px;
                        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
                    }
                    .info-card h2 {
                        font-size: 20px;
                        margin-bottom: 15px;
                        color: var(--primary-color);
                    }
                    .activation-url {
                        display: flex;
                        align-items: center;
                        background-color: white;
                        border: 1px solid #ddd;
                        border-radius: 4px;
                        padding: 10px;
                        margin: 10px 0;
                        position: relative;
                    }
                    .url-text {
                        flex-grow: 1;
                        font-family: monospace;
                        word-break: break-all;
                    }
                    .highlight {
                        color: var(--accent-color);
                        font-weight: bold;
                    }
                    footer {
                        text-align: center;
                        padding: 20px;
                        color: var(--light-text);
                        font-size: 14px;
                        border-top: 1px solid #eee;
                    }
                    footer a {
                        color: var(--primary-color);
                        text-decoration: none;
                    }
                    footer a:hover { text-decoration: underline; }
                    @media (max-width: 768px) {
                        .container { margin: 20px auto; }
                        header { padding: 15px 20px; }
                        .content { padding: 20px; }
                    }
                </style>
            </head>
            <body>
            <div class=\"container\">
                <header>
                    <h1>JRebel License Server</h1>
                </header>
                <div class=\"content\">
                    <div class=\"info-card\">
                        <h2>Server Information</h2>
                        <p>License Server started at:</p>
                        <div class=\"activation-url\">
                            <span class=\"url-text\">{host}</span>
                        </div>
                    </div>
                    <div class=\"info-card\">
                        <h2>JRebel 7.1 and Earlier Versions</h2>
                        <p>Activation address (with any email):</p>
                        <div class=\"activation-url\">
                            <span class=\"url-text\">{host}/<span class=\"highlight\">{tokenname}</span></span>
                        </div>
                    </div>
                    <div class=\"info-card\">
                        <h2>JRebel 2018.1 and Later Versions</h2>
                        <p>Activation address (with any email address):</p>
                        <div class=\"activation-url\">
                            <span class=\"url-text\">{host}/<span class=\"highlight\">{uuid}</span></span>
                        </div>
                    </div>
                </div>
                <footer>
                    <p>
                        <a href=\"https://github.com/yu-xiaoyao/jrebel-license-active-server\" target=\"_blank\">Developed from 2019 year</a>
                    </p>
                </footer>
            </div>
            </body>
            </html>
            """;
        return html.replace("{host}", host)
                   .replace("{uuid}", uuid)
                   .replace("{tokenname}", "{tokenname}");
    }

    /**
     * 生成简单索引页面HTML
     */
    private String generateSimpleIndexHtml(String host, String uuid) {
        return """
            <h1>Hello,This is a Jrebel License Server!</h1>
            <p>License Server started at %s</p>
            <p>JRebel 7.1 and earlier version Activation address was: <span style='color:red'>%s/{tokenname}</span>, with any email.</p>
            <p>JRebel 2018.1 and later version Activation address was: %s/{guid}(eg:<span style='color:red'> %s/%s </span>), with any email.</p>
            """.formatted(host, host, host, host, uuid);
    }
} 