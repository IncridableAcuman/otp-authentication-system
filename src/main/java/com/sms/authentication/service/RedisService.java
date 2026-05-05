package com.sms.authentication.service;

import com.sms.authentication.exception.CustomBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String,Object> template;

    @Value("${jwt.refresh_time}")
    private int refreshTime;

    public String getKey(String  userId){
        if (userId == null){
            throw new CustomBadRequestException("User id is null");
        }
        return "token:" + userId;
    }

    public void saveToken(String refreshToken,String  userId){
        if (refreshToken == null || refreshToken.isEmpty()){
            throw new CustomBadRequestException("Refresh token is empty or null");
        }
        String key = getKey(userId);
        template
                .opsForValue()
                .set(
                        key,
                        refreshToken,
                        refreshTime,
                        TimeUnit.MILLISECONDS
                );
    }
    public String getToken(String  userId){
        String key = getKey(userId);
        Object token = template.opsForValue().get(key);
        return token != null ? token.toString() : null;
    }
    public void removeTokenFromCache(String  userId){
        String key = getKey(userId);
        template
                .delete(key);
    }
}
