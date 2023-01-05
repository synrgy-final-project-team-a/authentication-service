package com.synrgy.security.controller;

import com.synrgy.security.configuration.Config;
import com.synrgy.security.dto.RegisterModel;
import com.synrgy.security.entity.User;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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


    @PostMapping("/register")
    public ResponseEntity<Map> saveRegisterManual(@Valid @RequestBody RegisterModel objModel) throws RuntimeException {
        Map map = new HashMap();

        User user = userRepository.checkExistingEmail(objModel.getEmail());
        if (null != user) {
            return new ResponseEntity<Map>(response.notFound("Email sudah ada"), HttpStatus.OK);
        }
        map = userAuthService.register(objModel);
        Map sendOTPUri = sendEmailegisterTymeleafUser(objModel);

        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @Autowired
    public EmailTemplate emailTemplate;

    @Value("${expired.token.password.minute}")
    int expiredToken;

    @GetMapping("/index/{token}")
    public ResponseEntity<Map> saveRegisterManual(@PathVariable(value = "token") String tokenOtp) throws RuntimeException {



        User user = userRepository.findOneByOTP(tokenOtp);
        if (null == user) {
            return new ResponseEntity<Map>(response.templateEror("OTP tidak ditemukan"), HttpStatus.OK);
        }

        if(user.isEnabled()){
            return new ResponseEntity<Map>(response.templateSukses("Akun Anda sudah aktif, Silahkan melakukan login"), HttpStatus.OK);
        }
        String today = config.convertDateToString(new Date());

        String dateToken = config.convertDateToString(user.getOtpExpiredDate());
        if(Long.parseLong(today) > Long.parseLong(dateToken)){
            return new ResponseEntity<Map>(response.templateEror("Your token is expired. Please Get token again."), HttpStatus.OK);
        }
        //update user
        user.setEnabled(true);
        user.setOtpExpiredDate(null);
        user.setOtp(null);
        userRepository.save(user);

        return new ResponseEntity<Map>(response.templateSukses("Sukses, silahkan melakukan login"), HttpStatus.OK);
    }

    @Value("${BASEURL:}")//FILE_SHOW_RUL
    private String BASEURL;


    @PostMapping("/send-otp")//send OTP : berupa URL
    public Map sendEmailegisterTymeleafUser(@RequestBody RegisterModel user) {
        String message = "Thanks, please check your email for activation.";

        if (user.getEmail() == null) return response.templateEror("No email provided");
        User found = userRepository.findOneByEmail(user.getEmail());
        if (found == null) return response.notFound("Email not found"); //throw new BadRequest("Email not found");

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
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();

            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? found.getEmail() : found.getUsername()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", BASEURL + "/register/index/"+ otp);
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? found.getEmail() : found.getUsername()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", BASEURL + "/register/index/"+  found.getOtp());
        }
        emailSender.sendAsync(found.getEmail(), "Register", template);
        return response.templateSukses(message);
    }


}