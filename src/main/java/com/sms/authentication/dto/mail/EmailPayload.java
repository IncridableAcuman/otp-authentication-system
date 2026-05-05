package com.sms.authentication.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailPayload {
    private String to;
    private String subject;
    private String text;
}
