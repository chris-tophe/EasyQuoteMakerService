package com.christophe.quoteService.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptManager {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BCryptManager(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Bean(name = "passwordEncoder")
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
