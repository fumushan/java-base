package com.base.aop;

import java.lang.reflect.Method;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpringExpressionUtils {

	public static <T> T parseValue(String key, Method method, Object[] args, Class<T> resultType) {

		StandardEvaluationContext context = new StandardEvaluationContext();
		Expression expression = getExpression(key, method, args, context);
		if (expression != null) {
			return expression.getValue(context, resultType);
		}
		return null;
	}

	public static Expression getExpression(String key, Method method, Object[] args, EvaluationContext context) {

		try {
			// 获取被拦截方法参数名列表(使用Spring支持类库)
			LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
			String[] paramters = discoverer.getParameterNames(method);
			// 使用SPEL进行key的解析
			ExpressionParser parser = new SpelExpressionParser();
			// 把方法参数放入SPEL上下文中
			for (int i = 0; i < paramters.length; i++) {
				context.setVariable(paramters[i], args[i]);
			}
			return parser.parseExpression(key);
		} catch (ParseException e) {
			return null;
		}
	}
}
