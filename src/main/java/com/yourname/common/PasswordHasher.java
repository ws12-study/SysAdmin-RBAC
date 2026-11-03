package com.yourname.common;

import org.springframework.util.DigestUtils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

/**
 * 密码哈希与校验
 * <p>
 * 新密码使用 PBKDF2（随机盐 + 多次迭代）存储，格式：
 * <pre>pbkdf2$迭代次数$Base64(盐)$Base64(哈希)</pre>
 * <p>
 * 历史遗留的无盐 MD5 口令仍可登录（向下兼容），登录成功后会由
 * {@link com.yourname.service.impl.SysUserServiceImpl} 自动重写为 PBKDF2，实现平滑迁移。
 *
 * @author yourname
 */
public final class PasswordHasher {

    private static final String PREFIX = "pbkdf2";
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    /** 老 JDK 不支持 SHA256 时的退路 */
    private static final String ALGORITHM_FALLBACK = "PBKDF2WithHmacSHA1";
    private static final int ITERATIONS = 120000;
    private static final int SALT_BYTES = 16;
    private static final int HASH_BYTES = 32;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordHasher() {
    }

    /**
     * 生成带随机盐的哈希串，同一明文每次结果都不同
     */
    public static String encode(String rawPassword) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] hash = pbkdf2(rawPassword, salt, ITERATIONS, HASH_BYTES);
        return PREFIX + '$' + ITERATIONS + '$' + b64(salt) + '$' + b64(hash);
    }

    /**
     * 校验明文与库中密文是否匹配，同时兼容旧的无盐 MD5
     */
    public static boolean matches(String rawPassword, String stored) {
        if (rawPassword == null || stored == null || stored.isEmpty()) {
            return false;
        }
        if (stored.startsWith(PREFIX + '$')) {
            String[] parts = stored.split("\\$");
            if (parts.length != 4) {
                return false;
            }
            int iterations;
            byte[] salt;
            byte[] expected;
            try {
                iterations = Integer.parseInt(parts[1]);
                salt = Base64.getDecoder().decode(parts[2]);
                expected = Base64.getDecoder().decode(parts[3]);
            } catch (IllegalArgumentException e) {
                return false;
            }
            byte[] actual = pbkdf2(rawPassword, salt, iterations, expected.length);
            // 定长比较，避免时序侧信道
            return MessageDigest.isEqual(expected, actual);
        }
        return legacyMd5(rawPassword).equalsIgnoreCase(stored);
    }

    /**
     * 是否为旧格式（无盐 MD5），用于登录后自动升级
     */
    public static boolean isLegacy(String stored) {
        return stored == null || !stored.startsWith(PREFIX + '$');
    }

    /** 旧格式：无盐 MD5 十六进制 */
    public static String legacyMd5(String rawPassword) {
        return DigestUtils.md5DigestAsHex(rawPassword.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] pbkdf2(String rawPassword, byte[] salt, int iterations, int length) {
        char[] chars = rawPassword == null ? new char[0] : rawPassword.toCharArray();
        try {
            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, length * 8);
            try {
                return secretKeyFactory().generateSecret(spec).getEncoded();
            } catch (InvalidKeySpecException e) {
                throw new IllegalStateException("密码哈希计算失败", e);
            } finally {
                spec.clearPassword();
            }
        } finally {
            Arrays.fill(chars, '\0');
        }
    }

    private static SecretKeyFactory secretKeyFactory() {
        try {
            return SecretKeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            try {
                return SecretKeyFactory.getInstance(ALGORITHM_FALLBACK);
            } catch (NoSuchAlgorithmException ex) {
                throw new IllegalStateException("当前 JDK 不支持 PBKDF2", ex);
            }
        }
    }

    private static String b64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

}
