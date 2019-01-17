package net.sunxu.study.c1;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public String accessDeniedExceptionHandler(HttpServletRequest req, HttpServletResponse res,
                                               AccessDeniedException err) {
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return String.format("url [%s] 没有权限访问 : %s", req.getRequestURI(), err.getMessage());
    }
}
