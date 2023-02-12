package com.synrgy.security.entity;

import org.springframework.boot.test.context.SpringBootTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ClientTest {

    private Client client;
    private Set<GrantedAuthority> authorities;

    @Before
    public void setUp() {
        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        client = new Client();
        client.setId(1L);
        client.setClientId("client1");
        client.setClientSecret("secret1");
        client.setScopes("read write");
        client.setGrantTypes("password authorization_code");
        client.setRedirectUris("http://localhost:8080/callback");
        client.setApproved(true);
        client.setAccessTokenValiditySeconds(3600);
        client.setRefreshTokenValiditySeconds(7200);
        client.setAuthorities(authorities);
    }

    @Test
    public void getId() {
        assertThat(client.getId()).isEqualTo(1L);
    }

    @Test
    public void getClientId() {
        assertThat(client.getClientId()).isEqualTo("client1");
    }

    @Test
    public void getResourceIds() {
        assertThat(client.getResourceIds().size()).isEqualTo(1);
        assertThat(client.getResourceIds().contains("oauth2-resource")).isTrue();
    }

    @Test
    public void isSecretRequired() {
        assertThat(client.isSecretRequired()).isTrue();
    }

    @Test
    public void getClientSecret() {
        assertThat(client.getClientSecret()).isEqualTo("secret1");
    }

    @Test
    public void isScoped() {
        assertThat(client.isScoped()).isTrue();
    }

    @Test
    public void getScope() {
        assertThat(client.getScope().size()).isEqualTo(2);
        assertThat(client.getScope().contains("read")).isTrue();
        assertThat(client.getScope().contains("write")).isTrue();
    }

    @Test
    public void getAuthorizedGrantTypes() {
        assertThat(client.getAuthorizedGrantTypes().size()).isEqualTo(2);
        assertThat(client.getAuthorizedGrantTypes().contains("password")).isTrue();
        assertThat(client.getAuthorizedGrantTypes().contains("authorization_code")).isTrue();
    }
}

