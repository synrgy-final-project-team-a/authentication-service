package com.synrgy.security.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginModel {
    @Email(message = "Email is not valid.")
    @NotEmpty(message = "email is required.")
    private String email;
    @NotEmpty(message = "password is required.")
    private String password;
}

