package com.calculator.Controller;

import com.calculator.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
public class CalculatorController {

    private final MathUtil mathUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CalculatorController(MathUtil mathUtil) {
        this.mathUtil = mathUtil;
    }

    //加法
    @GetMapping("/add")
    public double add(@RequestParam double angle1, @RequestParam double angle2) {
        return MathUtil.add(angle1, angle2);
    }

    //减法
    @GetMapping("/sub")
    public double sub(@RequestParam double angle1, @RequestParam double angle2) {
        return MathUtil.sub(angle1, angle2);
    }

    //乘法
    @GetMapping("/mul")
    public double mul(@RequestParam double angle1, @RequestParam double angle2) {
        return MathUtil.mul(angle1, angle2);
    }

    //除法
    @GetMapping("/div")
    public double div(@RequestParam double angle1, @RequestParam double angle2) {
        return MathUtil.div(angle1, angle2);
    }

    //取余
    @GetMapping("/mod")
    public double mod(@RequestParam double angle1, @RequestParam double angle2) {
        return MathUtil.mod(angle1, angle2);
    }

    //指数
    @GetMapping("/pow")
    public double pow(@RequestParam double angle1, @RequestParam double angle2) {
        return MathUtil.pow(angle1, angle2);
    }

    //自定义对数
    @GetMapping("/log")
    public double log(@RequestParam double angle1, @RequestParam double angle2) {
        return MathUtil.log(angle1, angle2);
    }

    //自定义开方
    @GetMapping("/sqrt")
    public double sqrt(@RequestParam double angle1, @RequestParam double angle2) {
        return MathUtil.sqrt(angle1, angle2);
    }

    //sin
    @GetMapping("/sin")
    public double sin(@RequestParam double angle1) {
        return MathUtil.sin(angle1);
    }

    //cos
    @GetMapping("/cos")
    public double cos(@RequestParam double angle1) {
        return MathUtil.cos(angle1);
    }

    //tan
    @GetMapping("/tan")
    public double tan(@RequestParam double angle1) {
        return MathUtil.tan(angle1);
    }

    //向上取整
    @GetMapping("/ceil")
    public double ceil(@RequestParam double angle1) {
        return MathUtil.ceil(angle1);
    }

    //向下取整
    @GetMapping("/floor")
    public double floor(@RequestParam double angle1) {
        return MathUtil.floor(angle1);
    }

    //四舍五入
    @GetMapping("/round")
    public double round(@RequestParam double angle1) {
        return MathUtil.round(angle1);
    }

    //等于
    @GetMapping("/isEqual")
    public boolean isEqual(@RequestParam double angle1, @RequestParam double angle2) {
        return MathUtil.isEqual(angle1, angle2);
    }

    //保存记录
    @GetMapping("/save")
    public String save(
            @RequestParam String expression,
            @RequestParam double result
    ){
        String sql = "insert into calc_record(expression,result) values(?,?)";
        jdbcTemplate.update(sql,expression,result);
        return "保存成功";
    }

    //查询记录
    @GetMapping("/query")
    public String query(){
        String sql ="select * from calc_record order by id desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        StringBuilder sb = new StringBuilder();
        sb.append("==================== 计算历史记录 ====================\n");

        for (Map<String, Object> map : list) {
            sb.append("序号：").append(map.get("id")).append("\n");
            sb.append("计算式：").append(map.get("expression")).append("\n");
            sb.append("结果：").append(map.get("result")).append("\n");
            sb.append("时间：").append(map.get("create_time")).append("\n");
            sb.append("----------------------------------------------------\n");
        }
        return sb.toString();
    }

    //保存公式
    @GetMapping("/saveFormula")
    public String saveFormula(@RequestParam String name,@RequestParam String content){
        //sql语句
        String sql = "insert into formula(formula_name,formula_content) values(?,?)";
        jdbcTemplate.update(sql,name,content);
        return "保存成功";
    }

    //查询公式
    @GetMapping("/listFormulas")
    public String listFormulas(){
        String sql = "select * from formula order by id desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        StringBuilder sb = new StringBuilder();
        sb.append("==================== 公式列表 ====================\n");
        for (Map<String, Object> map : list) {
            sb.append("序号：").append(map.get("id")).append("\n");
            sb.append("公式名称：").append(map.get("formula_name")).append("\n");
            sb.append("公式内容：").append(map.get("formula_content")).append("\n");
            sb.append("----------------------------------------------------\n");
        }
        return sb.toString();
    }

    //调用公式
    @GetMapping("/calcFormula")
    public String calcFormula(
            String name,
            @RequestParam(defaultValue = "0") Double x1,
            @RequestParam(defaultValue = "0") Double x2,
            @RequestParam(defaultValue = "0") Double x3,
            @RequestParam(defaultValue = "0") Double x4,
            @RequestParam(defaultValue = "0") Double x5,
            @RequestParam(defaultValue = "0") Double x6,
            @RequestParam(defaultValue = "0") Double x7,
            @RequestParam(defaultValue = "0") Double x8,
            @RequestParam(defaultValue = "0") Double x9
    ){
        return mathUtil.calcFormula(name, x1, x2, x3, x4, x5, x6, x7, x8, x9);
    }
}

