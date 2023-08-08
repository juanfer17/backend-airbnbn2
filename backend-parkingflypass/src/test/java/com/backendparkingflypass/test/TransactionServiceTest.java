package com.backendparkingflypass.test;

import com.amazonaws.services.sqs.model.Message;
import com.backendparkingflypass.config.AWSClient;
import com.backendparkingflypass.config.ApplicationProperties;
import com.backendparkingflypass.config.AwsProperties;
import com.backendparkingflypass.config.DynamoClient;
import com.backendparkingflypass.dto.RequestTransactionCreateDTO;
import com.backendparkingflypass.dto.RequestTransactionTerminatedDTO;
import com.backendparkingflypass.general.enums.EnumTransactionStatus;
import com.backendparkingflypass.general.exception.NoDataFoundException;
import com.backendparkingflypass.general.utils.DateTimeUtils;
import com.backendparkingflypass.model.ParkingTransaction;
import com.backendparkingflypass.repository.ParkingTransactionRepository;
import com.backendparkingflypass.service.MessagesService;
import com.backendparkingflypass.service.SnsService;
import com.backendparkingflypass.service.TransactionService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private AwsProperties awsProperties;
    @Mock
    private SnsService snsService;
    @Mock
    private MessagesService messagesService;

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private ParkingTransactionRepository parkingTransactionRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAndSaveTransaction() {
        // Crear un mock de RequestTransactionCreateDTO
        RequestTransactionCreateDTO mockRequest = mock(RequestTransactionCreateDTO.class);
        when(mockRequest.getPlate()).thenReturn("ABC123");
        when(mockRequest.getVehicleType()).thenReturn("car");

        // Crear un mock de ParkingTransaction
        ParkingTransaction mockParkingTransaction = mock(ParkingTransaction.class);

        // Mockear el método save de ParkingTransaction
        doNothing().when(mockParkingTransaction).save();

        // Llamar a la función bajo prueba
        transactionService.createAndSaveTransaction(mockRequest);
    }

    @Test
    public void testTerminateTransaction() {
        // Create a mock ParkingTransaction object
        ParkingTransaction parkingTransaction = new ParkingTransaction();
        parkingTransaction.setEntryDate(new Date());
        Date exitDate = DateTimeUtils.convertLocalToUTC(new Date());

        transactionService.terminateTransaction(parkingTransaction);

        // Assert that the time service was set correctly
        long millisecondsDifference = exitDate.getTime() - parkingTransaction.getEntryDate().getTime();
        long minutes = millisecondsDifference / (60 * 1000); // Convertir milisegundos a minutos
        int minutesAsInt = (int) minutes;
        assertEquals(minutesAsInt, parkingTransaction.getTimeService());
    }

    @Test
    public void testSendTransactionToSNS() {
        // Crear una lista de transacciones de prueba
        List<RequestTransactionCreateDTO> transactions = new ArrayList<>();
        // Agregar transacciones a la lista
        RequestTransactionCreateDTO transaction1 = new RequestTransactionCreateDTO();
        transaction1.setPlate("ABC123");
        // Configurar las propiedades de la transacción 1
        transactions.add(transaction1);
        // Configurar el comportamiento simulado del snsService y awsProperties
        when(awsProperties.getArnEntryParkingSns()).thenReturn("arnEntry");
        doNothing().when(snsService).sendToSNS(anyString(), eq("arnEntry"));

        // Llamar al método bajo prueba
        transactionService.sendTransactionToSNS(transactions);

        // Verificar que sendSNSTransaction se llamó para cada transacción en la lista
        verify(snsService, times(transactions.size())).sendToSNS(anyString(), eq("arnEntry"));
    }

    @Test
    public void testSendEndTransactionToSNS() {
        // Crear una lista de transacciones de prueba
        List<RequestTransactionTerminatedDTO> transactions = new ArrayList<>();

        // Agregar transacciones a la lista
        RequestTransactionTerminatedDTO transaction1 = new RequestTransactionTerminatedDTO();
        transaction1.setTransactionId("1231231");
        // Configurar las propiedades de la transacción 1
        transactions.add(transaction1);

        // Configurar el comportamiento simulado del snsService y awsProperties
        when(awsProperties.getArnExitParkingSns()).thenReturn("arnExit");
        doNothing().when(snsService).sendToSNS(anyString(), eq("arnExit"));

        // Llamar al método bajo prueba
        transactionService.sendEndTransactionToSNS(transactions);

        // Verificar que sendSNSTransactionTerminated se llamó para cada transacción en la lista
        verify(snsService, times(transactions.size())).sendToSNS(anyString(), eq("arnExit"));
    }
    @Test
    public void testTransactionIdValidation() {
        // Crear una solicitud de transacción de ejemplo
        RequestTransactionCreateDTO requestTransactionCreate = new RequestTransactionCreateDTO();
        requestTransactionCreate.setPlate("LMA530");
        requestTransactionCreate.setVehicleType("CAMION");
        try {
            ApplicationProperties applicationProperties = new ApplicationProperties();
            AwsProperties awsProperties = new AwsProperties();
            awsProperties.setAccessKey("DSADSA15DSA15D6SA");
            awsProperties.setSecretKey("DAS/QiLvuCx8tzmzhhNcS6yoC2JaZ4Xu");
            AWSClient awsClient = new AWSClient(applicationProperties, awsProperties);
            DynamoClient dynamoClient = new DynamoClient(applicationProperties, awsClient);
            assertNull(transactionService.transactionIdValidation("1312", ParkingTransaction.class));
        } catch (Exception e) {
            assertThrows(Exception.class, () -> Optional.ofNullable(transactionService).orElseThrow(Exception::new).transactionValidation("123123", ParkingTransaction.class));
        }
    }

    @Test
    public void testTransactionValidation() {
        // Crear una solicitud de transacción de ejemplo
        RequestTransactionCreateDTO requestTransactionCreate = new RequestTransactionCreateDTO();
        requestTransactionCreate.setPlate("LMA530");
        requestTransactionCreate.setVehicleType("CAMION");
        try{
            ApplicationProperties applicationProperties = new ApplicationProperties();
            AwsProperties awsProperties = new AwsProperties();
            awsProperties.setAccessKey("DSADSA15DSA15D6SA");
            awsProperties.setSecretKey("DAS/QiLvuCx8tzmzhhNcS6yoC2JaZ4Xu");
            AWSClient awsClient = new AWSClient(applicationProperties , awsProperties);
            DynamoClient dynamoClient = new DynamoClient(applicationProperties,awsClient);
            assertNull(transactionService.transactionValidation(requestTransactionCreate.getPlate(), ParkingTransaction.class));
        }catch (Exception e){
            assertThrows(Exception.class, () -> Optional.ofNullable(transactionService).orElseThrow(Exception::new).transactionValidation("123123", ParkingTransaction.class));
        }
    }

    @Test
    public void testCreateTransaction() {
        // Crear mensajes de ejemplo
        List<Message> messages = new ArrayList<>();
        String jsonBody = "{\n" +
                "  \"Type\" : \"Notification\",\n" +
                "  \"MessageId\" : \"10417b42-302a-59e4-9ec0-26082408f18f\",\n" +
                "  \"TopicArn\" : \"arn:aws:sns:us-east-1:332721419741:dev_parking_flypass\",\n" +
                "  \"Message\" : \"{\\\"plate\\\":\\\"AAA222\\\",\\\"vehicleType\\\":\\\"AUTOMOVIL\\\"}\",\n" +
                "  \"Timestamp\" : \"2023-08-06T21:34:26.273Z\",\n" +
                "  \"SignatureVersion\" : \"1\",\n" +
                "  \"Signature\" : \"Nv5w1lEzUBIbiEzH0UhAAMh3GUIHSA/toNRY+ZMp+O35uLnopVScsabHPlqbRgtmMDy27btPYcTJ28Jki3/PJwT8LDaTkLn0I5BQemAN7I1gpj8gbm7AMCZpYmf4HnH15TmgrKiAqpufpccIia+n0syOi1oHqvm8LYJJ97qMh0YRMwawlp1JRiCSHbf1tDhAPXNBe1M/76kzbF/y7K5ZBS1rZbu/xMwK+CypLj7NczXd0Q==\",\n" +
                "  \"SigningCertURL\" : \"https://sns.us-east-1.amazonaws.com/SimpleNotificationService-01d088a6f77103d0fe307c0069e40ed6.pem\",\n" +
                "  \"UnsubscribeURL\" : \"https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:332721419741:dev_parking_flypass:5b2872a2-efb3-413b-a9ef-ecb2946b4e7c\"\n" +
                "}";
        Message message1 = new Message();
        message1.setBody(jsonBody);
        messages.add(message1);

        // Crear un objeto mock de TransactionService
        TransactionService transactionServiceMock = Mockito.mock(TransactionService.class);

        // Configurar el comportamiento simulado del transactionService
        Mockito.when(transactionServiceMock.transactionValidation(Mockito.eq("ASD123"), Mockito.any(Class.class))).thenReturn(null);


        // Llamar al método bajo prueba
        List<Message> messagesToDelete = transactionService.createTransaction(messages);

    }

    @Test
    public void testCreateTransactionParkingTransactionId_NotNull() {
        // Crear mensajes de ejemplo
        List<Message> messages = new ArrayList<>();
        String jsonBody = "{\n" +
                "  \"Type\" : \"Notification\",\n" +
                "  \"MessageId\" : \"10417b42-302a-59e4-9ec0-26082408f18f\",\n" +
                "  \"TopicArn\" : \"arn:aws:sns:us-east-1:332721419741:dev_parking_flypass\",\n" +
                "  \"Message\" : \"{\\\"transactionId\\\":\\\"2222\\\",\\\"plate\\\":\\\"AAA222\\\",\\\"vehicleType\\\":\\\"AUTOMOVIL\\\"}\",\n" +
                "  \"Timestamp\" : \"2023-08-06T21:34:26.273Z\",\n" +
                "  \"SignatureVersion\" : \"1\",\n" +
                "  \"Signature\" : \"Nv5w1lEzUBIbiEzH0UhAAMh3GUIHSA/toNRY+ZMp+O35uLnopVScsabHPlqbRgtmMDy27btPYcTJ28Jki3/PJwT8LDaTkLn0I5BQemAN7I1gpj8gbm7AMCZpYmf4HnH15TmgrKiAqpufpccIia+n0syOi1oHqvm8LYJJ97qMh0YRMwawlp1JRiCSHbf1tDhAPXNBe1M/76kzbF/y7K5ZBS1rZbu/xMwK+CypLj7NczXd0Q==\",\n" +
                "  \"SigningCertURL\" : \"https://sns.us-east-1.amazonaws.com/SimpleNotificationService-01d088a6f77103d0fe307c0069e40ed6.pem\",\n" +
                "  \"UnsubscribeURL\" : \"https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:332721419741:dev_parking_flypass:5b2872a2-efb3-413b-a9ef-ecb2946b4e7c\"\n" +
                "}";
        Message message1 = new Message();
        message1.setBody(jsonBody);
        messages.add(message1);
        // Configurar el comportamiento simulado del transactionService para el caso del else
        ParkingTransaction parkingTransaction = new ParkingTransaction();
        Mockito.when(transactionService.transactionIdValidation("2222", ParkingTransaction.class)).thenReturn(parkingTransaction);

        // Llamar al método bajo prueba
        List<Message> messagesToDelete = transactionService.createTransaction(messages);

        // Verificar resultados esperados para el caso del else
        // Aquí puedes realizar las verificaciones necesarias según lo que esperas que suceda cuando el transactionIdValidation devuelve un objeto no nulo
    }

    @Test
    public void testTerminateTransactionFromQeue() {
        // Crear mensajes de ejemplo
        List<Message> messages = new ArrayList<>();
        String jsonBody = "{\n" +
                "  \"Type\" : \"Notification\",\n" +
                "  \"MessageId\" : \"e9288cfe-a059-50f4-b89d-d1b5a77a3385\",\n" +
                "  \"TopicArn\" : \"arn:aws:sns:us-east-1:332721419741:dev_parking_flypass_exit\",\n" +
                "  \"Message\" : \"{\\\"transactionId\\\":\\\"3333\\\"}\",\n" +
                "  \"Timestamp\" : \"2023-08-07T14:51:15.582Z\",\n" +
                "  \"SignatureVersion\" : \"1\",\n" +
                "  \"Signature\" : \"LQeVWK+GK2ufwsrPysH/+y14DWgzn8gbobQi2QSER5mrmV9ATQJNBkB9JSpQK3e8xonp+gp3GCuywIbk/qEDXOkeUSuWyn/BpqClhNk1O9YcxYAb15dpqSnSgK+vsNI/XgAk0aPloo9sjUkYpR+9QhsfCMeGuW21g4EVhuzWAu+36U6cgVx55p47uFaxI3DFGiti7DYy3b3IGGdAlcdtiw6YegeN5c2+c/O0gi83LtsqJCVZWFEeWcsjxTTYMczIUYVj1R+dkxW/IzbBsPfrT4m+DXn6dAFqrhvnUW1PqAlC0KIhC6dOb8xFPt5s/7dQr2SOVpqHfOshdzexUgMVzA==\",\n" +
                "  \"SigningCertURL\" : \"https://sns.us-east-1.amazonaws.com/SimpleNotificationService-01d088a6f77103d0fe307c0069e40ed6.pem\",\n" +
                "  \"UnsubscribeURL\" : \"https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:332721419741:dev_parking_flypass_exit:9e02e52c-83db-462a-93e8-83db166ab60c\"\n" +
                "}";
        Message message1 = new Message();
        message1.setBody(jsonBody);
        messages.add(message1);

        RequestTransactionTerminatedDTO requestTransactionTerminatedDTO = new RequestTransactionTerminatedDTO();
        requestTransactionTerminatedDTO.setTransactionId("161196");

        ParkingTransaction parkingTransactionValidation = new ParkingTransaction();
        parkingTransactionValidation.setTransactionId("161196");
        parkingTransactionValidation.setPlate("ABC123");
        parkingTransactionValidation.setTransactionStatus(0);

        // Configurar el comportamiento simulado del transactionService
        Mockito.when(transactionService.transactionIdValidation("161196", Mockito.any(Class.class))).thenReturn(parkingTransactionValidation);

        // Llamar al método bajo prueba
        List<Message> messagesToDelete = transactionService.terminateTransactionFromQeue(messages);

    }


    @Test
    public void testTerminateTransactionFromQeueParkingTransaction_Null() {
        // Crear mensajes de ejemplo
        List<Message> messages = new ArrayList<>();
        String jsonBody = "{\n" +
                "  \"Type\" : \"Notification\",\n" +
                "  \"MessageId\" : \"e9288cfe-a059-50f4-b89d-d1b5a77a3385\",\n" +
                "  \"TopicArn\" : \"arn:aws:sns:us-east-1:332721419741:dev_parking_flypass_exit\",\n" +
                "  \"Message\" : \"{\\\"transactionId\\\":\\\"3333\\\"}\",\n" +
                "  \"Timestamp\" : \"2023-08-07T14:51:15.582Z\",\n" +
                "  \"SignatureVersion\" : \"1\",\n" +
                "  \"Signature\" : \"LQeVWK+GK2ufwsrPysH/+y14DWgzn8gbobQi2QSER5mrmV9ATQJNBkB9JSpQK3e8xonp+gp3GCuywIbk/qEDXOkeUSuWyn/BpqClhNk1O9YcxYAb15dpqSnSgK+vsNI/XgAk0aPloo9sjUkYpR+9QhsfCMeGuW21g4EVhuzWAu+36U6cgVx55p47uFaxI3DFGiti7DYy3b3IGGdAlcdtiw6YegeN5c2+c/O0gi83LtsqJCVZWFEeWcsjxTTYMczIUYVj1R+dkxW/IzbBsPfrT4m+DXn6dAFqrhvnUW1PqAlC0KIhC6dOb8xFPt5s/7dQr2SOVpqHfOshdzexUgMVzA==\",\n" +
                "  \"SigningCertURL\" : \"https://sns.us-east-1.amazonaws.com/SimpleNotificationService-01d088a6f77103d0fe307c0069e40ed6.pem\",\n" +
                "  \"UnsubscribeURL\" : \"https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:332721419741:dev_parking_flypass_exit:9e02e52c-83db-462a-93e8-83db166ab60c\"\n" +
                "}";
        Message message1 = new Message();
        message1.setBody(jsonBody);
        messages.add(message1);

        RequestTransactionTerminatedDTO requestTransactionTerminatedDTO = new RequestTransactionTerminatedDTO();
        requestTransactionTerminatedDTO.setTransactionId("161196");

        ParkingTransaction parkingTransactionValidation = new ParkingTransaction();
        parkingTransactionValidation.setTransactionId("161196");
        parkingTransactionValidation.setPlate("ABC123");
        parkingTransactionValidation.setTransactionStatus(0);

        // Configurar el comportamiento simulado del transactionService
        Mockito.when(transactionService.transactionIdValidation("161196", Mockito.any(Class.class))).thenReturn(null);

        // Llamar al método bajo prueba
        List<Message> messagesToDelete = transactionService.terminateTransactionFromQeue(messages);

    }




}
