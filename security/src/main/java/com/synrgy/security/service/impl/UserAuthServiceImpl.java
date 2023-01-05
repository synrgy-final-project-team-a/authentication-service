package com.synrgy.security.service.impl;

import com.synrgy.security.dto.LoginModel;
import com.synrgy.security.dto.RegisterModel;
import com.synrgy.security.entity.Profile;
import com.synrgy.security.entity.Role;
import com.synrgy.security.entity.User;
import com.synrgy.security.repository.ProfileRepository;
import com.synrgy.security.repository.RoleRepository;
import com.synrgy.security.repository.UserRepository;
import com.synrgy.security.service.UserAuthService;
import com.synrgy.security.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
    private ProfileRepository profileRepository;
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
    public Map register(RegisterModel registerModel) {
        Map map = new HashMap();
        try {
            User user = new User();
            Profile profile = new Profile();
            profile.setFirstName(registerModel.getFirstName());
            profile.setLastName(registerModel.getLastName());
            profile.setPhoneNumber(registerModel.getPhoneNumber());
            profile.setAvatar("https://static.wikia.nocookie.net/cartoonica/images/8/8a/Profile_-_Otis.jpg/revision/latest?cb=20190507000059");
            user.setUsername(registerModel.getEmail().toLowerCase());
            //step 1 :
            user.setEnabled(false); // matikan user

            String password = encoder.encode(registerModel.getPassword().replaceAll("\\s+", ""));
            user.setPassword(password);
//            user.setStatus(objModel.getStatus());
//            if (user.getStatus().equals("Admin")) {
//                String[] roleNames = {"ROLE_TN", "ROLE_READ", "ROLE_WRITE"}; // admin
//                List<Role> r = roleRepository.findByNameIn(roleNames);
//                user.setRoles(r);
//            } else if (user.getStatus().equals("User")) {
            String role = registerModel.getRole();
            String[] roleNames = {role, "ROLE_READ", "ROLE_WRITE"}; // user
            List<Role> r = roleRepository.findByNameIn(roleNames);
            user.setRoles(r);
            Profile obj1 = profileRepository.save(profile);
            User obj = userRepository.save(user);

            return templateResponse.templateSuksesPost(obj);

        } catch (Exception e) {
            logger.error("Error registerManual=", e);
            return templateResponse.templateError("error:" + e);
        }
    }

    @Override
    public Map login(LoginModel loginModel) {
        try {
            Map<String, Object> map = new HashMap<>();

            String url = baseUrl + "/oauth/token?username=" + loginModel.getEmail() +
                    "&password=" + loginModel.getPassword() +
                    "&grant_type=password" +
                    "&client_id=my-client-web" +
                    "&client_secret=password";
            ResponseEntity<Map> response = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new
                    ParameterizedTypeReference<Map>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK) {
                User user = userRepository.findOneByEmail(loginModel.getEmail());
                List<String> roles = new ArrayList<>();

                for (Role role : user.getRoles()) {
                    roles.add(role.getName());
                }
                map.put("access_token", response.getBody().get("access_token"));
                map.put("token_type", response.getBody().get("token_type"));
                map.put("refresh_token", response.getBody().get("refresh_token"));
                map.put("expires_in", response.getBody().get("expires_in"));
                map.put("scope", response.getBody().get("scope"));
                map.put("jti", response.getBody().get("jti"));

                return map;
            } else {
                return templateResponse.templateError("user not found");
            }
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return templateResponse.templateError("Invalid login");
            }
            return templateResponse.templateError(e);
        } catch (Exception e) {
            e.printStackTrace();

            return templateResponse.templateError(e);
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());


        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        // Revoke the access token
        String accessToken = details.getTokenValue(); // Get the access token from the request or security context
        String url = baseUrl + "/oauth/revoke-token?token=" + accessToken;

        ResponseEntity<Void> responses = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, Void.class);
    }
}
