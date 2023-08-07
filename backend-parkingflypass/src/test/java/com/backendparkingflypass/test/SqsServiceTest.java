package com.backendparkingflypass.test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.auth.BasicAWSCredentials;
import com.backendparkingflypass.config.AWSClient;
import com.backendparkingflypass.general.constansts.Constants;
import com.backendparkingflypass.service.SqsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;

import java.util.*;

public class SqsServiceTest {

    @Mock
    private AmazonSQS amazonSQS;



    @InjectMocks
    private SqsService sqsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetMessages() {
        // Configure the mock to return a single message
        Message message = new Message();
        ReceiveMessageResult receiveMessageResult = new ReceiveMessageResult();
        receiveMessageResult.setMessages(Arrays.asList(message));
        Mockito.when(amazonSQS.receiveMessage(Mockito.any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult);

        // Call the getMessages() method
        Message actualMessage = (Message) sqsService.getMessages("queueUrl");

        // Assert that the expected message was returned
        assertEquals(message, actualMessage);
    }

    @Test
    public void testGetMessagesCount() {
        // Create a mock AmazonSQS object
        AmazonSQS mockAmazonSQS = Mockito.mock(AmazonSQS.class);

        // Configure the mock to return the number of messages
        int messagesCount = 2;
        Mockito.when(mockAmazonSQS.getQueueAttributes((GetQueueAttributesRequest) Mockito.any())).thenReturn(new GetQueueAttributesResult().withAttributes(
                Map.of(Constants.APROXIMATE_NUMBER_OF_MESSAGES, String.valueOf(messagesCount))
        ));

        // Call the getMessagesCount() method
        int actualMessagesCount = sqsService.getMessagesCount("queueUrl");

        // Assert that the expected messages count was returned
        assertEquals(messagesCount, actualMessagesCount);
    }


    @Test
    public void testDeleteMessages() {
        // Create a mock AmazonSQS object
        AmazonSQS mockAmazonSQS = Mockito.mock(AmazonSQS.class);

        // Configure the mock to delete the messages
        Mockito.doNothing().when(mockAmazonSQS).deleteMessageBatch(Mockito.any());

        // Call the deleteMessages() method
        sqsService.deleteMessages(new ArrayList<>(), "queueUrl");

        // Verify that the mock was called
        Mockito.verify(mockAmazonSQS, Mockito.times(1)).deleteMessageBatch(Mockito.any());
    }
}
