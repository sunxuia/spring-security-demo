package net.sunxu.study.rbac;

import net.sunxu.study.c0.CustomUserDetails;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * 进行权限判断的拦截类
 */
@Aspect
@Component
public class RbacAspect {

    private Logger logger = LoggerFactory.getLogger(RbacAspect.class);

    @Autowired
    private ResourceService resourceService;

    // @Resources 写不写都可以
    @Before("(@within(net.sunxu.study.rbac.Resource)" +
            " || @annotation(net.sunxu.study.rbac.Resource))" +
            " && target(target)")
    private void accessAuthorization(JoinPoint point, Object target) {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Class<?> targetClass = targetMethod.getDeclaringClass();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> availableResources;
        if ("anonymousUser".equals(principal)) {
            logger.info("anonymousUser[{}] access method [{}#{}]",
                    getIpAddress(), targetClass, targetMethod.getName());
            availableResources = resourceService.findResources("ROLE_ANONYMOUS");
        } else {
            CustomUserDetails user = (CustomUserDetails) principal;
            logger.info("user [{}][{}] access resource [{}#{}]",
                    user.getUsername(), getIpAddress(), targetClass, targetMethod.getName());
            String[] resourceName = user.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority).collect(toList())
                    .toArray(new String[user.getAuthorities().size()]);
            availableResources = resourceService.findResources(resourceName);
        }

        Resource[] classResources = targetClass.getAnnotationsByType(Resource.class);
        Resource[] methodResources = targetMethod.getAnnotationsByType(Resource.class);
        boolean hasAuthorization = false;
        if (methodResources.length == 0) {
            for (int i = 0; i < classResources.length && !hasAuthorization; i++) {
                hasAuthorization = availableResources.contains(classResources[i].value());
            }
        } else if (classResources.length == 0) {
            for (int i = 0; i < methodResources.length && !hasAuthorization; i++) {
                hasAuthorization = availableResources.contains(methodResources[i].value());
            }
        } else {
            for (int i = 0; i < classResources.length && !hasAuthorization; i++) {
                for (int j = 0; j < methodResources.length && !hasAuthorization; i++) {
                    hasAuthorization = availableResources.contains(
                            classResources[i].value() + "." + methodResources[i].value());
                }
            }
        }
        if (!hasAuthorization) {
            throw new AccessDeniedException("NOT_AUTHORIZED");
        }
    }

    public String getIpAddress() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (Strings.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (Strings.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (Strings.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (Strings.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (Strings.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
