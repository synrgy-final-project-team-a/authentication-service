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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/forget-password/")
public class ForgetPasswordController {

    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserAuthService userAuthService;

    @Value("${expired.token.password.minute:}")//FILE_SHOW_RUL
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
    public Map sendEmailPassword(@Valid @RequestBody ResetPasswordModel user) {
        String message = "Thanks, please check your email";

        if (StringUtils.isEmpty(user.getEmail())) return response.templateEror("No email provided");
        User found = userRepository.findOneByEmail(user.getEmail());
        if (found == null) return response.notFound("Email not found"); //throw new BadRequest("Email not found");

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
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getEmail() == null ? "" +
                    "@UserName"
                    :
                     found.getEmail()));
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", BASEURL + "/forget-password/index/"+ otp);
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getEmail() == null ? "" +
                    "@UserName"
                    :
                    found.getEmail()));
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", BASEURL + "/forget-password/index/"+ found.getOtp());
        }
        emailSender.sendAsync(found.getEmail(), "Binar - Forget Password", template);


        return response.templateSukses("success");

    }

    //Step 2 : Email token OTP
    @GetMapping(value = { "/index/{tokenotp}"})
    public String index(Model model, @PathVariable String  tokenotp) {
        User user = userRepository.findOneByOTP(tokenotp);
        if (null == user) {
            System.out.println("user null: tidak ditemukan");
            model.addAttribute("erordesc", "User not found for code "+tokenotp);
            model.addAttribute("title", "");
            return "register";
        }

        if(user.isEnabled()){
            model.addAttribute("erordesc", "Akun Anda ternyata sudah aktif, Silahkan melakukan login ");
            model.addAttribute("title", "");
            return "register";
        }
        String today = config.convertDateToString(new Date());

        String dateToken = config.convertDateToString(user.getOtpExpiredDate());
        if(Long.parseLong(today) > Long.parseLong(dateToken)){
            model.addAttribute("erordesc", "Your token is expired. Please Get token again.");
            model.addAttribute("title", "");
            return "register";
        }
        user.setEnabled(true);
        userRepository.save(user);
        model.addAttribute("title", "Congratulations, "+user.getEmail()+", you have successfully registered.");
        model.addAttribute("erordesc", "");
        return "Success OTP";
    }

    // Step 3 : lakukan reset password baru
    @PostMapping("/change-password")
    public Map<String, String> resetPassword(@RequestBody ResetPasswordModel model) {
        if (model.getOtp() == null) return response.notFound("Token is required");
        if (model.getNewPassword() == null) return response.notFound("New Password  is required");
        User user = userRepository.findOneByOTP(model.getOtp());
        String success;
        if (user == null) return response.notFound("Token not valid");

        user.setPassword(passwordEncoder.encode(model.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);

        try {
            userRepository.save(user);
            success = "success";
        } catch (Exception e) {
            return response.templateEror("Gagal simpan user");
        }
        return response.templateSukses(success);
    }


}
