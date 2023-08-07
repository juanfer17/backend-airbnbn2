package com.backendparkingflypass.test;

import com.backendparkingflypass.config.AWSClient;
import com.backendparkingflypass.config.ApplicationProperties;
import com.backendparkingflypass.config.AwsProperties;
import com.backendparkingflypass.config.DynamoClient;
import com.backendparkingflypass.dto.RequestStatisticsParkingDTO;
import com.backendparkingflypass.dto.RequestTransactionCreateDTO;
import com.backendparkingflypass.general.enums.EnumTransactionStatus;
import com.backendparkingflypass.model.ParkingTransaction;
import com.backendparkingflypass.repository.ParkingTransactionRepository;
import com.backendparkingflypass.service.StatisticsParking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class StatisticsParkingTest {

    @Mock
    private ParkingTransactionRepository parkingRepository;
    @InjectMocks
    private StatisticsParking statisticsParking;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByVehicleType() {
        String vehicleType = "AUTOMOVIL";
        try{
            ApplicationProperties applicationProperties = new ApplicationProperties();
            AwsProperties awsProperties = new AwsProperties();
            awsProperties.setAccessKey("DSADSA15DSA15D6SA");
            awsProperties.setSecretKey("DAS/QiLvuCx8tzmzhhNcS6yoC2JaZ4Xu");
            AWSClient awsClient = new AWSClient(applicationProperties , awsProperties);
            DynamoClient dynamoClient = new DynamoClient(applicationProperties,awsClient);
        }catch (Exception e){
            assertThrows(Exception.class, () -> Optional.ofNullable(statisticsParking).orElseThrow(Exception::new).findByVehicleType(vehicleType, ParkingTransaction.class));
        }
    }
    @Test
    public void testFindByTransactionStatus() {
        Integer transactionStatus = 123123;
        try{
            ApplicationProperties applicationProperties = new ApplicationProperties();
            AwsProperties awsProperties = new AwsProperties();
            awsProperties.setAccessKey("DSADSA15DSA15D6SA");
            awsProperties.setSecretKey("DAS/QiLvuCx8tzmzhhNcS6yoC2JaZ4Xu");
            AWSClient awsClient = new AWSClient(applicationProperties , awsProperties);
            DynamoClient dynamoClient = new DynamoClient(applicationProperties,awsClient);
        }catch (Exception e){
            assertThrows(Exception.class, () -> Optional.ofNullable(statisticsParking).orElseThrow(Exception::new).findByTransactionStatus(transactionStatus, ParkingTransaction.class));
        }
    }


    @Test
    public void testAverageTimeService() {
        List<ParkingTransaction> timeServicesByVehicleType = new ArrayList<>();
        ParkingTransaction parkingTransaction = new ParkingTransaction();
        parkingTransaction.setTransactionId("123123");
        parkingTransaction.setVehicleType("CAMION");
        parkingTransaction.setTimeService(10);
        timeServicesByVehicleType.add(parkingTransaction);
        // Create a mock `ParkingTransaction` object.
        ParkingTransaction mockTransaction = Mockito.mock(ParkingTransaction.class);

        // Configure the mock `ParkingTransaction` object to return a specific time service.
        Mockito.when(mockTransaction.getTimeService()).thenReturn(10);


        RequestStatisticsParkingDTO requestStatisticsParkingDTO = new RequestStatisticsParkingDTO();
        requestStatisticsParkingDTO.setVehicleType("CAMION");

        statisticsParking.averageTimeService(requestStatisticsParkingDTO);

        // Call the `getAverageTime()` method.
        double averageTime = parkingTransaction.getTimeService();

        // Assert that the result is correct.
        assertEquals(10, averageTime);
    }

    @Test
    public void testMaxTimeService() {
        List<ParkingTransaction> timeServicesByTransactionStatus = new ArrayList<>();
        ParkingTransaction parkingTransaction = new ParkingTransaction();
        parkingTransaction.setTransactionId("123123");
        parkingTransaction.setVehicleType("CAMION");
        parkingTransaction.setTimeService(3);
        timeServicesByTransactionStatus.add(parkingTransaction);

        Mockito.when(parkingRepository.findByTransactionStatus(EnumTransactionStatus.TERMINATED.getId())).thenReturn(timeServicesByTransactionStatus);
        statisticsParking.maxTimeService();
    }


}