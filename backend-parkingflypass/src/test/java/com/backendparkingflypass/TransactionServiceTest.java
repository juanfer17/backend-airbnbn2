package com.backendparkingflypass;

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
    @Mock
    private ParkingTransaction parkingRepository;
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAndSaveTransaction() {
        // Crear un mock de RequestTransactionCreateDTO
        RequestTransactionCreateDTO mockRequest = mock(RequestTransactionCreateDTO.class);
        when(mockRequest.getTransactionId()).thenReturn("transactionId");
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
        transaction1.setTransactionId("1231231");
        transaction1.setTransactionId("0");
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
    public void testCreateTransaction() {
        // Crear una solicitud de transacción de ejemplo
        RequestTransactionCreateDTO requestTransactionCreate = new RequestTransactionCreateDTO();
        requestTransactionCreate.setTransactionId("123123");
        requestTransactionCreate.setTransactionStatus(EnumTransactionStatus.STARTED.getId());
        requestTransactionCreate.setPlate("LMA530");
        requestTransactionCreate.setVehicleType("CAMION");

        try{
            // Crear mensajes de ejemplo
            List<Message> messages = new ArrayList<>();
            String jsonBody = "{\n" +
                    "  \"Type\" : \"Notification\",\n" +
                    "  \"MessageId\" : \"10417b42-302a-59e4-9ec0-26082408f18f\",\n" +
                    "  \"TopicArn\" : \"arn:aws:sns:us-east-1:332721419741:dev_parking_flypass\",\n" +
                    "  \"Message\" : \"{\\\"transactionId\\\":\\\"2222\\\",\\\"plate\\\":\\\"AAA222\\\",\\\"transactionStatus\\\":0,\\\"vehicleType\\\":\\\"AUTOMOVIL\\\"}\",\n" +
                    "  \"Timestamp\" : \"2023-08-06T21:34:26.273Z\",\n" +
                    "  \"SignatureVersion\" : \"1\",\n" +
                    "  \"Signature\" : \"Nv5w1lEzUBIbiEzH0UhAAMh3GUIHSA/toNRY+ZMp+O35uLnopVScsabHPlqbRgtmMDy27btPYcTJ28Jki3/PJwT8LDaTkLn0I5BQemAN7I1gpj8gbm7AMCZpYmf4HnH15TmgrKiAqpufpccIia+n0syOi1oHqvm8LYJJ97qMh0YRMwawlp1JRiCSHbf1tDhAPXNBe1M/76kzbF/y7K5ZBS1rZbu/xMwK+CypLj7NczXd0Q==\",\n" +
                    "  \"SigningCertURL\" : \"https://sns.us-east-1.amazonaws.com/SimpleNotificationService-01d088a6f77103d0fe307c0069e40ed6.pem\",\n" +
                    "  \"UnsubscribeURL\" : \"https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:332721419741:dev_parking_flypass:5b2872a2-efb3-413b-a9ef-ecb2946b4e7c\"\n" +
                    "}";
            Message message1 = new Message();
            message1.setBody(jsonBody);
            messages.add(message1);

            ApplicationProperties applicationProperties = new ApplicationProperties();
            AwsProperties awsProperties = new AwsProperties();
            awsProperties.setAccessKey("DSADSA15DSA15D6SA");
            awsProperties.setSecretKey("DAS/QiLvuCx8tzmzhhNcS6yoC2JaZ4Xu");
            AWSClient awsClient = new AWSClient(applicationProperties , awsProperties);
            DynamoClient dynamoClient = new DynamoClient(applicationProperties,awsClient);
            assertNotNull(transactionService.transactionIdValidation(requestTransactionCreate.getTransactionId(), ParkingTransaction.class));

            // Llamar al método bajo prueba
            List<Message> messagesToDelete = transactionService.createTransaction(messages);
            // Verificar que el transactionService haya sido invocado
            Mockito.verify(transactionService, times(1)).createTransaction(messages);
            // Verificar que el método haya retornado la lista de mensajes correctamente
            assertEquals(messages, messagesToDelete);
        }catch (Exception e){
            assertThrows(Exception.class, () -> Optional.ofNullable(transactionService).orElseThrow(Exception::new).transactionValidation(requestTransactionCreate.getTransactionId(), ParkingTransaction.class));
        }

    }

}
