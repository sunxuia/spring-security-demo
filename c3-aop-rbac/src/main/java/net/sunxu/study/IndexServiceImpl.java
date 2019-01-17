package net.sunxu.study;

import net.sunxu.study.rbac.Resource;
import org.springframework.stereotype.Service;

@Service
public class IndexServiceImpl implements IndexService {

    @Override
    public void withResourceInInterface() {

    }

    @Resource("service.implement")
    @Override
    public void withResourceInImplement() {

    }
}
