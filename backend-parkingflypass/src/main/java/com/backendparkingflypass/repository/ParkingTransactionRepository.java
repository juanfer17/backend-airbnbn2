package com.backendparkingflypass.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.backendparkingflypass.config.DynamoClient;
import com.backendparkingflypass.model.ParkingTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ParkingTransactionRepository {
    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    public ParkingTransactionRepository() {
        this.dynamoDBMapper = DynamoClient.getInstance();;
    }

    public ParkingTransaction findByPlateAndStatus(String plate, Integer transactionStatus) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":plate", new AttributeValue().withS(plate));
        eav.put(":status", new AttributeValue().withN(transactionStatus.toString()));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("plate = :plate AND transactionStatus = :status")
                .withExpressionAttributeValues(eav);

        List<ParkingTransaction> transactionList = dynamoDBMapper.scan(ParkingTransaction.class, scanExpression);

        if (transactionList.isEmpty()) {
            return null;
        }
        return transactionList.get(0);
    }

    public ParkingTransaction findByTransaction(String transactionId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":transactionId", new AttributeValue().withS(transactionId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("transactionId = :transactionId")
                .withExpressionAttributeValues(eav);

        List<ParkingTransaction> transactionList = dynamoDBMapper.scan(ParkingTransaction.class, scanExpression);

        if (transactionList.isEmpty()) {
            return null;
        }
        return transactionList.get(0);
    }

    public List<ParkingTransaction> findByVehicleTypeAndTransactionStatus(String vehicleType, Integer transactionStatus) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":vehicleType", new AttributeValue().withS(vehicleType));
        eav.put(":transactionStatus", new AttributeValue().withN(String.valueOf(transactionStatus)));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("vehicleType = :vehicleType AND transactionStatus = :transactionStatus")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(ParkingTransaction.class, scanExpression);
    }

    public List<ParkingTransaction> findByTransactionStatus(Integer transactionStatus) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":transactionStatus", new AttributeValue().withN(String.valueOf(transactionStatus)));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("transactionStatus = :transactionStatus")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(ParkingTransaction.class, scanExpression);
    }
}
