package com.bankapp.v1.config_server;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class EnvChecker {

    @PostConstruct
    public void check() {
        System.out.println("This is the GIT_USERNAME=" + System.getenv("GIT_USERNAME"));
        System.out.println("This is the GIT_PASSWORD=" + System.getenv("GIT_PASSWORD"));
    }
}
