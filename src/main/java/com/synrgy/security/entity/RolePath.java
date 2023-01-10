package com.synrgy.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Entity
@Table(name = "oauth_role_path")
public class RolePath implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    private String pattern;

    private String method;

    @ManyToOne(targetEntity = Role.class, cascade = CascadeType.ALL)
    @JsonIgnore
    private Role role;
}