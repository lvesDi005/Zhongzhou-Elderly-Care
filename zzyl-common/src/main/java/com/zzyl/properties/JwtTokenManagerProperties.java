package com.zzyl.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * jw配置文件
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "zzyl.framework.jwt")
public class JwtTokenManagerProperties implements Serializable {

    /**
     *  签名密码
     */
    private String base64EncodedSecretKey;

    /**
     *  有效时间
     */
    private Integer ttl;
    
}