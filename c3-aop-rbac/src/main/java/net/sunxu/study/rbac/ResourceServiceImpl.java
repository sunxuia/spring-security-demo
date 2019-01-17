package net.sunxu.study.rbac;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Override
    public Set<String> findResources(String... role) {
        Set<String> res = new HashSet<>();
        if ("ROLE_ANONYMOUS".equalsIgnoreCase(role[0])) {
            res.add("index.anonymous");
        } else {
            res.add("index.authenticated");
        }
        res.add("index.dual.resource-i-have");
        res.add("resource.with-method-resource");
        return res;
    }
}
