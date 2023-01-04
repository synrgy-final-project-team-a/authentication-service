package com.synrgy.security.service.impl;

import com.synrgy.security.dto.RegisterModel;
import com.synrgy.security.entity.Role;
import com.synrgy.security.entity.User;
import com.synrgy.security.repository.RoleRepository;
import com.synrgy.security.repository.UserRepository;
import com.synrgy.security.service.UserAuthService;
import com.synrgy.security.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private static Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);
    @Value("${BASEURL}")
    private String baseUrl;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    public Response templateResponse;
    @Autowired
    private Oauth2UserDetailsService userDetailsService;


    @Autowired
    Response response;

    @Override
    public Map register(RegisterModel objModel) {
        Map map = new HashMap();
        try {
            User user = new User();
            user.setEmail(objModel.getEmail().toLowerCase());
            //step 1 :
            user.setEnabled(false); // matikan user

            String password = encoder.encode(objModel.getPassword().replaceAll("\\s+", ""));
            user.setPassword(password);
            user.setStatus(objModel.getStatus());
            if (user.getStatus().equals("Admin")) {
                String[] roleNames = {"ROLE_ADMIN", "ROLE_READ", "ROLE_WRITE"}; // admin
                List<Role> r = roleRepository.findByNameIn(roleNames);
                user.setRoles(r);
            } else if (user.getStatus().equals("User")) {
                String[] roleNames = {"ROLE_USER", "ROLE_READ", "ROLE_WRITE"}; // user
                List<Role> r = roleRepository.findByNameIn(roleNames);
                user.setRoles(r);
            } else if (user.getStatus() == null) {
                return templateResponse.templateEror("No status");
            }else {
                return templateResponse.templateEror("Error for saving status");
            };
            User obj = userRepository.save(user);

            return templateResponse.templateSukses(obj);

        } catch (Exception e) {
            logger.error("Eror registerManual=", e);
            return templateResponse.templateEror("eror:" + e);
        }
    }
}
