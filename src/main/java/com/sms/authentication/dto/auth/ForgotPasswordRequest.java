package com.sms.authentication.dto.auth;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @Email(message = "Invalid email Format")
    private String email;
}
