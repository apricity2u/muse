package com.example.muse.domain.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
public class S3Config {
    @Value("${REGION}")
    private String region;
    @Value("${ACCESS_KEY}")
    private String accessKey;
    @Value("${SECRET_KEY}")
    private String secretKey;


    @Bean
    public S3Client s3Client() {

        StaticCredentialsProvider credential = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credential)
                .build();
    }
}

