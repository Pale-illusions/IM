package com.iflove.common.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote spring el表达式解析工具类
 */
public class SPELUtils {
    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 解析SPEL表达式
     * @param method 方法
     * @param args 方法参数
     * @param spEL SPEL表达式
     * @return 解析结果
     */
    public static String parseSPEL(Method method, Object[] args, String spEL) {
        // 解析参数名
        String[] params = Optional.ofNullable(parameterNameDiscoverer.getParameterNames(method)).orElse(new String[]{});
        // el解析需要的上下文对象
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            // 填入所有的参数
            context.setVariable(params[i], args[i]);
        }
        Expression expression = parser.parseExpression(spEL);
        return expression.getValue(context, String.class);
    }

    /**
     * 获取方法全限定名
     * @param method 方法
     * @return 方法全限定名
     */
    public static String getMethodKey(Method method) {
        return method.getDeclaringClass() + "#" + method.getName();
    }
}
