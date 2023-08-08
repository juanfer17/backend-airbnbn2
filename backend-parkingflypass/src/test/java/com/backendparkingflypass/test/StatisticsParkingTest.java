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
        parkingTransaction.setVehicleType("CAMION");
        parkingTransaction.setTimeService(10);
        timeServicesByVehicleType.add(parkingTransaction);

        // Mock the findByVehicleType method to return the predefined list
        Mockito.when(statisticsParking.findByVehicleType("CAMION", ParkingTransaction.class)).thenReturn(timeServicesByVehicleType);

        // Call the `averageTimeService` method.
        RequestStatisticsParkingDTO requestStatisticsParkingDTO = new RequestStatisticsParkingDTO();
        requestStatisticsParkingDTO.setVehicleType("CAMION");
        String result = statisticsParking.averageTimeService(requestStatisticsParkingDTO);

        // Verify the result
        String expectedMessage = "Tiempo promedio de servicio por tipo de vehículo: CAMION: 10 minutos";
        assertEquals(expectedMessage, result);
    }

    @Test
    public void testMaxTimeService() {
        List<ParkingTransaction> timeServicesByTransactionStatus = new ArrayList<>();
        ParkingTransaction parkingTransaction = new ParkingTransaction();
        parkingTransaction.setVehicleType("CAMION");
        parkingTransaction.setTimeService(3);
        timeServicesByTransactionStatus.add(parkingTransaction);

        Mockito.when(parkingRepository.findByTransactionStatus(EnumTransactionStatus.TERMINATED.getId())).thenReturn(timeServicesByTransactionStatus);
        statisticsParking.maxTimeService();
    }

    @Test
    public void testAverageTimeService_NoData() {
        // Mock the findByVehicleType method to return an empty list
        Mockito.when(statisticsParking.findByVehicleType("CAMION", ParkingTransaction.class)).thenReturn(Collections.emptyList());

        // Call the `averageTimeService` method.
        RequestStatisticsParkingDTO requestStatisticsParkingDTO = new RequestStatisticsParkingDTO();
        requestStatisticsParkingDTO.setVehicleType("CAMION");
        String result = statisticsParking.averageTimeService(requestStatisticsParkingDTO);

        // Verify the result
        String expectedMessage = "No hay informacion disponible para la consulta de findByVehicleType";
        assertEquals(expectedMessage, result);
    }

    @Test
    public void testMaxTimeService_NoData() {
        // Mock the findByTransactionStatus method to return an empty list
        Mockito.when(statisticsParking.findByTransactionStatus(EnumTransactionStatus.TERMINATED.getId(), ParkingTransaction.class)).thenReturn(Collections.emptyList());

        // Call the `maxTimeService` method.
        String result = statisticsParking.maxTimeService();

        // Verify the result
        String expectedMessage = "No hay información disponible para la consulta del vehículo que ha permanecido más tiempo en el parqueadero";
        assertEquals(expectedMessage, result);
    }

}