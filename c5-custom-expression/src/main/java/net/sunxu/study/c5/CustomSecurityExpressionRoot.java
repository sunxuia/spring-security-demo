package net.sunxu.study.c5;

import net.sunxu.study.c0.CustomUserDetails;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * 提供验证表达式(方法), 和设置验证对象的方法, 每次请求都会创建.
 * 复制的 MethodSecurityExpressionRoot
 */
public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;

    CustomSecurityExpressionRoot(Authentication a) {
        super(a);
    }

    public boolean isCustomUserDetails(Object principal) {
        return principal instanceof CustomUserDetails;
    }

    public boolean customFilter(Object obj) {
        return !"c".equals(obj);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return target;
    }
}
