package com.synrgy.security.service.impl;

import com.synrgy.security.configuration.Config;

import java.util.HashMap;
import java.util.Map;

public class TemplateResponse {
    Config config = new Config();
    public Map TemplateSuccess(Object obj)  {
        Map map = new HashMap();
        try {
            map.put("data",obj );
            map.put(config.getCode(), config.code_sukses);
            map.put(config.getMessage(), config.message_sukses);
            return map;

        } catch (Exception e) {
            System.out.println("eror template1 ="+e);
            map.put(config.getCode(), config.code_server);
            map.put(config.getMessage(), e.getMessage());
            return map;
        }

    }

    public Map TemplateError(String obj)  {
        Map map = new HashMap();
            map.put(config.getCode(), config.code_server);
            map.put(config.getMessage(), obj);
            return map;
    }

    public Map TemplateNotFound(String obj)  {
        Map map = new HashMap();
        map.put(config.getCode(), config.code_notFound);
        map.put(config.getMessage(), obj);
        return map;
    }

    public Map TemplateIsRequired(String obj)  {
        Map map = new HashMap();
        map.put(config.getCode(), config.code_notFound);
        map.put(config.getMessage(), obj);
        return map;
    }


}
