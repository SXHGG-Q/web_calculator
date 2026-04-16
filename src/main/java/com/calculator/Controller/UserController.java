package com.calculator.Controller;

import com.calculator.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*用户注册*/
    @GetMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        try {
            if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                return "用户名或密码不能为空";
            }

            // 检查用户名是否存在
            String checkUsernameSql = "SELECT COUNT(*) FROM user WHERE username = ?";
            int count = jdbcTemplate.queryForObject(checkUsernameSql, Integer.class, username.trim());
            if(count > 0) {
                return "注册失败：用户名已存在";
            }
            //存储用户信息到数据库
            String encryptedPassword = PasswordUtil.encrypt(password.trim());
            String insertSql = "INSERT INTO user (username, password) VALUES (?, ?)";
            jdbcTemplate.update(insertSql, username.trim(), encryptedPassword);
            return "注册成功";
        } catch (DataAccessException e) {
            return "注册失败：" + e.getMessage();
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        try {
            String sql = "select id,password from user_info where username = ?";
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, username.trim());
            if (rows.isEmpty()) {
                return "登录失败：用户名不存在";
            }
            Map<String, Object> row = rows.get(0);
            String encodedPwd = row.get("password").toString();
            if (!PasswordUtil.verify(password.trim(), encodedPwd)) {
                return "登录失败：密码错误";
            }
            return "登录成功"+row.get("id").toString();
        } catch (DataAccessException e) {
            return "登录失败：" + e.getMessage();
        }
    }
}
