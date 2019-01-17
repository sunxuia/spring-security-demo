package net.sunxu.study.c2;

import java.util.Collection;
import java.util.Set;

/**
 * 通过这个接口获取可访问url 内容
 */
public interface RoleService {
    Set<String> getAccessibleUrls(Collection<String> roleNames);
}
