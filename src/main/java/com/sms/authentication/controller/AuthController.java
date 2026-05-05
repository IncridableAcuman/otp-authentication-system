package com.sms.authentication.controller;

import com.sms.authentication.constant.Endpoint;
import com.sms.authentication.dto.auth.*;
import com.sms.authentication.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.AUTHENTICATE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(Endpoint.REGISTER)
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);
        return ResponseEntity.ok("Check your email");
    }
    @PostMapping(Endpoint.LOGIN)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request, HttpServletResponse response){
        return ResponseEntity.ok(authService.login(request,response));
    }
    @GetMapping(Endpoint.REFRESH)
    public ResponseEntity<AuthResponse> refresh(@CookieValue(name = "token",required = false) String token,HttpServletResponse response){
        return ResponseEntity.ok(authService.refresh(token,response));
    }
    @PostMapping(Endpoint.LOGOUT)
    public ResponseEntity<String> logout(@CookieValue(name = "token",required = false) String token,HttpServletResponse response){
        authService.logout(token,response);
        return ResponseEntity.ok("Logged out successfully");
    }
    @PostMapping(Endpoint.FORGOT_PASSWORD)
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request){
        authService.forgotPassword(request);
        return ResponseEntity.ok("Reset password link sent to email");
    }
    @PutMapping(Endpoint.RESET_PASSWORD)
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        authService.resetPassword(request);
        return ResponseEntity.ok("Password updated successfully");
    }
    @GetMapping(Endpoint.VERIFY_EMAIL)
    public ResponseEntity<String> verifyEmail(@RequestParam String otp){
        authService.activateUserAndCheckOtp(otp);
        return ResponseEntity.ok("Successfully");
    }
}
