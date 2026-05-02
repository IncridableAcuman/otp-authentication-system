package com.sms.authentication.service;

import com.sms.authentication.producer.RabbitMQProducer;
import com.sms.authentication.repository.UserRepository;
import com.sms.authentication.util.CookieUtil;
import com.sms.authentication.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final RabbitMQProducer rabbitMQProducer;
}
