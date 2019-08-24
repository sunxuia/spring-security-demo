package net.sunxu.study.c5;

import java.lang.reflect.Array;
import java.util.ArrayList;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.expression.AbstractSecurityExpressionHandler;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

/**
 * 自定义的权限验证handler,
 * 对于验证请求指定 createSecurityExpressionRoot() 进行验证.
 * 对于 过滤按顺序请求执行 createSecurityExpressionRoot() -> filter() -> setReturnObject() 方法.
 * 默认是 {@link DefaultMethodSecurityExpressionHandler}, 这里就是根据它改过来的.
 */
public class CustomMethodSecurityExpressionHandler
        extends AbstractSecurityExpressionHandler<MethodInvocation> implements MethodSecurityExpressionHandler {

    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

//    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultSecurityParameterNameDiscoverer();

    /**
     * 针对每次请求创建验证表达式
     *
     * @param authentication
     * @param invocation
     * @return
     */
    @Override
    protected SecurityExpressionOperations createSecurityExpressionRoot(
            Authentication authentication, MethodInvocation invocation) {
        CustomSecurityExpressionRoot root = new CustomSecurityExpressionRoot(authentication);
        root.setThis(invocation.getThis());
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        root.setDefaultRolePrefix("ROLE_");

        return root;

    }

    /**
     * 过滤返回结果(数组/ 集合). 只有在方法返回的结果是数组/集合的时候才会执行过滤.
     *
     * @param filterTarget     要过滤的数组/ 集合对象
     * @param filterExpression 过滤表达式, 就是在@PostFilter/ @PreFilter 中的数组
     * @param ctx              context
     * @return
     */
    @Override
    public Object filter(Object filterTarget, Expression filterExpression, EvaluationContext ctx) {
        // 就是上面一个方法创建的对象
        CustomSecurityExpressionRoot root = (CustomSecurityExpressionRoot) ctx.getRootObject().getValue();
        var resultList = new ArrayList();
        if (filterTarget instanceof Iterable) {
            for (Object filterObject : (Iterable) filterTarget) {
                // 设置过滤时要验证的对象(就是表达式中的 filterObject)
                root.setFilterObject(filterObject);
                // 获得验证结果, 如果验证通过则加入结果中
                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    resultList.add(filterObject);
                }
            }
            return resultList;
        }
        if (filterTarget.getClass().isArray()) {
            for (int i = 0, len = Array.getLength(filterTarget); i < len; i++) {
                Object filterObject = Array.get(filterTarget, i);
                root.setFilterObject(filterObject);
                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    resultList.add(filterObject);
                }
            }
            var res = Array.newInstance(filterExpression.getClass().getComponentType(), Array.getLength(filterTarget));
            for (int i = 0; i < resultList.size(); i++) {
                Array.set(res, i, resultList.get(i));
            }
            return res;
        }
        throw new RuntimeException("not support");
    }

    /**
     * 用来传入最终返回过滤的结果给这个对象, 如果需要的话
     *
     * @param returnObject
     * @param ctx
     */
    @Override
    public void setReturnObject(Object returnObject, EvaluationContext ctx) {
        System.out.println("result value is " + returnObject);
    }
}
