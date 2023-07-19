package com.autarklab.authService.controller;

import com.autarklab.authService.dto.AuthUserDTO;
import com.autarklab.authService.dto.TokenDTO;
import com.autarklab.authService.entity.AuthUser;
import com.autarklab.authService.security.JwtProvider;
import com.autarklab.authService.service.AuthUserService;
import com.autarklab.authService.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager manager;
    private final JwtProvider jwtProvider;
    private final AuthUserService authUserService;

    @Autowired
    public AuthController(AuthenticationManager manager,
                          JwtProvider jwtProvider, AuthUserService authUserService) {
        this.manager = manager;
        this.jwtProvider = jwtProvider;
        this.authUserService = authUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AuthUserDTO dto) {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(
                dto.getUserName(), dto.getPassword()
        );

        Authentication authentication = this.manager.authenticate(login);

        System.out.println("*******  VALIDATING  *****");
        System.out.println(authentication.isAuthenticated());
        System.out.println(authentication.getPrincipal());

        String jwt = jwtProvider.create(dto.getUserName());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }

    @PostMapping("/register")
    public ResponseEntity<AuthUser> register(@RequestBody AuthUserDTO dto){
        System.out.println("==== PASO 1 =====");
        System.out.println(dto);
        AuthUser authUser = authUserService.save(dto);

        System.out.println("==== PASO 3 =====");
        System.out.println(authUser);

        if (authUser == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(authUser);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDTO> validate(@RequestBody String token){
        TokenDTO tokenDTO = authUserService.validate(token);
        if (tokenDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(tokenDTO);
    }
}
