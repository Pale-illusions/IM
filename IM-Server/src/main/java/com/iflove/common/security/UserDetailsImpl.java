package com.iflove.common.security;

import com.iflove.api.user.domain.entity.Role;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.enums.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final User user;
    private final Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(role.getRole())
                .map(role -> new SimpleGrantedAuthority(UserRoleEnum.of(role)))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }
}
