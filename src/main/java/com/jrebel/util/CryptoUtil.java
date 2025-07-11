package com.jrebel.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * 加密工具类
 */
@Component
public class CryptoUtil {

    // RSA私钥
    private static final String PRIVATE_KEY = 
        "-----BEGIN RSA PRIVATE KEY-----\n" +
        "MIIEpAIBAAKCAQEA0vx7agoebGcTDuuK8gcZImCCtdsZrFWJxHKDoe0aY9fPlwGK\n" +
        "aqfwtZsxigEPjwJipxnxE2WRnW4HYPmJ8a1qjrsctNn3Xk7avihS4q/0Wf2Bj8\n" +
        "UZbB10uQZvEOkXjf9vLwxjh2ZrrJCa1dfFgOvuchYvJGB+NvW+s7MZ6VkWMA4K\n" +
        "l5p8Va1pfMBr9C1Xqw97jgUGu8PutZxoLqqyS1OOLFvmmytXpgMRoIiw9qN5n\n" +
        "dvGXJWQcr8TjXkHjGTXgE4l6V9MuVuF6CDnKuOnW48aGvzkR6LJoQm+r+Vm\n" +
        "eqMXOhu3gnEkBkX4f7M5mWnsK6o81j3hVEzbbcmU5USn0Y6mOOeaFpmCAo\n" +
        "mbmHjXlOr7eQZ9sSgJgwriRUBY5iR2QzGlgUjE15wlFAFwIDAQABAoIBAB\n" +
        "hOOo1Iqo2jb9wxzDmQhHNCmLkDHMou9oa8RrnyRyJlmQ9OQHHPZvGJFKVb\n" +
        "nJtD8fdV/on4G+Fa36Z6d8WUY6Xw5RsGCj70G6WP3aEQRHUEncT5p5Lgf\n" +
        "Z4J9Ww8/acxK6RE4voaxcXlVHsKtQvJfP0mY0z4I9kbGcntE6uUYOSgF\n" +
        "Y5pIdfe9dOwJ1xL0tC47G79Dz3g==\n" +
        "-----END RSA PRIVATE KEY-----";

    // RSA私钥
    private static final String ASM_PRIVATE_KEY = 
        "-----BEGIN PRIVATE KEY-----\n" +
        "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAt5yrcHAAjhglnCEn\n" +
        "6yecMWPeUXcMyo0+itXrLlkpcKIIyqPw546bGThhlb1ppX1ySX/OUA4jSakHekNP\n" +
        "5eWPawIDAQABAkBbr9pUPTmpuxkcy9m5LYBrkWk02PQEOV/fyE62SEPPP+GRhv4Q\n" +
        "Fgsu+V2GCwPQ69E3LzKHPsSNpSosIHSO4g3hAiEA54JCn41fF8GZ90b9L5dtFQB2\n" +
        "/yIcGX4Xo7bCvl8DaPMCIQDLCUN8YiXppydqQ+uYkTQgvyq+47cW2wcGumRS46dd\n" +
        "qQIhAKp2v5e8AMj9ROFO5B6m4SsVrIkwFICw17c0WzDRxTEBAiAYDmftk990GLcF\n" +
        "0zhV4lZvztasuWRXE+p4NJtwasLIyQIgVKzknJe8VOt5a3shCMOyysoNEg+YAt02\n" +
        "O98RPCU0nJg=\n" +
        "-----END PRIVATE KEY-----";

