package com.sms.authentication.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Firstname must be required")
    @Size(min = 3,max = 50,message = "Firstname must be between 3 and 50 characters")
    private String firstName;
    @NotBlank(message = "Lastname must be required")
    @Size(min = 3,max = 50,message = "Firstname must be between 3 and 50 characters")
    private String lastName;
    @NotBlank(message = "Username must be required")
    @Size(min = 3,max = 50,message = "Firstname must be between 3 and 50 characters")
    private String username;
    @Email(message = "Invalid email Format")
    private String email;
    @NotBlank(message = "Password must be required")
    @Size(min = 8,max = 50,message = "Password must be between 3 and 50 characters")
    private String password;
}
