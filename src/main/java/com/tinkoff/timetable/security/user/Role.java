package com.tinkoff.timetable.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN(Set.of(Permission.CREATE, Permission.READ, Permission.DELETE, Permission.REFACTOR_ALL, Permission.TEACHER_CONTROL)),
    TEACHER(Set.of(Permission.READ, Permission.CREATE));

    private final Set<Permission> permissionSet;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissionSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
