package com.backendparkingflypass.rest;

import com.backendparkingflypass.dto.RequestStatisticsParkingDTO;
import com.backendparkingflypass.dto.RequestTransactionCreateDTO;
import com.backendparkingflypass.dto.RequestTransactionTerminatedDTO;
import com.backendparkingflypass.general.constansts.Constants;
import com.backendparkingflypass.service.StatisticsParking;
import com.backendparkingflypass.service.TransactionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping(Constants.ROOT_ENDPOINT + "/" )

public class ParkingTransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private StatisticsParking statisticsParking;

    @PostMapping(Constants.CREATE_TRANSACTIONS)
    public ResponseEntity<String> createTransaction(@RequestBody List<RequestTransactionCreateDTO> requestTransactionCreate) {
        try {
            transactionService.sendTransactionToSNS(requestTransactionCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body("Transacion creada exitosamente.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fallo al crear transaccion.", e);
        }
    }

    @PostMapping(Constants.TERMINATED_TRANSACTIONS)
    public ResponseEntity<String> endTransaction(@RequestBody List<RequestTransactionTerminatedDTO> requestTransactionTerminated) {
        try {
            transactionService.sendEndTransactionToSNS(requestTransactionTerminated);
            return ResponseEntity.status(HttpStatus.CREATED).body("Transacion finalizada exitosamente.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fallo al finalizar transaccion.", e);
        }
    }

    @PostMapping(Constants.AVERAGE_TRANSACTIONS)
    public ResponseEntity<String> averageTransactions(@RequestBody RequestStatisticsParkingDTO requestStatisticsParkingDTO) {
        try {
            String result = statisticsParking.averageTimeService(requestStatisticsParkingDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fallo al calcular el promedio.", e);
        }
    }

    @PostMapping(Constants.MAX_SERVICE_TIME)
    public ResponseEntity<String> maxTimeService() {
        try {
            String result = statisticsParking.maxTimeService();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fallo al consultar el tiempo máximo", e);
        }
    }

}
