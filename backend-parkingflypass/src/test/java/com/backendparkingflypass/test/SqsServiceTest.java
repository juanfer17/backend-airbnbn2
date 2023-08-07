package com.backendparkingflypass.test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.SdkInternalMap;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.backendparkingflypass.config.AWSClient;
import com.backendparkingflypass.config.AwsProperties;
import com.backendparkingflypass.general.constansts.Constants;
import com.backendparkingflypass.service.SnsService;
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
    @Mock
    private AWSClient awsClient;
    private AwsProperties awsProperties;
    private AWSStaticCredentialsProvider awsStaticCredentialsProvider;
    private AmazonSQSClientBuilder amazonSQSClientBuilder;
    @InjectMocks
    private SqsService sqsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAmazonSNS() {
        amazonSQSClientBuilder = AmazonSQSClientBuilder.standard();

        awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials("accessKey", "secretKey"));
        amazonSQSClientBuilder.withCredentials(awsStaticCredentialsProvider);

        sqsService = new SqsService(awsClient);

        assertNotNull(sqsService.getAmazonSQS());
    }
    @Test
    public void testGetMessages() {
        AmazonSQS mockAmazonSQS = Mockito.mock(AmazonSQS.class);
        // Configure the mock to return a single message
        Message message = new Message();
        ReceiveMessageResult receiveMessageResult = new ReceiveMessageResult();
        receiveMessageResult.setMessages(Arrays.asList(message));
        Mockito.when(mockAmazonSQS.receiveMessage(Mockito.any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult);

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
    }


    @Test
    public void testDeleteMessages() {
        // Crear un objeto AmazonSQS mock
        AmazonSQS mockAmazonSQS = Mockito.mock(AmazonSQS.class);

        // Crear una respuesta simulada para el método deleteMessageBatch
        DeleteMessageBatchResult deleteMessageBatchResult = new DeleteMessageBatchResult();
        Mockito.when(mockAmazonSQS.deleteMessageBatch(Mockito.any())).thenReturn(deleteMessageBatchResult);

        // Crear una lista simulada de mensajes a eliminar
        List<Message> deleteEntries = new ArrayList<>();
        DeleteMessageBatchRequest deleteEntriesDelete = new DeleteMessageBatchRequest();
        // Agregar entradas simuladas a la lista...

        // Llamar al método deleteMessages()
        sqsService.deleteMessages(deleteEntries, "queueUrl");
    }
}
