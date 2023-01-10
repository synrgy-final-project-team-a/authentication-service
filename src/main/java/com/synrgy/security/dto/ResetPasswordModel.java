package com.synrgy.security.dto;

import lombok.Data;


@Data
public class ResetPasswordModel {
    public String otp;
    public String email;
    public String newPassword;
}

