package com.backendparkingflypass.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;
import io.swagger.annotations.ApiModelProperty;

@DynamoDBTable(tableName = "ParkingTransaction")
public class ParkingTransaction extends Model{
    private String transactionId;
    private Date entryDate;
    private String plate;
    private String transactionStatus;
    private Date exitDate;
    private String vehicleType;
    private String timeService;

    @DynamoDBHashKey(attributeName = "transactionId")
    @ApiModelProperty(required = true)
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @DynamoDBHashKey(attributeName = "entryDate")
    public Date getEntryDate() {
        return entryDate;
    }
    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }
    @DynamoDBHashKey(attributeName = "plate")
    public String getPlate() {
        return plate;
    }
    public void setPlate(String plate) {
        this.plate = plate;
    }
    @DynamoDBHashKey(attributeName = "transactionStatus")
    @ApiModelProperty(allowableValues = "0, 1")
    public String getTransactionStatus() {
        return transactionStatus;
    }
    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
    @DynamoDBHashKey(attributeName = "entryDate")
    public Date getExitDate() {
        return exitDate;
    }
    public void setExitDate(Date exitDate) {
        this.exitDate = exitDate;
    }
    @DynamoDBHashKey(attributeName = "vehicleType")
    public String getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    @DynamoDBHashKey(attributeName = "timeService")
    public String getTimeService() {
        return timeService;
    }
    public void setTimeService(String timeService) {
        this.timeService = timeService;
    }

    /*public static Vehicle findByTid(String tid) {
        Map<String, AttributeValue> eav = Collections.singletonMap(":tid", new AttributeValue().withS(tid));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("dsnro_tag = :tid").withExpressionAttributeValues(eav);
        List<Vehicle> vehicleList = scan(Vehicle.class, scanExpression);
        if(vehicleList.isEmpty()) {
            return null;
        }
        return vehicleList.get(0);
    }*/

}
