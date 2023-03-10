package com.synrgy.security.util;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Response {

    public Map sukses(Object obj){
        Map map = new HashMap();
        map.put("data", obj);
        map.put("code", 200);
        map.put("status", "sukses");
        return map;
    }

    public Map error(Object obj, Object code){
        Map map = new HashMap();
        map.put("code", code);
        map.put("status", obj);
        return map;
    }


    public Map templateSuksesGet(Object objek){
        Map map = new HashMap();
        map.put("data", objek);
        map.put("message", "sukses");
        map.put("status", "200");
        return map;
    }

    public Map templateSuksesPost(Object objek){
        Map map = new HashMap();
        map.put("data", objek);
        map.put("message", "sukses");
        map.put("status", "201");
        return map;
    }

    public Map templateError(Object objek){
        Map map = new HashMap();
        map.put("message", objek);
        map.put("status", "400");
        return map;
    }
    public Map urlNotFound(Object objek){
        Map map = new HashMap();
        map.put("message", objek);
        map.put("status", "404");
        return map;
    }

    public Boolean isRequired(Object obj){
        return obj == null;
    }

    public Map<String, Object> resSuccess(Object data, String message, Integer statusCode) {
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("message", message);
        res.put("status_code", statusCode);
        return res;
    }

    public Map<String, Object> clientError(Object message) {
        Map<String, Object> res = new HashMap<>();
        res.put("message", message);
        res.put("status_code", 400);
        return res;
    }

    public Map<String, Object> internalServerError(Object message) {
        Map<String, Object> res = new HashMap<>();
        res.put("message", message);
        res.put("status", 500);
        return res;
    }

    public Map<String, Object> notFoundError(Object message ) {
        Map<String, Object> res = new HashMap<>();
        res.put("message", message);
        res.put("status", 404);
        return res;
    }
}
