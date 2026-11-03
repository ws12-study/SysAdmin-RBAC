package com.yourname.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 密码强度策略（服务端强制校验，前端提示仅作辅助）
 * <p>
 * 规则：<br>
 * 1. 长度 8 ~ 20 位<br>
 * 2. 仅允许可见 ASCII 字符，不含空格、不支持中文<br>
 * 3. 大写字母 / 小写字母 / 数字 / 特殊符号，至少包含 3 类<br>
 * 4. 不能是常见弱口令<br>
 * 5. 不能包含 3 位及以上连续或重复字符（aaa / 123 / abc）<br>
 * 6. 不能包含账号名
 *
 * @author yourname
 */
public final class PasswordPolicy {

    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 20;

    /** 至少包含的字符种类数 */
    private static final int MIN_CATEGORIES = 3;

    /** 连续 / 重复的判定长度 */
    private static final int MAX_RUN = 3;

    private static final Set<String> WEAK_PASSWORDS = new HashSet<String>(Arrays.asList(
            "12345678", "123456789", "1234567890", "87654321", "password", "passw0rd",
            "admin123", "administrator", "abc12345", "a1234567", "qwertyui", "qwerty123",
            "iloveyou", "11111111", "00000000", "88888888", "5201314", "test1234",
            "sysadmin", "changeme", "abcd1234", "1q2w3e4r"));

    private PasswordPolicy() {
    }

    /**
     * @param password  明文密码
     * @param userName  账号名（用于判断密码是否包含账号），可为空
     * @return null 表示通过；否则返回给用户看的错误原因
     */
    public static String validate(String password, String userName) {
        if (password == null || password.isEmpty()) {
            return "密码不能为空";
        }
        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            return "密码长度必须为 " + MIN_LENGTH + " ~ " + MAX_LENGTH + " 位";
        }
        if (password.matches(".*\\s.*")) {
            return "密码不能包含空格";
        }
        // \x21-\x7E 覆盖可见 ASCII：字母、数字、特殊符号
        if (!password.matches("[\\x21-\\x7E]+")) {
            return "密码只能包含字母、数字和特殊符号，不支持中文或空格";
        }
        if (categories(password) < MIN_CATEGORIES) {
            return "密码需包含大写字母、小写字母、数字、特殊符号中的至少 " + MIN_CATEGORIES + " 类";
        }
        if (WEAK_PASSWORDS.contains(password.toLowerCase(Locale.ENGLISH))) {
            return "该密码过于常见，请更换";
        }
        if (hasSequence(password)) {
            return "密码不能包含 3 位及以上连续或重复字符（如 aaa、123、abc）";
        }
        if (containsUserName(password, userName)) {
            return "密码不能包含账号名";
        }
        return null;
    }

    /** 大写 / 小写 / 数字 / 特殊符号 四类命中数 */
    private static int categories(String password) {
        int count = 0;
        if (password.matches(".*[a-z].*")) {
            count++;
        }
        if (password.matches(".*[A-Z].*")) {
            count++;
        }
        if (password.matches(".*[0-9].*")) {
            count++;
        }
        if (password.matches(".*[^A-Za-z0-9].*")) {
            count++;
        }
        return count;
    }

    /**
     * 是否存在连续（abc / cba / 123 / 321）或重复（aaa）的长度 >= 3 片段
     */
    private static boolean hasSequence(String password) {
        int repeat = 1;
        int stepUp = 1;
        int stepDown = 1;
        char[] chars = password.toCharArray();
        for (int i = 1; i < chars.length; i++) {
            int delta = chars[i] - chars[i - 1];
            repeat = delta == 0 ? repeat + 1 : 1;
            stepUp = delta == 1 ? stepUp + 1 : 1;
            stepDown = delta == -1 ? stepDown + 1 : 1;
            if (repeat >= MAX_RUN || stepUp >= MAX_RUN || stepDown >= MAX_RUN) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsUserName(String password, String userName) {
        if (userName == null || userName.length() < 3) {
            return false;
        }
        String name = userName.toLowerCase(Locale.ENGLISH);
        String lower = password.toLowerCase(Locale.ENGLISH);
        return lower.contains(name) || lower.contains(new StringBuilder(name).reverse().toString());
    }

}
