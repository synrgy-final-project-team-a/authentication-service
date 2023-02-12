package com.synrgy.security.entity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class UserTest {
    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void testId() {
        Long id = 1L;
        user.setId(id);
        Assert.assertEquals(id, user.getId());
    }

    @Test
    public void testUsername() {
        String username = "test@example.com";
        user.setUsername(username);
        Assert.assertEquals(username, user.getUsername());
    }

    @Test
    public void testPassword() {
        String password = "password";
        user.setPassword(password);
        Assert.assertEquals(password, user.getPassword());
    }

    @Test
    public void testVerifyToken() {
        String verifyToken = "token";
        user.setVerifyToken(verifyToken);
        Assert.assertEquals(verifyToken, user.getVerifyToken());
    }

    @Test
    public void testExpiredVerifyToken() {
        Date expiredVerifyToken = new Date();
        user.setExpiredVerifyToken(expiredVerifyToken);
        Assert.assertEquals(expiredVerifyToken, user.getExpiredVerifyToken());
    }

    @Test
    public void testOtp() {
        String otp = "otp";
        user.setOtp(otp);
        Assert.assertEquals(otp, user.getOtp());
    }

    @Test
    public void testOtpExpiredDate() {
        Date otpExpiredDate = new Date();
        user.setOtpExpiredDate(otpExpiredDate);
        Assert.assertEquals(otpExpiredDate, user.getOtpExpiredDate());
    }

    @Test
    public void testEnabled() {
        user.setEnabled(true);
        Assert.assertTrue(user.isEnabled());
    }

    @Test
    public void testAccountNonExpired() {
        user.setAccountNonExpired(true);
        Assert.assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testAccountNonLocked() {
        user.setAccountNonLocked(true);
        Assert.assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testCredentialsNonExpired() {
        user.setCredentialsNonExpired(true);
        Assert.assertTrue(user.isCredentialsNonExpired());
    }
}
