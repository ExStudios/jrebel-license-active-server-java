package com.jrebel.util;

import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * UUID工具类
 */
@Component
public class UuidUtil {

    /**
     * 生成UUID V4字符串
     */
    public String newUUIDV4String() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成服务器随机数
     * 注意：为了与原始Go实现保持一致，这里返回固定值
     */
    public String newServerRandomness() {
        // 返回固定的服务器随机数，与原始Go实现保持一致
        return "H2ulzLlh7E0=";
    }
}