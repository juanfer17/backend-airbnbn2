package com.backendparkingflypass.test;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.backendparkingflypass.config.AWSClient;
import com.backendparkingflypass.config.AwsProperties;
import com.backendparkingflypass.service.SnsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SnsServiceTest {
    @Mock
    private AWSClient awsClient;
    private AwsProperties awsProperties;
    private AWSStaticCredentialsProvider awsStaticCredentialsProvider;
    @Mock
    private AmazonSNS amazonSNS;
    private AmazonSNSClientBuilder amazonSNSClientBuilder;
    @InjectMocks
    private SnsService snsService;

    @BeforeEach
    public void setup() {
        // Inicializar los mocks o espías
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAmazonSNS() {
        amazonSNSClientBuilder = AmazonSNSClientBuilder.standard();

        awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials("accessKey", "secretKey"));
        amazonSNSClientBuilder.withCredentials(awsStaticCredentialsProvider);

        snsService = new SnsService(awsClient, awsProperties);

        assertNotNull(snsService.getAmazonSNS());
    }

    @Test
    public void testSendToSNS() {
        String message = "Este es un mensaje de prueba para enviar a SNS";
        String arnParkingTransaction = "arn:aws:sns:us-east-1:1234567890:my-topic";
        PublishResult publishResult = new PublishResult().withMessageId("123456");
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.withMessage(message);
        publishRequest.withTopicArn(arnParkingTransaction);

        Mockito.when(amazonSNS.publish(publishRequest)).thenReturn(publishResult);

        snsService = new SnsService(awsClient, awsProperties);


        snsService.sendToSNS(publishRequest.getMessage(), publishRequest.getTopicArn());




    }

}
