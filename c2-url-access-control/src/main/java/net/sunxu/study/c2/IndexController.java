package net.sunxu.study.c2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/anonymous-access")
    public String anonymousAccess() {
        return "anonymousAccess";
    }

    @GetMapping("/authenticated-access")
    public String authenticatedAccess() {
        return "authenticatedAccess";
    }

    @GetMapping("/role-admin-access")
    public String roleAdminAccess() {
        return "roleAdminAccess";
    }
}
