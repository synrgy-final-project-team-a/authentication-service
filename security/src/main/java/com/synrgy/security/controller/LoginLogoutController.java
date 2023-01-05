package com.synrgy.security.controller;

import com.synrgy.security.configuration.Config;
import com.synrgy.security.dto.LoginModel;
import com.synrgy.security.repository.UserRepository;
import com.synrgy.security.service.UserAuthService;
import com.synrgy.security.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RestController
public class LoginLogoutController {
    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserAuthService userAuthService;

    @Autowired
    public Response response;

    @PostMapping("/login-user")
    public ResponseEntity<Map> login(@Valid @RequestBody LoginModel loginModel) {
        Map map = userAuthService.login(loginModel);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PostMapping("/logout-user")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        userAuthService.logout(request, response);
    }
}