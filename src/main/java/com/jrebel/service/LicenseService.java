package com.jrebel.service;

import com.jrebel.config.ServerConfig;
import com.jrebel.model.JRebelLeasesStruct;
import com.jrebel.model.JrebelLeases1Struct;
import com.jrebel.model.JrebelValidateStruct;
import com.jrebel.util.CryptoUtil;
import com.jrebel.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 许可证服务类
 */
@Service
public class LicenseService {

    @Autowired
    private CryptoUtil cryptoUtil;

    @Autowired
    private UuidUtil uuidUtil;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 创建许可证响应
     */
    public JRebelLeasesStruct createLeaseResponse(Map<String, String> parameters) throws Exception {
        String clientRandomness = parameters.get("randomness");
        String username = parameters.get("username");
        String guid = parameters.get("guid");
        String product = parameters.get("product");
        String clientTime = parameters.get("clientTime");
        String offlineDays = parameters.get("offlineDays");
        String oldGuid = parameters.get("oldGuid");
        String offlineParam = parameters.get("offline");

        System.out.println("=== JRebel客户端请求参数 ===");
        System.out.println("product = " + product);
        System.out.println("randomness = " + clientRandomness);
        System.out.println("guid = " + guid);
        System.out.println("offline = " + offlineParam);
        System.out.println("username = " + username);
        System.out.println("clientTime = " + clientTime);
        System.out.println("offlineDays = " + offlineDays);
        System.out.println("oldGuid = " + oldGuid);
        System.out.println("=============================");

        boolean offline = determineOfflineMode(product, parameters, oldGuid);

        JRebelLeasesStruct response = new JRebelLeasesStruct();
        response.setCompany(username);

        if (offline) {
            long startTimeInt = parseLongOrDefault(clientTime, System.currentTimeMillis() / 1000 * 1000);
            long offlineDaysInt = parseLongOrDefault(offlineDays, serverConfig.getOfflineDays());
            long expireTime = startTimeInt + (offlineDaysInt * 24 * 60 * 60 * 1000);
            response.setOffline(true);
            response.setValidFrom(startTimeInt);
            response.setValidUntil(expireTime);
        }

        // 生成serverRandomness
        String serverRandomness = generateServerRandomness();
        System.out.println("生成的serverRandomness: " + serverRandomness);
        response.setServerRandomness(serverRandomness);

        // 生成signature
        String validFrom = offline ? clientTime : "";
        String validUntil = offline ? String.valueOf(response.getValidUntil()) : "";
        String signature = createLeaseSignature(clientRandomness, serverRandomness, guid, offline, validFrom, validUntil);
        System.out.println("生成的signature: " + signature);
        response.setSignature(signature);

        System.out.println("响应中的serverRandomness: " + response.getServerRandomness());
        System.out.println("响应中的signature: " + response.getSignature());
        System.out.println("=====================");

        System.out.println("=== 最终响应信息 ===");
        System.out.println("最终serverRandomness: " + response.getServerRandomness());
        System.out.println("最终signature: " + response.getSignature());
        System.out.println("=====================");

        return response;
    }

    /**
     * 创建许可证响应1
     */
    public JrebelLeases1Struct createLease1Response(Map<String, String> parameters) {
        String username = parameters.get("username");
        
        JrebelLeases1Struct response = new JrebelLeases1Struct();
        if (username != null && !username.isEmpty()) {
            response.setCompany(username);
        }
        
        return response;
    }

    /**
     * 创建验证响应
     */
    public JrebelValidateStruct createValidateResponse() {
        return new JrebelValidateStruct();
    }

    /**
     * 创建Ping响应
     */
    public String createPingResponse(String salt) throws Exception {
        String xmlContent = "<PingResponse><message></message><responseCode>OK</responseCode><salt>" + salt + "</salt></PingResponse>";
        byte[] signature = cryptoUtil.signWithMd5(xmlContent.getBytes());
        String hexSignature = bytesToHex(signature);
        return "<!-- " + hexSignature + " -->\n" + xmlContent;
    }

    /**
     * 创建ObtainTicket响应
     */
    public String createObtainTicketResponse(String salt, String username) throws Exception {
        String prolongationPeriod = "607875500";
        String xmlContent = "<ObtainTicketResponse><message></message><prolongationPeriod>" + 
            prolongationPeriod + "</prolongationPeriod><responseCode>OK</responseCode><salt>" + 
            salt + "</salt><ticketId>1</ticketId><ticketProperties>licensee=" + 
            username + "\tlicenseType=0\t</ticketProperties></ObtainTicketResponse>";
        
        byte[] signature = cryptoUtil.signWithMd5(xmlContent.getBytes());
        String hexSignature = bytesToHex(signature);
        return "<!-- " + hexSignature + " -->\n" + xmlContent;
    }

    /**
     * 创建ReleaseTicket响应
     */
    public String createReleaseTicketResponse(String salt) throws Exception {
        String xmlContent = "<ReleaseTicketResponse><message></message><responseCode>OK</responseCode><salt>" + salt + "</salt></ReleaseTicketResponse>";
        byte[] signature = cryptoUtil.signWithMd5(xmlContent.getBytes());
        String hexSignature = bytesToHex(signature);
        return "<!-- " + hexSignature + " -->\n" + xmlContent;
    }

    /**
     * 判断离线模式
     */
    private boolean determineOfflineMode(String product, Map<String, String> parameters, String oldGuid) {
        // XRebel产品强制在线模式
        if ("XRebel".equals(product)) {
            return false;
        }

        int workMode = serverConfig.getJrebelWorkMode();
        
        switch (workMode) {
            case 1: // 强制离线模式
                return true;
            case 2: // 强制在线模式
                return false;
            case 3: // oldGuid模式
                return oldGuid != null && !oldGuid.isEmpty();
            default: // 自动模式
                String offlineParam = parameters.get("offline");
                return "true".equalsIgnoreCase(offlineParam);
        }
    }

    /**
     * 创建许可证签名
     */
    private String createLeaseSignature(String clientRandomness, String serverRandomness, 
                                      String guid, boolean offline, String validFrom, String validUntil) throws Exception {
        String s2;
        if (offline) {
            s2 = clientRandomness + ";" + serverRandomness + ";" + guid + ";" + String.valueOf(offline) + ";" + validFrom + ";" + validUntil;
        } else {
            s2 = clientRandomness + ";" + serverRandomness + ";" + guid + ";" + String.valueOf(offline);
        }
        System.out.println("签名拼接字符串: " + s2);
        System.out.println("字符串字节: " + java.util.Arrays.toString(s2.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        byte[] signature = cryptoUtil.signWithSha1(s2.getBytes());
        String base64 = cryptoUtil.encodeBase64(signature);
        System.out.println("签名结果(Base64): " + base64);
        return base64;
    }

    /**
     * 解析Long值，失败时返回默认值
     */
    private long parseLongOrDefault(String value, long defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 字节数组转十六进制字符串
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 生成服务器随机数
     */
    private String generateServerRandomness() {
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            int index = (int) (Math.random() * charset.length());
            sb.append(charset.charAt(index));
        }
        return sb.toString() + "=";
    }
} 