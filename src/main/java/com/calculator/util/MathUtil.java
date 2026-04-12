package com.calculator.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Component
public class MathUtil {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MathUtil(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static double add(double a, double b) {
        return a + b;
    }

    public static double sub(double a, double b) {
        return a - b;
    }

    public static double mul(double a, double b) {
        return a * b;
    }

    public static double div(double a, double b) {
        // 处理除数为0的情况
        if (b == 0) {
            throw new IllegalArgumentException("除数不能为0");
        }
        return a / b;
    }

    public static double mod(double a, double b) {
        // 处理除数为0的情况
        if (b == 0) {
            throw new IllegalArgumentException("除数不能为0");
        }
        return a % b;
    }

    public static double pow(double a, double b) {
        // 处理指数为负的情况
        if (b < 0) {
            throw new IllegalArgumentException("指数不能为负");
        }
        return Math.pow(a, b);
    }

    //自定义对数
    public static double log(double a, double base) {
        // 处理负数的情况
        if (a <= 0) {
            throw new IllegalArgumentException("负数不能取对数");
        }
        // 处理底数为1的情况
        if (base == 1) {
            throw new IllegalArgumentException("底数不能为1");
        }
        return Math.log(a) / Math.log(base);
    }

    //自定义开方
    public static double sqrt(double a, double base) {
        // 处理负数的情况
        if (a < 0) {
            throw new IllegalArgumentException("负数不能取开方");
        }
        // 处理底数为1的情况
        if (base == 1) {
            throw new IllegalArgumentException("底数不能为1");
        }
        return Math.pow(a, 1 / base);
    }

    //sin（用角度）
    public static double sin(double a) {
        return Math.sin(Math.toRadians(a));
    }

    //cos（用角度）
    public static double cos(double a) {
        return Math.cos(Math.toRadians(a));
    }

    //tan（用角度）
    public static double tan(double a) {
        return Math.tan(Math.toRadians(a));
    }

    //向上取整
    public static double ceil(double a) {
        return Math.ceil(a);
    }

    //向下取整
    public static double floor(double a) {
        return Math.floor(a);
    }

    //四舍五入
    public static double round(double a) {
        return Math.round(a);
    }

    //等于
    public static boolean isEqual(double a, double b) {
        return a == b;
    }

    //自定义公式
    public String calcFormula(String name, double x1, double x2, double x3, double x4, double x5, double x6, double x7, double x8, double x9) {
        try {
            String formula = jdbcTemplate.queryForObject("select formula_content from formula where formula_name = ?", String.class, name);
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
            engine.put("x1", x1);
            engine.put("x2", x2);
            engine.put("x3", x3);
            engine.put("x4", x4);
            engine.put("x5", x5);
            engine.put("x6", x6);
            engine.put("x7", x7);
            engine.put("x8", x8);
            engine.put("x9", x9);
            return engine.eval(formula).toString();
        } catch (ScriptException e) {
            return "计算错误"+e.getMessage();
        }
    }
}
