package com.backendparkingflypass.rest;

import com.backendparkingflypass.dto.RequestStatisticsParkingDTO;
import com.backendparkingflypass.dto.RequestTransactionCreateDTO;
import com.backendparkingflypass.dto.RequestTransactionTerminatedDTO;
import com.backendparkingflypass.general.constansts.Constants;
import com.backendparkingflypass.service.StatisticsParking;
import com.backendparkingflypass.service.TransactionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping(Constants.ROOT_ENDPOINT + "/" )

public class ParkingTransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private StatisticsParking statisticsParking;

    @PostMapping( value = Constants.CREATE_TRANSACTIONS)
    public void createTransaction(@RequestBody List<RequestTransactionCreateDTO> requestTransactionCreate, @ApiIgnore HttpServletResponse response){
        transactionService.sendTransactionToSNS(requestTransactionCreate);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
    @PostMapping( value = Constants.TERMINATED_TRANSACTIONS)
    public void endTransaction(@RequestBody List<RequestTransactionTerminatedDTO> requestTransactionTerminated, @ApiIgnore HttpServletResponse response){
        transactionService.sendEndTransactionToSNS(requestTransactionTerminated);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
    @PostMapping( value = Constants.AVERAGE_TRANSACTIONS)
    public String averageTransactions(@RequestBody RequestStatisticsParkingDTO requestStatisticsParkingDTO, @ApiIgnore HttpServletResponse response){
        return statisticsParking.averageTimeService(requestStatisticsParkingDTO);
    }

    @PostMapping( value = Constants.MAX_SERVICE_TIME)
    public String maxTimeService(@ApiIgnore HttpServletResponse response){
        return statisticsParking.maxTimeService();
    }

}
