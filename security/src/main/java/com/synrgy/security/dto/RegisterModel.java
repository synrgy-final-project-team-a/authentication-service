package com.synrgy.security.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class RegisterModel {
    @Email(message = "Email is not valid.")
    @NotEmpty(message = "Email is required.")
    private String email;
    @NotEmpty(message = "Password is required.")
    private String password;
    @NotEmpty(message = "Status is required.")
    private String status;

}
