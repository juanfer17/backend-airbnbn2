package com.backendparkingflypass.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class ParkingTransactionDTO {
    private String transactionId;
    private Date entryDate;
    private String plate;
    private Integer transactionStatus;
    private Date exitDate;
    private String vehicleType;
    private String timeService;

    @ApiModelProperty(example = "123122331")
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @ApiModelProperty(example = "2023-08-05T15:30:00Z")
    public Date getEntryDate() {
        return entryDate;
    }
    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    @ApiModelProperty(example = "1")
    public Integer getTransactionStatus() {
        return transactionStatus;
    }
    public void setTransactionStatus(Integer transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @ApiModelProperty(example = "2023-08-05T18:45:00Z")
    public Date getExitDate() {
        return exitDate;
    }
    public void setExitDate(Date exitDate) {
        this.exitDate = exitDate;
    }

    @ApiModelProperty(example = "car")
    public String getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @ApiModelProperty(example = "2 hours")
    public String getTimeService() {
        return timeService;
    }
    public void setTimeService(String timeService) {
        this.timeService = timeService;
    }
}
