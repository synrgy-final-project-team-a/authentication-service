package com.synrgy.security.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Table(name = "profile")
public class Profile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    private Long user;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @JsonIgnore
    @NotNull
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "gmaps")
    private String gmaps;


}
