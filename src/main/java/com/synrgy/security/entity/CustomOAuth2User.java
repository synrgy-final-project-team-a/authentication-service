//package com.synrgy.security.entity;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import javax.persistence.*;
//import java.util.Collection;
//import java.util.Map;
//
//@Data
//@Entity
//@Table(name = "oauth2_user")
//@NoArgsConstructor
//public class CustomOAuth2User implements OAuth2User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    private OAuth2User oauth2User;
//
//    public CustomOAuth2User(OAuth2User oauth2User) {
//        this.oauth2User = oauth2User;
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return oauth2User.getAttributes();
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return oauth2User.getAuthorities();
//    }
//
//    @Override
//    public String getName() {
//        return oauth2User.getAttribute("name");
//    }
//
//    public String getEmail() {
//        return oauth2User.<String>getAttribute("email");
//    }
//}
