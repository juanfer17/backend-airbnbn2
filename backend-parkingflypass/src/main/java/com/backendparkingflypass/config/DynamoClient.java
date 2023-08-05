package com.backendparkingflypass.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoClient {

    private static DynamoDBMapper mapper;

    public DynamoClient(ApplicationProperties applicationProperties, AWSClient awsClient) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(awsClient.getAWSCredentials())
                .withRegion(AWSClient.REGION).build();
        DynamoDBMapperConfig config = new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(applicationProperties.getProfile()))
                .build();
        mapper = new DynamoDBMapper(client, config);
    }

    public static DynamoDBMapper getInstance() {
        return mapper;
    }
}
