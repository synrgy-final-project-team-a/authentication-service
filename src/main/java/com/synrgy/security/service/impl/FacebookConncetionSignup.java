package com.synrgy.security.service.impl;

import com.synrgy.security.entity.User;
import com.synrgy.security.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

public class FacebookConncetionSignup implements ConnectionSignUp {
    @Autowired
    UserRepository userRepository;

    @Override
    public String execute(Connection<?> connection) {
        User user = new User();
        user.setUsername(connection.getDisplayName());
        user.setPassword(RandomStringUtils.random(8));
        userRepository.save(user);
        return user.getUsername();
    }
}
