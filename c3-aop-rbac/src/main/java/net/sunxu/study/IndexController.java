package net.sunxu.study;

import net.sunxu.study.rbac.Resource;
import net.sunxu.study.rbac.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Autowired
    private IndexService indexService;

    @GetMapping("/no-resource-method")
    public String noResourceMethod() {
        return "noResourceMethod";
    }

    @GetMapping("/resource-for-anonymous")
    @Resource("index.anonymous")
    public String resourceForAnonymous() {
        return "resourceForAnonymous";
    }

    @GetMapping("/resource-for-authenticated")
    @Resource("index.authenticated")
    public String resourceForAuthenticated() {
        return "resourceForAuthenticated";
    }

    @GetMapping("/dual-resource")
    @Resource("index.dual.resource-i-have")
    @Resource("index.dual.resource-i-do-not-have")
    public String dualResources() {
        return "dualResources";
    }

    @GetMapping("/dual-resource-with-resources-annotation")
    @Resources({@Resource("index.dual.resource-i-have"), @Resource("index.dual.resource-i-do-not-have")})
    public String dualResourcesWithResourcesAnnotation() {
        return "dualResourcesWithResourcesAnnotation";
    }

    // 因为Spring AOP 默认的CGLIB 实现是针对类的, 所以在接口上使用资源没有效果.
    @GetMapping("/call-service-with-resource-in-interface")
    public void callServiceWithResourceInInterface() {
        indexService.withResourceInInterface();
    }

    // 在类上使用资源限制可以拦截
    @GetMapping("/call-service-with-resource-in-implement")
    public void callServiceWithResourceInImplement() {
        indexService.withResourceInImplement();
    }
}
