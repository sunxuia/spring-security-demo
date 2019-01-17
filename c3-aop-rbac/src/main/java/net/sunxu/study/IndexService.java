package net.sunxu.study;

import net.sunxu.study.rbac.Resource;

public interface IndexService {

    @Resource("service.interface")
    void withResourceInInterface();

    void withResourceInImplement();
}
