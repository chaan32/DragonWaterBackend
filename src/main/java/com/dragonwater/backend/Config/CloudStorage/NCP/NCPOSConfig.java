package com.dragonwater.backend.Config.CloudStorage.NCP;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NCPOSConfig {
    @Value("${naver.object-storage.region}")
    private String region;

    @Value("${naver.object-storage.endpoint}")
    private String endPoint;

    @Value("${naver.access-key}")
    private String accessKey;

    @Value("${naver.secret-key}")
    private String secretKey;
    @Bean
    public AmazonS3Client ncpOS() {
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }
}
