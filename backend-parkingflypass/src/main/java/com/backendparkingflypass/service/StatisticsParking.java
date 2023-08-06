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

}
