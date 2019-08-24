package net.sunxu.study.c5;

import java.util.List;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/test")
    @PreAuthorize("isCustomUserDetails(principal)")
    public String testString() {
        return "test";
    }

    @GetMapping("/test-array")
    @PostFilter("customFilter(filterObject)")
    public List<String> testArray() {
        return List.of("a", "b", "c");
    }
}
