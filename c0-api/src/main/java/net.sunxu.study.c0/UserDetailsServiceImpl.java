package net.sunxu.study.c0;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setName(s);
        userModel.setCreateTime(new Date());
        userModel.setMailAddress("somone@somewhere.com");
        userModel.setPassword("{bcrypt}" + BCrypt.hashpw("123456", BCrypt.gensalt()));
        userModel.setUserState(UserState.NORMAL);
        if (s.equalsIgnoreCase("admin")) {
            return new CustomUserDetails(userModel, "NORMAL", "ADMIN");
        } else {
            return new CustomUserDetails(userModel, "NORMAL");
        }
    }
}
