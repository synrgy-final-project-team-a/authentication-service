package com.synrgy.security.dto;

import lombok.Data;


@Data
public class ResetPasswordModel {
    public String email;
    public String otp;
    public String newPassword;
}

