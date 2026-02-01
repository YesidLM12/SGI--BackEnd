package com.tracker.sgi.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
@EnableConfigurationProperties(JwtProperties.class)
public class JwtProperties {
    private String secret;
    private long expiration;
}
