package org.lgazdek.diplomski.todo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfiguration {
    @Value("${aws.accessKey}")
    private String accessKey;
    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;
    @Value("${aws.region}")
    private String region;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretAccessKey));
        return DynamoDbClient
            .builder()
            .credentialsProvider(staticCredentialsProvider)
            .region(Region.of(region))
            .httpClientBuilder(ApacheHttpClient.builder())
            .build();
    }
}
