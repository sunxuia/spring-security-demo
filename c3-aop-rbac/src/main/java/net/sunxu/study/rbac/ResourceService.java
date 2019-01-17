package net.sunxu.study.rbac;

import java.util.Set;

/**
 * 用于动态载入角色和资源的服务
 */
public interface ResourceService {
    /**
     * 根据角色获得资源名设置
     * @param role 要查询的角色名
     * @return 对应的资源名. 没有返回一个空的集合.
     */
    Set<String> findResources(String... role);
}
