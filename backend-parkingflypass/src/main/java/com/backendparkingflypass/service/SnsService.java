package com.backendparkingflypass.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.backendparkingflypass.config.AWSClient;
import com.backendparkingflypass.config.AwsProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SnsService {

    private static final Logger logger = LogManager.getLogger(SnsService.class);
    private final AWSClient awsClient;
    private AmazonSNS amazonSNS;
    private final AwsProperties awsProperties;

    public SnsService(AWSClient awsClient, AwsProperties awsProperties){
        this.awsClient = awsClient;
        this.awsProperties = awsProperties;
    }

    public AmazonSNS getAmazonSNS(){
        if(amazonSNS == null){
            amazonSNS = AmazonSNSClientBuilder.standard().withRegion(AWSClient.REGION).withCredentials(awsClient.getAWSCredentials()).build();
        }
        return amazonSNS;
    }

    public void sendToSNS(String message, String arnParkingTransaction){
        logger.info("Inicio del envio del mensaje a SNS");
        try{
            PublishResult result = getAmazonSNS().publish(new PublishRequest()
                    .withTopicArn(arnParkingTransaction)
                    .withMessage(message)
            );
            logger.info("mensaje enviado al SNS con exito id: "+result.getMessageId());
        }catch(Exception e){
            logger.error("error en el envio del mensaje al SNS: "+e.getMessage());
        }
    }
}
