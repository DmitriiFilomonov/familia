package com.diplom.beautyshop;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.diplom.beautyshop.core.WorkerDto;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(WorkerDto user) {
        return new JwtUser(
                user.pkWorker,
                user.login,
                user.pass,
                mapToGrantedAuthorities(user), true
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(WorkerDto user) {
    	List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
    	roles.add(new SimpleGrantedAuthority("ADMIN"));
        return roles;
    }
}

