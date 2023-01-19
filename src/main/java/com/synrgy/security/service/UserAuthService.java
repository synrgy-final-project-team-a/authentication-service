package com.synrgy.security.service;

import com.synrgy.security.dto.LoginModel;
import com.synrgy.security.dto.RegisterModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserAuthService {
    Map registerSeeker(RegisterModel objModel);
    Map registerTennant(RegisterModel objModel);
    Map loginSeeker(LoginModel loginModel);

    Map loginTennant(LoginModel loginModel);

    Map loginSuperAdmin(LoginModel loginModel);


    public void logout(HttpServletRequest request, HttpServletResponse response);


}
