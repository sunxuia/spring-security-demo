package net.sunxu.study.c1;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 过滤器. 是对返回结果的过滤
 */
@RequestMapping("/filter")
@RestController
public class FilterController {
    /**
     * 没有过滤器. 直接返回.
     *
     * @return
     */
    @GetMapping({"", "/"})
    public List<Integer> noFilter() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    /**
     * 过滤返回结果
     * 过滤只留下奇数值
     * 对于需要删除的值, 程序会调用remove() 方法, 所以返回的集合不能是只读集合.
     *
     * @return
     */
    @GetMapping("/get-even")
    @PostFilter("filterObject % 2 == 1")
    public List<Integer> getEven() {
        return new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    }

    /**
     * 过滤输入参数, 只保留奇数
     * 如果只有一个参数, filterTarget 也可以不写
     *
     * @param list 查询参数
     * @return
     */
    @GetMapping("/filter-even")
    @PreFilter(value = "filterObject % 2 == 1", filterTarget = "list")
    public List<Integer> filterEven(@RequestParam("list") List<Integer> list) {
        return list;
    }
}
