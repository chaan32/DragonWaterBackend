package com.dragonwater.backend.Config.Jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {

    // Value를 통해서 가져오지 않아도 자동으로 맵핑 됨 -> @ConfigurationProperties("jwt") 덕분에
    private String issuer;
    private String secretKey;
}
