package com.backendparkingflypass.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.backendparkingflypass.general.constansts.Constants;
import org.springframework.stereotype.Component;

@Component
public class AWSClient {
    public static final String REGION = Regions.US_EAST_1.getName();
    public static String TRANSACTION_QUEUE_URL;
    private final AwsProperties awsProperties;
    private AWSStaticCredentialsProvider awsStaticCredentialsProvider;

    public AWSClient(ApplicationProperties applicationProperties, AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
        TRANSACTION_QUEUE_URL = String.format("%s%s", applicationProperties.getProfile(), Constants.TRANSACTIONS_QUEUE);
    }

    public AWSStaticCredentialsProvider getAWSCredentials() {
        if(awsStaticCredentialsProvider == null) {
            BasicAWSCredentials awsCreeds = new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());
            awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(awsCreeds);
        }
        return awsStaticCredentialsProvider;
    }
}
