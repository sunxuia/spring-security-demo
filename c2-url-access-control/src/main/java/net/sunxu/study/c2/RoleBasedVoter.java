package net.sunxu.study.c2;

import net.sunxu.study.c0.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基于角色的权限投票者
 * <p>
 * 认证失败的情况 :
 * 这个投票的vote 方法会在 {@link org.springframework.security.access.intercept.AbstractSecurityInterceptor#beforeInvocation(java.lang.Object)}
 * 调用的 {@link UnanimousBased#decide(org.springframework.security.core.Authentication, java.lang.Object, java.util.Collection)}
 * 中被调用, 如果投票结果是-1 (否决) 则在UnanimousBased 的decode 方法中抛出异常{@link AccessDeniedException}
 * 这个最终会在 {@link ExceptionTranslationFilter#handleSpringSecurityException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain, java.lang.RuntimeException)}
 * 中被处理, 没有登录的会被跳转到登录页面, 登录的跳到 {@link AccessDeniedException} 的异常处理
 */
public class RoleBasedVoter implements AccessDecisionVoter<Object> {

    @Autowired
    private RoleService service;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * 权限投票
     *
     * @param authentication 当前用户的认证信息
     * @param object         要检查权限的对象, 在这个是FilterInvocation 对象
     * @param attributes     这里是 WebExpressionConfigAttribute 集合对象
     * @return 投片结果
     */
    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        if (authentication == null) {
            return ACCESS_DENIED;
        }
        Set<String> roleNames = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        Set<String> urls = service.getAccessibleUrls(roleNames);
        FilterInvocation fi = (FilterInvocation) object;
        AntPathMatcher matcher = new AntPathMatcher();
        for (var url : urls) {
            if (matcher.match(url, fi.getRequestUrl())) {
                return ACCESS_GRANTED;
            }
        }
        return ACCESS_DENIED;
    }
}
