package com.backendparkingflypass.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.backendparkingflypass.general.constansts.Constants;
import org.springframework.stereotype.Component;

@Component
public class AWSClient {
    public static final String REGION = Regions.US_EAST_1.getName();
    public static String TRANSACTION_SNS_URL;
    public static String TRANSACTION_SQS_URL;

    public static String TRANSACTION_EXIT_SNS_URL;
    public static String TRANSACTION_EXIT_SQS_URL;
    private final AwsProperties awsProperties;
    private AWSStaticCredentialsProvider awsStaticCredentialsProvider;

    public AWSClient(ApplicationProperties applicationProperties, AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
        TRANSACTION_SNS_URL = String.format("%s%s", applicationProperties.getProfile(), Constants.TRANSACTIONS_SNS);
        TRANSACTION_SQS_URL = String.format("%s%s", applicationProperties.getProfile(), Constants.TRANSACTIONS_SQS);
        TRANSACTION_EXIT_SNS_URL = String.format("%s%s", applicationProperties.getProfile(), Constants.TRANSACTIONS_EXIT_SNS);
        TRANSACTION_EXIT_SQS_URL = String.format("%s%s", applicationProperties.getProfile(), Constants.TRANSACTIONS_EXIT_SQS);
    }

    public AWSStaticCredentialsProvider getAWSCredentials() {
        if(awsStaticCredentialsProvider == null) {
            BasicAWSCredentials awsCreeds = new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());
            awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(awsCreeds);
        }
        return awsStaticCredentialsProvider;
    }
}
