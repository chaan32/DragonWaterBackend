package com.dragonwater.backend.Config.Webclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebclientApiConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Qualifier("kakaoApiClient")
    public WebClient kakaoApiClient(WebClient.Builder webClientBuilder) {
        return WebClient.builder()
                // URL 넣으면 됨
                .baseUrl("")
                .build();
    }

    @Bean
    @Qualifier("smsApiClient")
    public WebClient smsApiClient(WebClient.Builder webClientBuilder) {
        return WebClient.builder()
                .build();
    }
}
