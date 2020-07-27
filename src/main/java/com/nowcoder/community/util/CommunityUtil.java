package com.nowcoder.community.util;

import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;

import org.springframework.util.DigestUtils;
import org.thymeleaf.util.StringUtils;

public class CommunityUtil {

    // 生成随机字符串
    public static String genetateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    //MD5加密
    public static String md5(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJsonString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            map.keySet().forEach(key -> json.put(key, map.get(key)));
        }
        return json.toJSONString();
    }

    public static String getJsonString(int code, String msg) {
        return getJsonString(code, msg, null);
    }

    public static String getJsonString(int code) {
        return getJsonString(code, null, null);
    }
}
