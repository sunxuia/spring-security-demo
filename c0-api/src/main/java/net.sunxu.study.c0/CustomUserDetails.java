package net.sunxu.study.c0;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
public class CustomUserDetails implements UserDetails {
    private UserModel user;

    private List<GrantedAuthority> authorities;

    public CustomUserDetails(UserModel user, String... authorities) {
        this.user = user;
        this.authorities = Stream.of(authorities)
                .map(a -> "ROLE_" + a) //角色名判断的时候都会默认加上ROLE_ 的前缀
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getUserState() == UserState.NORMAL;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getUserState() != UserState.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserModel getUser() {
        return user;
    }
}
