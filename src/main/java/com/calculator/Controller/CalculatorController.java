package com.calculator.Controller;

import com.calculator.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 科学计算器核心控制器
 * 修复点：参数校验、异常处理、前后端参数适配、运算合法性校验
 */
@RestController
public class CalculatorController {

    private final MathUtil mathUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CalculatorController(MathUtil mathUtil) {
        this.mathUtil = mathUtil;
    }

    // ===================== 基础运算接口（带参数校验+异常处理） =====================
    /**
     * 加法运算
     */
    @GetMapping("/add")
    public Object add(@RequestParam double angle1, @RequestParam double angle2) {
        try {
            double result = MathUtil.add(angle1, angle2);
            // 保留6位小数，避免精度溢出
            return new BigDecimal(result).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return "加法运算失败：" + e.getMessage();
        }
    }

    /**
     * 减法运算
     */
    @GetMapping("/sub")
    public Object sub(@RequestParam double angle1, @RequestParam double angle2) {
        try {
            double result = MathUtil.sub(angle1, angle2);
            return new BigDecimal(result).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return "减法运算失败：" + e.getMessage();
        }
    }

    /**
     * 乘法运算
     */
    @GetMapping("/mul")
    public Object mul(@RequestParam double angle1, @RequestParam double angle2) {
        try {
            double result = MathUtil.mul(angle1, angle2);
            return new BigDecimal(result).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return "乘法运算失败：" + e.getMessage();
        }
    }

    /**
     * 除法运算（核心修复：除数为0校验）
     */
    @GetMapping("/div")
    public Object div(@RequestParam double angle1, @RequestParam double angle2) {
        try {
            // 校验除数为0
            if (Math.abs(angle2) < 1e-10) { // 浮点数精度容错
                return "除法运算失败：除数不能为0";
            }
            double result = MathUtil.div(angle1, angle2);
            return new BigDecimal(result).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return "除法运算失败：" + e.getMessage();
        }
    }

    /**
     * 取余运算（补充除数为0校验）
     */
    @GetMapping("/mod")
    public Object mod(@RequestParam double angle1, @RequestParam double angle2) {
        try {
            if (Math.abs(angle2) < 1e-10) {
                return "取余运算失败：除数不能为0";
            }
            double result = MathUtil.mod(angle1, angle2);
            return new BigDecimal(result).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return "取余运算失败：" + e.getMessage();
        }
    }

    /**
     * 指数运算（补充合法性校验）
     */
    @GetMapping("/pow")
    public Object pow(@RequestParam double angle1, @RequestParam double angle2) {
        try {
            // 0的负数次方不合法
            if (Math.abs(angle1) < 1e-10 && angle2 < 0) {
                return "指数运算失败：0不能取负数次方";
            }
            double result = MathUtil.pow(angle1, angle2);
            // 处理无穷大/NaN情况
            if (Double.isInfinite(result) || Double.isNaN(result)) {
                return "指数运算失败：结果超出范围";
            }
            return new BigDecimal(result).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return "指数运算失败：" + e.getMessage();
        }
    }

    /**
     * 对数运算（核心修复：底数/真数合法性校验）
     */
    @GetMapping("/log")
    public Object log(@RequestParam double angle1, @RequestParam double angle2) {
        try {
            // 真数必须>0，底数必须>0且≠1
            if (angle1 <= 1e-10) {
                return "对数运算失败：真数必须大于0";
            }
            if (angle2 <= 1e-10 || Math.abs(angle2 - 1) < 1e-10) {
                return "对数运算失败：底数必须大于0且不等于1";
            }
            double result = MathUtil.log(angle1, angle2);
            return new BigDecimal(result).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return "对数运算失败：" + e.getMessage();
        }
    }

    /**
     * 开方运算（补充合法性校验）
     */
    @GetMapping("/sqrt")
    public Object sqrt(@RequestParam double angle1, @RequestParam double angle2) {
        try {
            // 偶次根号下不能为负
            if (angle1 < -1e-10 && (angle2 % 2) < 1e-10) {
                return "开方运算失败：偶次根号下不能为负数";
            }
            double result = MathUtil.sqrt(angle1, angle2);
            if (Double.isInfinite(result) || Double.isNaN(result)) {
                return "开方运算失败：结果超出范围";
            }
            return new BigDecimal(result).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return "开方运算失败：" + e.getMessage();
        }
    }

    /**
     * 正弦运算（弧度/角度兼容，补充异常处理）
     */
    @GetMapping("/sin")
    public Object sin(@RequestParam double angle1) {
        try {
            double result = MathUtil.sin(angle1);
            return new BigDecimal(result).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return "正弦运算失败：" + e.getMessage();
        }
    }

    /**
     * 余弦运算
     */
    @GetMapping("/cos")
    public Object cos(@RequestParam double angle1) {
        try {
            double result = MathUtil.cos(angle1);
            return new BigDecimal(result).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return "余弦运算失败：" + e.getMessage();
        }
    }

