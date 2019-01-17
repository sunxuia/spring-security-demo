package net.sunxu.study;

import net.sunxu.study.rbac.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/class-resource")
@Resource("resource")
public class ResourceController {

    @GetMapping("/no-method-resource")
    public String noMethodResource() {
        return "noMethodResource";
    }

    @GetMapping("with-method-resource")
    @Resource("with-method-resource")
    public String withMethodResource() {
        return "withMethodResource";
    }
}
