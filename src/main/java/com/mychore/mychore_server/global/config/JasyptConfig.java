package com.mychore.mychore_server.global.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class JasyptConfig {
//    @Value("${jasypt.encryptor.password}")
//    private String encryptKey;
//
//    @Bean(name = "jasyptSpringEncryptor")
//    public StandardPBEStringEncryptor jasyptStringEncryptor() {
//        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//        encryptor.setAlgorithm("PBEWithMD5AndDES");
//        encryptor.setPassword(encryptKey);
//        return encryptor;
//    }
//}