    /**
     * 正切运算（补充特殊角度校验：90°/270°等）
     */
    @GetMapping("/tan")
    public Object tan(@RequestParam double angle1) {
        try {
            // 处理tan(π/2 + kπ)的无意义情况（弧度）
            double radian = angle1;
            if (Math.abs(radian % Math.PI - Math.PI/2) < 1e-10) {
                return "正切运算失败：该角度无意义（tan(π/2 + kπ)不存在）";
            }
            double result = MathUtil.tan(angle1);
            return new BigDecimal(result).setScale(6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return "正切运算失败：" + e.getMessage();
        }
    }

    /**
     * 向上取整
     */
    @GetMapping("/ceil")
    public Object ceil(@RequestParam double angle1) {
        try {
            return MathUtil.ceil(angle1);
        } catch (Exception e) {
            return "向上取整失败：" + e.getMessage();
        }
    }

    /**
     * 向下取整
     */
    @GetMapping("/floor")
    public Object floor(@RequestParam double angle1) {
        try {
            return MathUtil.floor(angle1);
        } catch (Exception e) {
            return "向下取整失败：" + e.getMessage();
        }
    }

    /**
     * 四舍五入
     */
    @GetMapping("/round")
    public Object round(@RequestParam double angle1) {
        try {
            return MathUtil.round(angle1);
        } catch (Exception e) {
            return "四舍五入失败：" + e.getMessage();
        }
    }

    /**
     * 数值比较（等于）
     */
    @GetMapping("/isEqual")
    public Object isEqual(@RequestParam double angle1, @RequestParam double angle2) {
        try {
            // 浮点数精度容错比较
            return Math.abs(angle1 - angle2) < 1e-10;
        } catch (Exception e) {
            return "数值比较失败：" + e.getMessage();
        }
    }

    // ===================== 历史记录接口（修复异常+补充时间字段） =====================
    /**
     * 保存计算记录（核心修复：异常处理、参数校验、补充创建时间）
     */
    // 保存计算记录（核心修复：异常处理、参数校验、补充创建时间）
    @GetMapping("/save")
    public String save(
            @RequestParam String expression,
            @RequestParam String result, // 修改为String类型
            @RequestParam Integer user_id
    ){
        try {
            // 参数校验
            if (expression == null || expression.trim().isEmpty()) {
                return "保存失败：计算式不能为空";
            }
            // 补充创建时间（如果表中create_time字段未设置自动生成，手动插入）
            String sql = "insert into calc_record(expression,result,create_time,user_id) values(?,?,?,?)";
            jdbcTemplate.update(sql, expression.trim(), result, LocalDateTime.now(), user_id);
            return "保存成功";
        } catch (Exception e) {
            // 捕获数据库异常，返回友好提示
            return "保存失败：" + e.getMessage();
        }
    }

    /**
     * 查询历史记录（修复空记录展示、异常处理）
     */
    @GetMapping("/query")
    public String query(@RequestParam Integer user_id){
        try {
            String sql ="select * from calc_record where user_id=? order by id desc";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,user_id);

            StringBuilder sb = new StringBuilder();
            sb.append("==================== 计算历史记录 ====================\n");

            if (list.isEmpty()) {
                sb.append("暂无历史记录\n");
                return sb.toString();
            }

            for (Map<String, Object> map : list) {
                sb.append("序号：").append(map.get("id")).append("\n");
                sb.append("计算式：").append(map.get("expression")).append("\n");
                sb.append("结果：").append(map.get("result")).append("\n");
                sb.append("时间：").append(map.get("create_time")).append("\n");
                sb.append("----------------------------------------------------\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "查询历史记录失败：" + e.getMessage();
        }
    }

    // ===================== 公式管理接口（增强异常处理） =====================
    /**
     * 保存自定义公式
     */
    @GetMapping("/saveFormula")
    public String saveFormula(
            @RequestParam String name,
            @RequestParam String content,
            @RequestParam Integer user_id
    ){
        try {
            // 参数校验
            if (name == null || name.trim().isEmpty()) {
                return "保存失败：公式名称不能为空";
            }
            if (content == null || content.trim().isEmpty()) {
                return "保存失败：公式内容不能为空";
            }
            String sql = "insert into formula(formula_name,formula_content,user_id) values(?,?,?)";
            jdbcTemplate.update(sql, name.trim(), content.trim(),user_id);
            return "保存成功";
        } catch (Exception e) {
            return "保存公式失败：" + e.getMessage();
        }
    }

    /**
     * 查询公式列表（修复空列表展示）
     */
    @GetMapping("/listFormulas")
    public String listFormulas(@RequestParam Integer user_id){
        try {
            String sql = "select * from formula where user_id=? order by id desc";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,user_id);

            StringBuilder sb = new StringBuilder();
            sb.append("==================== 公式列表 ====================\n");

            if (list.isEmpty()) {
                sb.append("暂无自定义公式\n");
                return sb.toString();
            }

            for (Map<String, Object> map : list) {
                sb.append("序号：").append(map.get("id")).append("\n");
                sb.append("公式名称：").append(map.get("formula_name")).append("\n");
                sb.append("公式内容：").append(map.get("formula_content")).append("\n");
                sb.append("----------------------------------------------------\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "查询公式列表失败：" + e.getMessage();
        }
    }

    /**
     * 执行自定义公式（核心修复：异常捕获、参数适配）
     */
    @GetMapping("/calcFormula")
    public String calcFormula(
            @RequestParam String name,
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
        try {
            // 参数校验
            if (name == null || name.trim().isEmpty()) {
                return "计算公式失败：公式名称不能为空";
            }
            // 调用公式计算工具类，捕获工具类异常
            String result = mathUtil.calcFormula(name.trim(), x1, x2, x3, x4, x5, x6, x7, x8, x9);
            // 校验计算结果是否合法
            if (result == null || result.trim().isEmpty()) {
                return "计算公式失败：未获取到有效结果";
            }
            return result;
        } catch (Exception e) {
            return "计算公式失败：" + e.getMessage();
        }
    }
}