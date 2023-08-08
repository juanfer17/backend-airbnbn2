package com.backendparkingflypass.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RequestTransactionCreateDTO {
    private String plate;
    private String vehicleType;
}
