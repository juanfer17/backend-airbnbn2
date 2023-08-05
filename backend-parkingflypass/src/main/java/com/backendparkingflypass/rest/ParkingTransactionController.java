package com.backendparkingflypass.rest;

import com.backendparkingflypass.general.constansts.Constants;
import com.backendparkingflypass.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.ROOT_ENDPOINT + "/" )

public class ParkingTransactionController {

    @Autowired
    private TransactionService transactionService;

}
