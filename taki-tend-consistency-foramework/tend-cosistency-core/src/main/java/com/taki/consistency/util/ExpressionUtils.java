package com.taki.consistency.util;

import com.taki.consistency.model.ConsistencyTaskInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @ClassName ExpressionUtils
 * @Description el 表达式 解析的工具类 用于解析配置中的表达式
 * @Author Long
 * @Date 2022/9/3 13:39
 * @Version 1.0
 */
@Slf4j
public class ExpressionUtils {

    public static  final   String START_MARK = "\\$\\{";

    public static final    String RESULT_FLAG = "true";


    /*** 
     * @description:  重写表达式
     * @param alertExpression 告警表达式
     * @return  java.lang.String
     * @author Long
     * @date: 2022/9/3 13:41
     */ 
    public  static String rewriteExpr(String  alertExpression){
        String exprExpr = StringUtils.replace(alertExpression,"executeTimes","#taskInstance.executeTimes");

        StringJoiner exprJoiner = new StringJoiner("","${","}");
        exprJoiner.add(exprExpr);
        return exprJoiner.toString();
        
    }


    /***
     * @description: 获取指定EL表达式在指定对象中的值
     * @param expr spring el 表达式
     * @param  dataMap 数据集合 ref -> data 对象引用 -》 data
     * @return  java.lang.String
     * @author Long
     * @date: 2022/9/3 13:44
     */
    public static   String readExpr(String  expr , Map<String,Object>  dataMap){
        try {
            expr = formatExpr(expr);

            //表达式的上下文
            EvaluationContext context = new StandardEvaluationContext();

            //为了让表达式可以访问改对象，先把对象放到上下文
            dataMap.forEach((key,value)->{
                // key - ref value ->iterator.next().getValue();
                context.setVariable(key,value);
            });

            //executeTimes <= xx && executeTimes >= xx 跟我们 dataMap 里的一些数据参数，executeTimes
            //去做一个比较，看看executeTimes 是否在我们的表达式范围内
            SpelExpressionParser parser = new SpelExpressionParser();

            Expression expression = parser.parseExpression(expr,new TemplateParserContext());

            return expression.getValue(context,String.class);

        }catch (Exception e){
            log.info("解析表达式{}时，发送异常",expr,e);

            return "";
        }

    }

    /*** 
     * @description:  对表达式进行格式化 ${xxx.name} -> #{xxx.name}
     * @param expr 表达式
     * @return  java.lang.String
     * @author Long
     * @date: 2022/9/3 13:46
     */ 
    private static String formatExpr(String expr) {

        return expr.replaceAll(START_MARK,"#{");

    }

    /*** 
     * @description:  构造数据map
     * @param object 要访问的数据
     * @return
     * @author Long
     * @date: 2022/9/3 13:55
     */ 
    public static Map<String,Object> buildDataMap(Object object){
        Map<String,Object > map = new HashMap<>(1);

        map.put("taskInstance",object);

        return map;
    }

    /**
     * 对表达式格式化 ${xxx.name} -> #{xxx.name}
     * @param args
     */
    public static void main(String[] args) {

        ConsistencyTaskInstance instance = ConsistencyTaskInstance.builder()
                .executeTimes(6).build();

        Map<String,Object> dataMap = new HashMap<>(2);
        dataMap.put("taskInstance",instance);

        String expr ="executeTimes > 1 &&  executeTimes < 5";
        String  executeTimesExpr = StringUtils.replace(expr,"executeTimes","#taskInstance.executeTimes");
        System.out.println("executeTimesExpr"+executeTimesExpr);

        System.out.println(""+readExpr("${" + executeTimesExpr +"}",dataMap));

        String expr2 = "executeTimes % 2 == 0";
        String executeTimesExpr2 = StringUtils.replace(expr2,"executeTimes","#taskInstance.executeTimes");
        System.out.println(executeTimesExpr2);

        System.out.println(readExpr("${" + executeTimesExpr2 +"}",dataMap));


        System.out.println(readExpr(rewriteExpr(expr),dataMap));





    }
}
