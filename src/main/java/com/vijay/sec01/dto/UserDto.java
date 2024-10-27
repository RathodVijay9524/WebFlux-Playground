package com.vijay.sec01.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String role;
    private String gender;
    private String mobileNumber;
}
