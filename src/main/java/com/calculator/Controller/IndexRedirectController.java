package com.calculator.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 根路径重定向：访问/直接跳登录页
 */
@Controller
public class IndexRedirectController {
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login.html";
    }

    //如果有人直接访问index.html，也跳登录页
    @GetMapping("/index.html")
    public String redirectIndexToLogin() {
        // 未登录则跳转，已登录则放行（可选优化）
        return "redirect:/login.html";
    }
}
