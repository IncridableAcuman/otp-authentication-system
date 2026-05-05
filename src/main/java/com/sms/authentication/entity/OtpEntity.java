package com.sms.authentication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "otp_entities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpEntity {
    @Id
    private String id;

    private String userId;

    private String otp;

    private Date expiration;
}
