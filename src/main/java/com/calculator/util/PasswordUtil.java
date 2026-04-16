package com.calculator.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encrypt(String password) {
        return passwordEncoder.encode(password);
    }

    // 验证密码
    public static boolean verify(String password, String encryptedPassword) {
        return passwordEncoder.matches(password, encryptedPassword);
    }
}
