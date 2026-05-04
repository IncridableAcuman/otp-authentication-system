package com.sms.authentication.dto.mail;

import lombok.Data;

@Data
public class EmailPayload {
    private String to;
    private String subject;
    private String text;
}
