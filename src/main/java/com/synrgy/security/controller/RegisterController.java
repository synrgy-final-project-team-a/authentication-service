package com.synrgy.security.controller;

import com.synrgy.security.configuration.Config;
import com.synrgy.security.dto.LoginModel;
import com.synrgy.security.dto.RegisterModel;
import com.synrgy.security.entity.Profile;
import com.synrgy.security.entity.Role;
import com.synrgy.security.entity.User;
import com.synrgy.security.repository.ProfileRepository;
import com.synrgy.security.repository.UserRepository;
import com.synrgy.security.service.UserAuthService;
import com.synrgy.security.service.email.EmailSender;
import com.synrgy.security.util.EmailTemplate;
import com.synrgy.security.util.Response;
import com.synrgy.security.util.SimpleStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/register/")
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserAuthService userAuthService;

    @Autowired
    public Response response;

    @Autowired
    public EmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/seeker")
    public ResponseEntity<Map> saveRegisterSeeker(@Valid @RequestBody RegisterModel objModel) throws RuntimeException {
        Map map = new HashMap();
        if (!(objModel.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$"))) {
            return new ResponseEntity<Map>(response.templateError("Wrong email format!"), HttpStatus.BAD_REQUEST);
        }


        User user = userRepository.findOneByUsername(objModel.getEmail());
        if (null != user) {
            return new ResponseEntity<Map>(response.templateError("Email sudah ada"), HttpStatus.BAD_REQUEST);
        }
        map = userAuthService.registerSeeker(objModel);
        ResponseEntity<Map> sendOTPUri = sendEmailegisterTymeleafUser(objModel);

        return new ResponseEntity<Map>(map, HttpStatus.CREATED);
    }

    @PostMapping("/tennant")
    public ResponseEntity<Map> saveRegisterTennant(@Valid @RequestBody RegisterModel objModel) throws RuntimeException {
        Map map = new HashMap();
        if (!(objModel.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")))  {
            return new ResponseEntity<Map>(response.templateError("Wrong email format!"), HttpStatus.BAD_REQUEST);
        }

        if (objModel.getPassword().length()<8) {
            return new ResponseEntity<Map>(response.templateError("Password must be greater or equals 8 character, please try again"), HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findOneByUsername(objModel.getEmail());
        if (null != user) {
            return new ResponseEntity<Map>(response.templateError("Email sudah ada"), HttpStatus.BAD_REQUEST);
        }
        map = userAuthService.registerTennant(objModel);
        ResponseEntity<Map> sendOTPUri = sendEmailegisterTymeleafUser(objModel);

        return new ResponseEntity<Map>(map, HttpStatus.CREATED);
    }

    @Autowired
    public EmailTemplate emailTemplate;

    @Value("${expired.token.password.hour}")
    int expiredToken;

    @GetMapping("/index/{token}")
    public ResponseEntity<Map> saveRegisterManual(@PathVariable(value = "token") String tokenOtp) throws RuntimeException {

        if (tokenOtp.isEmpty()) {
            return new ResponseEntity<Map>(response.urlNotFound("OTP not found in url!"), HttpStatus.NOT_FOUND);
        }

        if (tokenOtp.length()!=6) {
            return new ResponseEntity<Map>(response.urlNotFound("Wrong format OTP!"), HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findOneByOTP(tokenOtp);
        if (null == user) {
            return new ResponseEntity<Map>(response.urlNotFound("OTP not found!"), HttpStatus.NOT_FOUND);
        }

        if(user.isEnabled()){
            return new ResponseEntity<Map>(response.urlNotFound("Your account is already active!"), HttpStatus.NOT_FOUND);
        }
        String today = config.convertDateToString(new Date());

        String dateToken = config.convertDateToString(user.getOtpExpiredDate());
        if(Long.parseLong(today) > Long.parseLong(dateToken)){
            return new ResponseEntity<Map>(response.templateError("Your token is expired. Please get token again."), HttpStatus.NOT_FOUND);
        }
        //update user
        user.setEnabled(true);
        user.setOtpExpiredDate(null);
        user.setOtp(null);
        userRepository.save(user);

        return new ResponseEntity<Map>(response.templateSuksesGet("Your email is verified! Now you will be redirected into login menu. "), HttpStatus.OK);
    }

    @Value("${BASEURL:}")//FILE_SHOW_RUL
    private String BASEURL;
    @Autowired
    private ProfileRepository profileRepository;


    @PostMapping("/send-otp")//send OTP : berupa URL
    public ResponseEntity<Map> sendEmailegisterTymeleafUser(@RequestBody RegisterModel user) {
//        String message = "Thanks, please check your email for activation.";

        if (user.getEmail() == null) return new ResponseEntity<Map>(response.templateError("No email provided"), HttpStatus.BAD_REQUEST);
        User found = userRepository.checkExistingEmail(user.getEmail());
        if (found == null) return new ResponseEntity<Map>(response.templateError("Email not found"), HttpStatus.BAD_REQUEST); //throw new BadRequest("Email not found");

        String template = emailTemplate.getRegisterTemplate();
        if (StringUtils.isEmpty(found.getOtp())) {
            User search;
            String otp;
            do {
                otp = SimpleStringUtils.randomString(6, true);
                search = userRepository.findOneByOTP(otp);
            } while (search != null);
            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.HOUR, expiredToken);
            Date expirationDate = calendar.getTime();

            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? user.getEmail() : found.getUsername()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", BASEURL + "/register/index/"+ otp);
            userRepository.save(found);
        } else if (found.getOtp() != null) {
            found.setOtpExpiredDate(null);
            Date in = new Date();
            LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
            Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(out);
            calendar.add(Calendar.HOUR, expiredToken);
            Date expirationDate = calendar.getTime();
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? user.getEmail() : found.getUsername()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", BASEURL + "/register/index/"+  found.getOtp());
            userRepository.save(found);
        } else {
            return new ResponseEntity<Map>(response.templateError("Unable to send OTP to your email, please try again."), HttpStatus.BAD_REQUEST);

        }
        emailSender.sendAsync(user.getEmail(), "Register", template);
        return new ResponseEntity<Map>(response.templateSuksesPost(found), HttpStatus.CREATED);
    }


}