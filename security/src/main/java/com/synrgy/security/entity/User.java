package com.synrgy.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "oauth_user")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique=true)
    private String username;

    @JsonIgnore
    @NotNull
    private String password;


    @JsonIgnore
    private String verifyToken;

    @JsonIgnore
    private Date expiredVerifyToken;

    @Column
    private String otp;

    @JsonIgnore
    private Date otpExpiredDate;


    @JsonIgnore
    private boolean enabled = true;

    @JsonIgnore
    @Column(name = "not_expired")
    private boolean accountNonExpired = true;

    @JsonIgnore
    @Column(name = "not_locked")
    private boolean accountNonLocked = true;

    @JsonIgnore
    @Column(name = "credential_not_expired")
    private boolean credentialsNonExpired = true;

    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "oauth_user_role",
            joinColumns = {
                    @JoinColumn(name = "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id")
            }
    )

    @JsonIgnore
    private List<Role> roles = new ArrayList<>();


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }



    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

}

