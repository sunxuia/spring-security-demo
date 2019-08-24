package net.sunxu.study.c1;

import java.io.Serializable;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * spring security hasPermission 表达式的自定义验证
 */
@Component
public class MyPermissionEvaluator implements PermissionEvaluator {

    /**
     * hasPermission(targetDomainObject, permission) 表达式的执行方法
     *
     * @param authentication     认证信息, 这个在hasPermission 表达式中不用写出来.
     * @param targetDomainObject hasPermission 的第1 个参数, 表示应当要检查权限的领域对象.
     * @param permission         hasPermission 的第2 个参数, 表示要对对象的这个权限进行检查.
     * @return 权限验证是否通过
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return canAccess(authentication.getPrincipal(), (String) targetDomainObject, (String) permission);
    }

    private boolean canAccess(Object principal, String resource, String permission) {
        if (principal.equals("anonymousUser") && permission.equals("read")) {
            return true;
        }
        return false;
    }

    /**
     * hasPermission(targetId, targetType, permission) 表达式的执行方法.
     * 针对无法取得要检查权限的领域对象, 使用标识符(targetId) 和数据类型(targetType) 来确定对象的方法.
     *
     * @param authentication 认证信息. 这个在hasPermission 表达式中不用写出来.
     * @param targetId       hasPermissin 的第1 个参数, 表示要检查权限的领域对象的标识符( 一般是Long).
     * @param targetType     hasPermission 的第2 个参数, 表示要检查权限的领域对象的数据类型.
     * @param permission     hasPermission 的第3 个参数, 表示要对对象的这个权限进行检查.
     * @return 权限验证是否通过
     */
    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId, String targetType, Object permission) {
        String targetDomainObject = getResource((Long) targetId, targetType);
        return hasPermission(authentication, targetDomainObject, permission);
    }

    private String getResource(Long id, String type) {
        return "*";
    }
}
