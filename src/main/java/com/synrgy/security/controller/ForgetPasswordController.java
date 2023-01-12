package com.synrgy.security.controller;

import com.synrgy.security.configuration.Config;
import com.synrgy.security.dto.ResetPasswordModel;
import com.synrgy.security.entity.User;
import com.synrgy.security.repository.UserRepository;
import com.synrgy.security.service.UserAuthService;
import com.synrgy.security.service.email.EmailSender;
import com.synrgy.security.util.EmailTemplate;
import com.synrgy.security.util.Response;
import com.synrgy.security.util.SimpleStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/forget-password/")
public class ForgetPasswordController {

    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserAuthService userAuthService;

    @Value("${expired.token.password.hour:}")//FILE_SHOW_RUL
    private int expiredToken;

    @Autowired
    public Response response;

    @Autowired
    public EmailTemplate emailTemplate;

    @Autowired
    public EmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${BASEURL:}")//FILE_SHOW_RUL
    private String BASEURL;

    // Step 1 : Send OTP
    @PostMapping("/send")//send OTP
    public ResponseEntity<Map> sendEmailPassword(@Valid @RequestBody ResetPasswordModel user) {


        if (!(user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")))  {
            return new ResponseEntity<Map>(response.templateError("Wrong email format!"), HttpStatus.BAD_REQUEST);

        }
        if (StringUtils.isEmpty(user.getEmail())) return new ResponseEntity<Map>(response.templateError("No email provided"), HttpStatus.BAD_REQUEST);
        User found = userRepository.checkExistingEmail(user.getEmail());
        if (found == null) return new ResponseEntity<Map>(response.templateError("Email not found"), HttpStatus.BAD_REQUEST);; //throw new BadRequest("Email not found");


        String template = emailTemplate.getResetPassword();
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
            found.setEnabled(false); // matikan user
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? user.getEmail() : found.getUsername()));
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", BASEURL + "/forget-password/index/" + otp);
            userRepository.save(found);

        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? user.getEmail() : found.getUsername()));
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", BASEURL + "/forget-password/index/" + found.getOtp());
        }
        emailSender.sendAsync(user.getEmail(), "Binar - Forget Password", template);


        return new ResponseEntity<Map>(response.templateSuksesPost(found), HttpStatus.CREATED);

    }

    //Step 2 : Email token OTP
    @GetMapping(value = {"/index/{tokenotp}"})
    public ResponseEntity<Map> index(Model model, @PathVariable String tokenotp) {

        if (tokenotp.isEmpty()) {
            return new ResponseEntity<Map>(response.urlNotFound("OTP not found in url!"), HttpStatus.NOT_FOUND);
        }

        User user = userRepository.findOneByOTP(tokenotp);
        if (null == user) {
            System.out.println("User null: tidak ditemukan");
            model.addAttribute("erordesc", "User not found for code " + tokenotp);
            model.addAttribute("title", "");
            return new ResponseEntity<Map>(response.urlNotFound("User not found"), HttpStatus.NOT_FOUND);
        }

        if (user.isEnabled()) {
            model.addAttribute("erordesc", "Your account is already active, please do login");
            model.addAttribute("title", "");
            return new ResponseEntity<Map>(response.urlNotFound("Your account is already active, please do login"), HttpStatus.NOT_FOUND);
        }
        String today = config.convertDateToString(new Date());

        String dateToken = config.convertDateToString(user.getOtpExpiredDate());
        if (Long.parseLong(today) > Long.parseLong(dateToken)) {
            model.addAttribute("erordesc", "Your token is expired. Please get token again.");
            model.addAttribute("title", "");
            return new ResponseEntity<Map>(response.urlNotFound("Your token is expired. Please get token again. "), HttpStatus.NOT_FOUND);
        }
        user.setEnabled(true);
        userRepository.save(user);
        model.addAttribute("title", "Congratulations, " + user.getUsername() + ", you have successfully registered!");
        model.addAttribute("erordesc", "");
        return new ResponseEntity<Map>(response.templateSuksesGet(user), HttpStatus.OK);
    }

    // Step 3 : lakukan reset password baru
    @PostMapping("/change-password")
    public ResponseEntity<Map> resetPassword(@RequestBody ResetPasswordModel model) {
        if (model.getOtp() == null) return new ResponseEntity<Map>(response.templateError("Token is required"), HttpStatus.BAD_REQUEST);
        if (model.getNewPassword() == null) return new ResponseEntity<Map>(response.templateError("New Password is required"), HttpStatus.BAD_REQUEST);
        User user = userRepository.findOneByOTP(model.getOtp());
        String success;
        if (user == null) return new ResponseEntity<Map>(response.templateError("Token not valid"), HttpStatus.BAD_REQUEST);

        user.setPassword(passwordEncoder.encode(model.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);

        try {
            userRepository.save(user);
            success = "Password changed successfully!";
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.templateError("Failed save user, please try again."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(response.templateSuksesPost(success), HttpStatus.CREATED);
    }
}
