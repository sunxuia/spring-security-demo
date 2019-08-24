package net.sunxu.study.c1;

import net.sunxu.study.c0.CustomUserDetails;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 不同的权限可以访问到的方法不同
 * 权限控制的相关方法和对象都在 {@link org.springframework.security.access.expression.SecurityExpressionRoot} 中实现
 */
@RequestMapping("/authorize")
@RestController
public class AuthorizeController {

    /**
     * 不控制权限
     * 登录还是没登录都可以访问
     *
     * @return
     */
    @GetMapping({"/", ""})
    public String noAuthority() {
        return "noAuthority";
    }

    //region hasRole demo 角色判断
    // 通过用户是否有相应的角色来判断(输入的字符串会加上ROLE_ 前缀然后判断authorities 中是否包含这个字符串)
    // 类似的还有 hasAnyRole

    /**
     * 角色ANONYMOUS 权限控制 (未登录的用户只有一个ANONYOUS 权限)
     *
     * @return
     */
    @GetMapping("/has-role-anonymous")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public String hasRoleAnonymous() {
        return "hasRoleAnonymous";
    }

    /**
     * 角色ADMIN 权限控制
     * 需要登录后访问(登录用户有ADMIN 和NORMAL 的角色)
     *
     * @return
     */
    @GetMapping("/has-role-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String hasRoleAdmin() {
        return "hasRoleAdmin";
    }

    /**
     * 角色ROOT 权限控制
     * 没有人可以访问 (不论匿名用户还是登录用户都没有ROOT 角色)
     *
     * @return
     */
    @GetMapping("/has-role-root")
    @PreAuthorize("hasRole('ROOT')")
    public String hasRoleRoot() {
        return "rootAuthority";
    }
    //endregion

    //region hasAuthority demo 权限验证
    // 和hasRole 差不多, 不过不会加上ROLE_ 的前缀
    // 类似的还有hasAnyAuthority

    /**
     * 权限检查需要有权限名为 ROLE_ADMIN
     * 登录的用户可以通过
     *
     * @return
     */
    @GetMapping("/has-authority-admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String hasAuthorityAdmin() {
        return "hasAuthorityAdmin";
    }

    /**
     * 权限检查需要有权限名为 ROLE_ANONYMOUS
     * 没登录的用户可以通过, 登录的用户不能通过
     *
     * @return
     */
    @GetMapping("/has-authority-anonymous")
    @PreAuthorize("hasAuthority('ROLE_ANONYMOUS')")
    public String hasAuthorityAnonymous() {
        return "hasAuthorityAnonymous";
    }
    //endregion

    //region 其它判断 (用户状态相关的)

    /**
     * isAnonymous() 限制
     * 只有没登录的才可以访问
     *
     * @return
     */
    @GetMapping("/is-anonymous")
    @PreAuthorize("isAnonymous()")
    public String isAnonymous() {
        return "isAnonymous";
    }

    /**
     * isAutnenticated() 限制
     * 只有登录的才能访问
     *
     * @return
     */
    @GetMapping("/is-authenticated")
    @PreAuthorize("isAuthenticated()")
    public String isAuthenticated() {
        return "isAuthenticated";
    }

    /**
     * isFullyAuthenticated() 限制
     * 只有已登录且没有选择"记住我" 选项的用户才能访问
     *
     * @return
     */
    @GetMapping("/is-fully-authenticated")
    @PreAuthorize("isFullyAuthenticated()")
    public String isFullyAuthenticated() {
        return "isFullyAuthenticated";
    }

    /**
     * isRememberMe() 限制
     * 只有已经登录且选择了"记住我" 选项的用户才能访问
     *
     * @return
     */
    @GetMapping("/is-remember-me")
    @PreAuthorize("isRememberMe()")
    public String isRememberMe() {
        return "isRememberMe";
    }
    //endregion

    //region 对象的使用 在权限控制中可以使用的对象

    /**
     * 当前用户的principal 是字符串 "AnonymousUser"
     * 只有未登录用户才能访问
     *
     * @return
     */
    @GetMapping("/principal-is-anonymous-user")
    @PreAuthorize("principal == 'anonymousUser'")
    public String principalIsAnonymousUser() {
        return "principalIsAnonymousUser";
    }

