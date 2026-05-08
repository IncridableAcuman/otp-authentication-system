package com.sms.authentication.constant;

import lombok.Getter;

@Getter
public enum ResponseType {
    SUCCESS("Successfully"),
    USER_NOT_FOUND("User not found"),
    VERIFY_USER("User already verified"),
    EXIST_USER("User already exist"),
    INCORRECT_OTP("Incorrect otp"),
    OTP_NOT_FOUND("Otp not found with user id"),
    OTP_EXPIRED("OTP expired"),
    PASSWORD_EQUAL_CONFIRM_PASSWORD("Password and confirm password must be equal"),
    INVALID_TOKEN("Invalid token"),
    INCORRECT_PASSWORD("Password incorrect"),
    INACTIVE_USER("User not activated"),
    RESET_PASSWORD("Reset password"),
    CHECK_EMAIL("Check your email"),
    LOGGED_OUT("Logged out successfully"),
    RESET_PASSWORD_LINK("Reset password link sent to email"),
    UPDATE_PASSWORD("Password updated successfully");


    private final String message;
    ResponseType(String message){
        this.message=message;
    }
}
