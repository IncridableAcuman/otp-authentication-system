package com.sms.authentication.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    @Value("${jwt.refresh_time}")
    private int refreshTime;

    public void cookieManagement(String token,int expirationTime, HttpServletResponse response){
        Cookie cookie = new Cookie("token",token);
        cookie.setHttpOnly(true);
        cookie.setValue(token);
        cookie.setMaxAge(expirationTime);
        cookie.setSecure(false);
        cookie.setPath("/");

        response.addCookie(cookie);
    }
    public void addCookie(String token,HttpServletResponse response){
        cookieManagement(token,refreshTime/1000,response);
    }
    public void clearCookie(HttpServletResponse response){
        cookieManagement(null,0,response);
    }
}
