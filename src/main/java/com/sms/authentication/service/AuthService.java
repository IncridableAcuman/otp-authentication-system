package com.sms.authentication.service;

import com.sms.authentication.dto.auth.*;
import com.sms.authentication.dto.mail.EmailPayload;
import com.sms.authentication.entity.OtpEntity;
import com.sms.authentication.entity.User;
import com.sms.authentication.entity.enums.Role;
import com.sms.authentication.exception.CustomBadRequestException;
import com.sms.authentication.exception.CustomNotFoundException;
import com.sms.authentication.producer.RabbitMQProducer;
import com.sms.authentication.repository.OtpRepository;
import com.sms.authentication.repository.UserRepository;
import com.sms.authentication.util.CookieUtil;
import com.sms.authentication.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${jwt.access_time}")
    private long accessTime;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final RabbitMQProducer rabbitMQProducer;
    private final OtpRepository otpRepository;

    public void register(RegisterRequest request){
        existUserByEmail(request.getEmail());
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setActive(false);
        saveUser(user);

        String  otp = String.valueOf(Math.toIntExact(Math.round(Math.random() * 1000000)));
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setOtp(otp);
        otpEntity.setUserId(user.getId());
        otpEntity.setExpiration(new Date(System.currentTimeMillis() + accessTime));
        otpRepository.save(otpEntity);
        EmailPayload payload = new EmailPayload(user.getEmail(), "OTP",otp);

        rabbitMQProducer
                .sendMailWithRabbitMQ(payload);
    }

    public AuthResponse login(AuthRequest request,HttpServletResponse response){
        User user = findUserByEmail(request.getEmail());
        if (!user.isActive()){
            throw new CustomBadRequestException("User not activated");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new CustomBadRequestException("Password incorrect");
        }
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        cookieUtil.addCookie(refreshToken,response);
        redisService.saveToken(refreshToken,user.getId());
        return AuthResponse.from(accessToken);
    }

    public AuthResponse refresh(String token,HttpServletResponse response){
        String email = jwtUtil.extractSubject(token);
        User user = findUserByEmail(email);
        String cacheToken = redisService.getToken(user.getId());
        if (!cacheToken.equals(token)){
            throw new CustomBadRequestException("Invalid token");
        }
        redisService.removeTokenFromCache(user.getId());
        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        cookieUtil.addCookie(newRefreshToken,response);
        redisService.saveToken(newRefreshToken, user.getId());
        return AuthResponse.from(newAccessToken);
    }
    public void logout(String token,HttpServletResponse response){
        String email = jwtUtil.extractSubject(token);
        User user = findUserByEmail(email);
        String cacheToken = redisService.getToken(user.getId());
        if (!cacheToken.equals(token)){
            throw new CustomBadRequestException("Invalid token");
        }
        cookieUtil.clearCookie(response);
        redisService.removeTokenFromCache(user.getId());
    }

    public void forgotPassword(ForgotPasswordRequest request){
        User user = findUserByEmail(request.getEmail());
        String token = jwtUtil.generateAccessToken(user);
        String url = "http://localhost:5173/reset-password?token=" + token;
        String html = """
                <div>
                <button>Click here</button>
                </div>
                """.formatted(
                        user,
                url
        );
        EmailPayload payload = new EmailPayload(user.getEmail(),"Reset Password",html);
        rabbitMQProducer.sendMailWithRabbitMQ(payload);
    }

    public void resetPassword(ResetPasswordRequest request){
        if (request.getPassword().equals(request.getConfirmPassword())){
            throw new CustomBadRequestException("Password and confirm password must be equal");
        }
        String email = jwtUtil.extractSubject(request.getToken());
        User user = findUserByEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        saveUser(user);
    }

    public void activateUserAndCheckOtp(String otp){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = (User) authentication.getPrincipal();
        assert user != null;
        OtpEntity otpEntity = otpRepository.findByUserId(user.getId()).orElseThrow(()-> new CustomNotFoundException("Otp not found with user id"));
        if (!otpEntity.getOtp().equals(otp) || !otpEntity.getExpiration().before(new Date())){
            throw new CustomBadRequestException("Otp incorrect");
        }
        user.setActive(true);
        saveUser(user);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new CustomNotFoundException("User not found"));
    }

    public void existUserByEmail(String email){
        if (userRepository.findByEmail(email).isPresent()){
            throw new CustomBadRequestException("User already exist");
        }
    }

    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }
}
