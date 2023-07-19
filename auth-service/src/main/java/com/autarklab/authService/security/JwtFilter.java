package com.autarklab.authService.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService detailsService;

    @Autowired
    public JwtFilter(JwtProvider jwtProvider, UserDetailsService detailsService) {
        this.jwtProvider = jwtProvider;
        this.detailsService = detailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. validate valid header Authorization
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Validate JWT
        String jwt = authHeader.split(" ")[1].trim();
        if (!this.jwtProvider.isValid(jwt)){
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Load user from UserDetailsService
        String username = this.jwtProvider.getUsername(jwt);
        User user = (User) this.detailsService.loadUserByUsername(username);

        // 4. Load user in security context
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword()
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        System.out.println("======== reviewing the token =========");
        System.out.println(authenticationToken);
        filterChain.doFilter(request,response);

    }
}
