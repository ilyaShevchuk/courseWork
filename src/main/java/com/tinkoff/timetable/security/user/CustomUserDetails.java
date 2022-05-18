package com.tinkoff.timetable.security.user;

import com.tinkoff.timetable.model.entity.Teacher;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Setter
@Getter
public class CustomUserDetails implements UserDetails {
    private final String defaultRole = "ROLE_ADMIN";

    private String login;

    private String password;

    private Collection<? extends GrantedAuthority> grantedAuthorities;


    public static CustomUserDetails fromTeacherToCustomUserDetails(Teacher teacher) {
        CustomUserDetails user = new CustomUserDetails();
        user.login = teacher.getLogin();
        user.password = teacher.getPassword();
        user.grantedAuthorities = teacher.getRole().getAuthorities();
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
