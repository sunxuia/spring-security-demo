package net.sunxu.study.c2;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Override
    public Set<String> getAccessibleUrls(Collection<String> roleNames) {
        Set<String> res = new HashSet<>();
        res.add("/");
        if (roleNames.contains("ROLE_ANONYMOUS")) {
            res.add("/anonymous-access");
        } else {
            res.add("/authenticated-access");
            if (roleNames.contains("ROLE_ADMIN")) {
                res.add("/role-admin-access");
            }
        }
        return res;
    }
}
