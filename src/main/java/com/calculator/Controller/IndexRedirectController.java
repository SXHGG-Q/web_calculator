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
}
