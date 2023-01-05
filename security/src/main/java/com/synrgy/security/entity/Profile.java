package com.synrgy.security.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long id;

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


    public void setAvatar(String avatar) {
        this.avatar = "https://static.wikia.nocookie.net/cartoonica/images/8/8a/Profile_-_Otis.jpg/revision/latest?cb=20190507000059";
    }

}
