package com.backendparkingflypass.model;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.*;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import io.swagger.annotations.ApiModelProperty;

@DynamoDBTable(tableName = "ParkingTransaction")
public class ParkingTransaction extends Model{
    private String transactionId;
    private Date entryDate;
    private String plate;
    private Integer transactionStatus;
    private Date exitDate;
    private String vehicleType;
    private Integer timeService;

    @DynamoDBHashKey(attributeName = "transactionId")
    @ApiModelProperty(required = true)
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @DynamoDBAttribute(attributeName = "entryDate")
    public Date getEntryDate() {
        return entryDate;
    }
    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    @DynamoDBAttribute(attributeName = "plate")
    public String getPlate() {
        return plate;
    }
    public void setPlate(String plate) {
        this.plate = plate;
    }

    @DynamoDBAttribute(attributeName = "transactionStatus")
    @ApiModelProperty(allowableValues = "0, 1")
    public Integer getTransactionStatus() {
        return transactionStatus;
    }
    public void setTransactionStatus(Integer transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @DynamoDBAttribute(attributeName = "exitDate")
    public Date getExitDate() {
        return exitDate;
    }
    public void setExitDate(Date exitDate) {
        this.exitDate = exitDate;
    }

    @DynamoDBAttribute(attributeName = "vehicleType")
    public String getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @DynamoDBAttribute(attributeName = "timeService")
    public Integer getTimeService() {
        return timeService;
    }
    public void setTimeService(Integer timeService) {
        this.timeService = timeService;
    }


    public static ParkingTransaction findByPlateAndStatus(String plate, Integer transactionStatus) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":plate", new AttributeValue().withS(plate));
        eav.put(":status", new AttributeValue().withN(transactionStatus.toString()));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("plate = :plate AND transactionStatus = :status").withExpressionAttributeValues(eav);
        List<ParkingTransaction> transactionList = scan(ParkingTransaction.class, scanExpression);
        if(transactionList.isEmpty()) {
            return null;
        }
        return transactionList.get(0);
    }
    public static ParkingTransaction findByTransaction(String transactionId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":transactionId", new AttributeValue().withS(transactionId));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("transactionId = :transactionId").withExpressionAttributeValues(eav);
        List<ParkingTransaction> transactionList = scan(ParkingTransaction.class, scanExpression);
        if(transactionList.isEmpty()) {
            return null;
        }
        return transactionList.get(0);
    }
}
