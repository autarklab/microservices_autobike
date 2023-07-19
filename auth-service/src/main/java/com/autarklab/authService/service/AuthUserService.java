package com.autarklab.authService.service;

import com.autarklab.authService.dto.AuthUserDTO;
import com.autarklab.authService.dto.TokenDTO;
import com.autarklab.authService.entity.AuthUser;
import com.autarklab.authService.repository.AuthUserRepository;
import com.autarklab.authService.security.JwtProvider;
import com.autarklab.authService.security.PasswordEncoderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserService {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthUserService(AuthUserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public AuthUser save(AuthUserDTO dto) {
        Optional<AuthUser> user = userRepository.findByUserName(dto.getUserName());
        if(user.isPresent()) {
            return null;
        }

        String password = passwordEncoder.encode(dto.getPassword());
        AuthUser authUser = AuthUser.builder()
                .userName(dto.getUserName())
                .password(password)
                .build();
        System.out.println("==== PASO 2 =====");
        System.out.println(authUser);
        return this.userRepository.save(authUser);
    }

    public TokenDTO validate(String token) {
        if(!jwtProvider.isValid(token))
            return null;
        String username = jwtProvider.getUsername(token);
        if(userRepository.findByUserName(username).isEmpty())
            return null;
        return new TokenDTO(token);
    }
}