    /**
     * 当前用户是已经登录的用户 ( 通过表单登录的用户的principal 是CustomUserDetails 对象)
     *
     * @return
     */
    @GetMapping("/principal-is-custom-user-details")
    @PreAuthorize("principal instanceof T(net.sunxu.study.c0.CustomUserDetails)")
    public String principalIsCustomUserDetails() {
        return "principalIsCustomUserDetails";
    }


    /**
     * 当前用户的用户名是admin 的才能访问
     *
     * @return
     */
    @GetMapping("/user-name-is-admin")
    @PreAuthorize("principal instanceof T(org.springframework.security.core.userdetails.UserDetails) " +
            "and principal.getUsername() == 'admin'")
    public String userNameIsAdmin() {
        return "userNameIsAdmin";
    }

    /**
     * 当前用户已经认证
     * 不论是登录用户还是匿名用户, 在经过spring security 的用户认证之前都是false, 之后都是true.
     * 所以这个不论是登录还是未登录用户都可以访问.
     *
     * @return
     */
    @GetMapping("/authentication-is-authenticated")
    @PreAuthorize("authentication.isAuthenticated()")
    public String authenticationIsAuthenticated() {
        return "authenticationIsAuthenticated";
    }

    /**
     * 判断一下当前用户的ip 地址是什么
     * 这个只要通过访问127.0.0.1 就可以, 单元测试的登录的用户 details 是空的, 所以会返回false, 单元测试的匿名登录的details 是有的.
     *
     * @return
     */
    @GetMapping("/authentication-details-is-from-127001")
    @PreAuthorize("authentication.getDetails() != null &&" +
            " authentication.getDetails().getRemoteAddress() == '127.0.0.1'")
    public String authenticationDetailsIsFrom127001() {
        return "authenticationDetailsIsFrom127001";
    }

    /**
     * hasPermssion 的权限检查. 通过检查认证用户对该对象是否具有相应的权限来判断是否可以访问.
     *
     * @return
     */
    @GetMapping("/has-permission-w-2-args")
    @PreAuthorize("hasPermission('hasPermissionW2Args', 'read')")
    public String hasPermissionW2Args() {
        return "hasPermissionW2Args";
    }

    /**
     * hasPermission 的权限检查. 通过标识符和类型获得对象, 然后检查用户对该对象是否具有权限来判断是否可以访问.
     * 通过"#参数名" 传入方法的参数.
     *
     * @return
     */
    @GetMapping("/has-permission-w-3-args")
    @PreAuthorize("hasPermission(#number, 'java.lang.String', 'read')")
    public String hasPermissionW3Args(Long number) {
        return "hasPermissionW3Args";
    }

    /**
     * 使用自定义的权限验证方法. 通过"@beanName.methodName(arguments)" 的方式调用方法进行验证.
     * 通过"#参数名" 传入方法的参数.
     *
     * @return
     */
    @GetMapping("/custom-method")
    @PreAuthorize("@authorizeController.customVerifyMethod(principal, #userName)")
    public String customMethod(@RequestParam String userName) {
        return "customMethod";
    }

    // 自定义的验证表达式, 方法返回true 表示权限验证通过, false 表示权限验证失败.
    public boolean customVerifyMethod(Object principal, String userName) {
        return principal != null
                && (principal instanceof String && principal.equals(userName)
                || (principal instanceof CustomUserDetails
                && ((CustomUserDetails) principal).getUsername().equals(userName)));
    }

    //endregion

    //region PostAuthorize
    // 这个在方法执行后才会执行其中的spring el 进行权限判断.

    /**
     * 返回这个方法执行的次数. 不论是否通过权限验证这个方法都会被执行.
     *
     * @return
     */
    @GetMapping("/post-authorize")
    @PostAuthorize("principal == 'anonymousUser'")
    public int postAuthorize() {
        return ++postAuthorizeVisitCount;
    }

    private int postAuthorizeVisitCount = 0;

    //endregion
}
