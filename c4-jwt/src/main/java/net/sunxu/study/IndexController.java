package net.sunxu.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class IndexController {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    // 获得用户名
    @GetMapping("/")
    public String index(Authentication authentication) {
        return authentication == null ? "" : ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    //权限验证, 必须要登录, 因为没有session 只能使用jwt
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/require-auth")
    public String requireAuth() {
        return "ok";
    }

    // 请求这个获得jwt
    @GetMapping("/auth")
    public String auth(@RequestParam String name) {
        return jwtTokenUtils.createJwt(name);
    }
}
