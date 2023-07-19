package com.autarklab.authService.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthUserDTO {

    private String userName;
    private String password;
}
