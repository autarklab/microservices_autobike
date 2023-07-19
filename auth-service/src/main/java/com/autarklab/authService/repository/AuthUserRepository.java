package com.autarklab.authService.repository;

import com.autarklab.authService.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {

    Optional<AuthUser> findByUserName(String username);
}