    /**
     * 使用SHA1进行RSA签名
     */
    public byte[] signWithSha1(byte[] data) throws Exception {
        PrivateKey privateKey = getPrivateKey(PRIVATE_KEY);
        
        // 使用标准的SHA1withRSA方法
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * 使用MD5进行RSA签名
     */
    public byte[] signWithMd5(byte[] data) throws Exception {
        // 使用Go版本中相同的ASM私钥
        String asmPkcs8Key = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAt5yrcHAAjhglnCEn\n" +
            "6yecMWPeUXcMyo0+itXrLlkpcKIIyqPw546bGThhlb1ppX1ySX/OUA4jSakHekNP\n" +
            "5eWPawIDAQABAkBbr9pUPTmpuxkcy9m5LYBrkWk02PQEOV/fyE62SEPPP+GRhv4Q\n" +
            "Fgsu+V2GCwPQ69E3LzKHPsSNpSosIHSO4g3hAiEA54JCn41fF8GZ90b9L5dtFQB2\n" +
            "/yIcGX4Xo7bCvl8DaPMCIQDLCUN8YiXppydqQ+uYkTQgvyq+47cW2wcGumRS46dd\n" +
            "qQIhAKp2v5e8AMj9ROFO5B6m4SsVrIkwFICw17c0WzDRxTEBAiAYDmftk990GLcF\n" +
            "0zhV4lZvztasuWRXE+p4NJtwasLIyQIgVKzknJe8VOt5a3shCMOyysoNEg+YAt02\n" +
            "O98RPCU0nJg=\n" +
            "-----END PRIVATE KEY-----";
        
        String asmPkcs8Content = asmPkcs8Key
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");
        
        byte[] asmPkcs8Bytes = Base64.getDecoder().decode(asmPkcs8Content);
        PKCS8EncodedKeySpec asmPkcs8KeySpec = new PKCS8EncodedKeySpec(asmPkcs8Bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(asmPkcs8KeySpec);
        
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * Base64编码
     */
    public String encodeBase64(byte[] data) {
        if (data == null) {
            return "";
        }
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Base64解码
     */
    public byte[] decodeBase64(String str) {
        if (str == null || str.isEmpty()) {
            return new byte[0];
        }
        try {
            return Base64.getDecoder().decode(str);
        } catch (IllegalArgumentException e) {
            return new byte[0];
        }
    }

    /**
     * 从PEM格式的私钥字符串中解析私钥
     */
    private PrivateKey getPrivateKey(String privateKeyPem) throws Exception {
        // 使用Go版本中相同的PKCS8格式私钥
        String pkcs8Key = "-----BEGIN PRIVATE KEY-----\n" +
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAND3cI/pKMSd4OLMIXU/8xoEZ/nz\n" +
            "a+g00Vy7ygyGB1Nn83qpro7tckOvUVILJoN0pKw8J3E8rtjhSyr9849qzaQKBhxFL+J5uu08QVn/\n" +
            "tMt+Tf0cu5MSPOjT8I2+NWyBZ6H0FjOcVrEUMvHt8sqoJDrDU4pJyex2rCOlpfBeqK6XAgMBAAEC\n" +
            "gYBM5C+8FIxWxM1CRuCs1yop0aM82vBC0mSTXdo7/3lknGSAJz2/A+o+s50Vtlqmll4drkjJJw4j\n" +
            "acsR974OcLtXzQrZ0G1ohCM55lC3kehNEbgQdBpagOHbsFa4miKnlYys537Wp+Q61mhGM1weXzos\n" +
            "gCH/7e/FjJ5uS6DhQc0Y+QJBAP43hlSSEo1BbuanFfp55yK2Y503ti3Rgf1SbE+JbUvIIRsvB24x\n" +
            "Ha1/IZ+ttkAuIbOUomLN7fyyEYLWphIy9kUCQQDSbqmxZaJNRa1o4ozGRORxR2KBqVn3EVISXqNc\n" +
            "UH3gAP52U9LcnmA3NMSZs8tzXhUhYkWQ75Q6umXvvDm4XZ0rAkBoymyWGeyJy8oyS/fUW0G63mIr\n" +
            "oZZ4Rp+F098P3j9ueJ2k/frbImXwabJrhwjUZe/Afel+PxL2ElUDkQW+BMHdAkEAk/U7W4Aanjpf\n" +
            "s1+Xm9DUztFicciheRa0njXspvvxhY8tXAWUPYseG7L+iRPh+Twtn0t5nm7VynVFN0shSoCIAQJA\n" +
            "Ljo7A6bzsvfnJpV+lQiOqD/WCw3A2yPwe+1d0X/13fQkgzcbB3K0K81Euo/fkKKiBv0A7yR7wvrN\n" +
            "jzefE9sKUw==\n" +
            "-----END PRIVATE KEY-----";
        
        String pkcs8Content = pkcs8Key
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");
        
        byte[] pkcs8Bytes = Base64.getDecoder().decode(pkcs8Content);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(pkcs8KeySpec);
    }
} 