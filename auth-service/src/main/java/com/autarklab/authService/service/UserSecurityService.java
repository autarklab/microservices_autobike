package com.autarklab.authService.service;

import com.autarklab.authService.entity.AuthUser;
import com.autarklab.authService.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {

    private final AuthUserRepository userRepository;

    @Autowired
    public UserSecurityService(AuthUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AuthUser authUser = this.userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found"));


        return User.builder()
                .username(authUser.getUserName())
                .password(authUser.getPassword())
                .build();

    }
}
