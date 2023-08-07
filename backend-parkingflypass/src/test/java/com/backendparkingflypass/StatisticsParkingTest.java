package com.backendparkingflypass;

import com.backendparkingflypass.dto.RequestStatisticsParkingDTO;
import com.backendparkingflypass.general.enums.EnumTransactionStatus;
import com.backendparkingflypass.model.ParkingTransaction;
import com.backendparkingflypass.service.StatisticsParking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class StatisticsParkingTest {

    @InjectMocks
    private StatisticsParking statisticsParking;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByVehicleType() {
        // Configuración de prueba
        String vehicleType = "AUTOMOVIL";
        Class<ParkingTransaction> dtoClass = ParkingTransaction.class;

        List<ParkingTransaction> mockTransactions = new ArrayList<>(); // Mock de las transacciones
        ParkingTransaction transaction1 = new ParkingTransaction();
        transaction1.setTransactionStatus(EnumTransactionStatus.TERMINATED.getId());
        mockTransactions.add(transaction1);

        ParkingTransaction transaction2 = new ParkingTransaction();
        transaction2.setTransactionStatus(EnumTransactionStatus.TERMINATED.getId());
        mockTransactions.add(transaction2);

        when(ParkingTransaction.findByVehicleTypeAndTransactionStatus(eq(vehicleType), anyInt())).thenReturn(mockTransactions);

        // Ejecutar el método bajo prueba
        List<ParkingTransaction> result = statisticsParking.findByVehicleType(vehicleType, dtoClass);

        // Verificar el resultado
        assertEquals(2, result.size());
        // Puedes agregar más verificaciones aquí según tus necesidades
    }



}