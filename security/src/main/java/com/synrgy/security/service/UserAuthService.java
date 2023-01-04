package com.synrgy.security.service;

import com.synrgy.security.dto.RegisterModel;

import java.util.Map;

public interface UserAuthService {
    Map register(RegisterModel objModel);

}
