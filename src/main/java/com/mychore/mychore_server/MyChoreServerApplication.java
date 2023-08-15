package com.mychore.mychore_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing
public class MyChoreServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyChoreServerApplication.class, args);
    }

}
