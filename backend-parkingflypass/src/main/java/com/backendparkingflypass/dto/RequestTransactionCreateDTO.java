package com.backendparkingflypass.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RequestTransactionCreateDTO {
    private String transactionId;
    private String plate;
    private Integer transactionStatus;
    private String vehicleType;
}
