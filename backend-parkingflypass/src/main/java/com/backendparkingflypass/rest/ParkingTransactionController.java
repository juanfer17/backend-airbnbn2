package com.backendparkingflypass.rest;

import com.backendparkingflypass.dto.RequestTransactionCreateDTO;
import com.backendparkingflypass.general.constansts.Constants;
import com.backendparkingflypass.general.exception.NoDataFoundException;
import com.backendparkingflypass.general.exception.ValidationException;
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

    @PostMapping( value = Constants.CREATE_TRANSACTIONS)
    public void createTransaction(@RequestBody List<RequestTransactionCreateDTO> requestTransactionCreate, @ApiIgnore HttpServletResponse response){
        transactionService.sendTransactionToSNS(requestTransactionCreate);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

}
