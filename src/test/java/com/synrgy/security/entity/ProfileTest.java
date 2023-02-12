package com.synrgy.security.entity;

import lombok.Builder;
import lombok.Data;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@SpringBootTest

public class ProfileTest {

    @Test
    @Builder
    public void testProfile() {
        LocalDateTime now = LocalDateTime.now();
        Profile profile = new Profile();
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setPhoneNumber("1234567890");
        profile.setAvatar("avatar.jpg");
        profile.setProvince("Ontario");
        profile.setGender("Male");
        profile.setCity("Toronto");
        profile.setAddress("123 Main St.");
        profile.setGmaps("https://maps.google.com");
        assertEquals("John", profile.getFirstName());
        assertEquals("Doe", profile.getLastName());
        assertEquals("1234567890", profile.getPhoneNumber());
        assertEquals("avatar.jpg", profile.getAvatar());
        assertEquals("Ontario", profile.getProvince());
        assertEquals("Male", profile.getGender());
        assertEquals("Toronto", profile.getCity());
        assertEquals("123 Main St.", profile.getAddress());
        assertEquals("https://maps.google.com", profile.getGmaps());
    }
}
