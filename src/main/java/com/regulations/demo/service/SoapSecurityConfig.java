package com.regulations.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

@Configuration
public class SoapSecurityConfig {

    @Bean
    public Wss4jSecurityInterceptor securityInterceptor() {
        Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();

        interceptor.setSecurementActions("UsernameToken");
        interceptor.setSecurementUsername("n00mk0ap");     // TODO: externalize
        interceptor.setSecurementPassword("ZLxtG2ep10V");  // TODO: externalize
        interceptor.setSecurementPasswordType("PasswordText");

        return interceptor;
    }
}
