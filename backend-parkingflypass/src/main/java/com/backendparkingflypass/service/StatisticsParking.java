package com.backendparkingflypass.service;


import com.backendparkingflypass.dto.RequestStatisticsParkingDTO;
import com.backendparkingflypass.dto.RequestTransactionTerminatedDTO;
import com.backendparkingflypass.general.enums.EnumTransactionStatus;
import com.backendparkingflypass.general.exception.NoDataFoundException;
import com.backendparkingflypass.model.ParkingTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class StatisticsParking {

    private static final Logger logger = LogManager.getLogger(TransactionService.class);

    public String averageTimeService(RequestStatisticsParkingDTO requestStatisticsParkingDTO) {
        List<ParkingTransaction> timeServicesByVehicleType = findByVehicleType(requestStatisticsParkingDTO.getVehicleType(), ParkingTransaction.class);

        if (timeServicesByVehicleType.isEmpty()) {
            return "No hay informacion disponible para la consulta de findByVehicleType";
        }

        double sumTime = 0.0;
        for (ParkingTransaction transaction : timeServicesByVehicleType) {
            sumTime += transaction.getTimeService(); // Ajusta el nombre del método según tu clase ParkingTransaction
        }

        double averageTime = sumTime / timeServicesByVehicleType.size();

        return "Tiempo promedio de servicio por tipo de vehículo: " + requestStatisticsParkingDTO.getVehicleType() + ": " + averageTime + " minutos";
    }

    public <T> List<T> findByVehicleType(String vehicleType, Class<T> parkingTransactionDTOClass) {
        logger.info("Consulta de transacciones por vehicleType: {}", vehicleType);
        List<ParkingTransaction> transactions = ParkingTransaction.findByVehicleTypeAndTransactionStatus(vehicleType, EnumTransactionStatus.TERMINATED.getId());
        return transactions.stream()
                .map(v -> v.getDTO(parkingTransactionDTOClass))
                .collect(Collectors.toList());
    }

    // Se busca el vehículo que ha permanecido más tiempo en el parqueadero.
    public String maxTimeService() {
        List<ParkingTransaction> timeServicesByTransactionStatus = findByTransactionStatus(EnumTransactionStatus.TERMINATED.getId(), ParkingTransaction.class);

        if (timeServicesByTransactionStatus.isEmpty()) {
            return "No hay información disponible para la consulta del vehículo que ha permanecido más tiempo en el parqueadero";
        }

        int maxTimeService = Integer.MIN_VALUE; // Inicializar con un valor muy pequeño
        ParkingTransaction vehicleWithMaxTimeService = null;

        for (ParkingTransaction transaction : timeServicesByTransactionStatus) {
            int currentTimeService = transaction.getTimeService(); // Ajusta el nombre del método según tu clase ParkingTransaction

            if (currentTimeService > maxTimeService) {
                maxTimeService = currentTimeService;
                vehicleWithMaxTimeService = transaction;
            }
        }

        if (vehicleWithMaxTimeService != null) {
            return "El vehículo con el tiempo de servicio máximo es " + vehicleWithMaxTimeService.getPlate() + " con un tiempo de servicio de " + maxTimeService + " minutos.";
        } else {
            return "No se pudo determinar el vehículo con el tiempo de servicio máximo.";
        }
    }


    public <T> List<T> findByTransactionStatus(Integer transactionStatus, Class<T> parkingTransactionDTOClass) {
        logger.info("Consulta de transacciones por transactionStatus: {}", transactionStatus);
        List<ParkingTransaction> transactions = ParkingTransaction.findByTransactionStatus(transactionStatus);
        return transactions.stream()
                .map(v -> v.getDTO(parkingTransactionDTOClass))
                .collect(Collectors.toList());
    }


}
