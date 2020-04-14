package com.apap.tu08.service;

import com.apap.tu08.model.UserRoleModel;
import com.apap.tu08.repository.UserRoleDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * FlightServiceImpl
 */
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRoleDB userRoleDB;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRoleModel user = userRoleDB.findByUsername(username);

        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<GrantedAuthority>();
        grantedAuthoritySet.add(new SimpleGrantedAuthority(user.getRole()));

        return new User(user.getUsername(), user.getPassword(), grantedAuthoritySet);
    }
}