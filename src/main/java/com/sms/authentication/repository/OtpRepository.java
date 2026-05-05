package com.sms.authentication.repository;

import com.sms.authentication.entity.OtpEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends MongoRepository<OtpEntity,String> {
    Optional<OtpEntity> findByUserId(String userId);
}
