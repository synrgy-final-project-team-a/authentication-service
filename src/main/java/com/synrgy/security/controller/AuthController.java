package com.synrgy.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AuthController {

    @Autowired
    OAuth2AuthorizedClientService authorizedClientService;

    public AuthController(){

    }

    @GetMapping("login-oauth2-google")
    public ResponseEntity getLoginInfoOA2(OAuth2AuthenticationToken token){
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        token.getAuthorizedClientRegistrationId(),
                        token.getName()
                );

        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();
        Map<String, Object> response = new HashMap<>();
        if(!StringUtils.isEmpty(userInfoEndpointUri)){
            response.put("name", token.getName());
            response.put("token", "Bearer" + client.getAccessToken());
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        response.put("status", "ERROR");
        response.put("message", "OAuth2 token is empty");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @RequestMapping("/")
    public String getHomepage() {
        return "index";
    }
}
