package com.ems.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserDto {

    private Long userId;

    private String userName;

    private String email;

    private String password;

    private String role;

    private Boolean active;
}