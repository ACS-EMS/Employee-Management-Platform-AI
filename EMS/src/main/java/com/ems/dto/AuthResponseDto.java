package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDto {

    private Long userId;
    private String userName;
    private String email;
    private String role;
    private String token;
}