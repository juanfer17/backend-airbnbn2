package com.backendparkingflypass.model;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.backendparkingflypass.config.DynamoClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class Model {
    private static final Logger logger = LogManager.getLogger(Model.class);

    public void delete() {
        DynamoClient.getInstance().delete(this);
    }

    protected static <T> T findById(Class<T> clazz, String id) {
        return DynamoClient.getInstance().load(clazz, id);
    }

    protected static <T> T findByIdAndRangeKey(Class<T> clazz, String id, String rangeKey) {
        return DynamoClient.getInstance().load(clazz, id, rangeKey);
    }

    public <T> T getDTO(Class<T> clazz) {
        try {
            T dto = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(this, dto);
            return dto;
        } catch (InstantiationException | IllegalAccessException | NullPointerException | NoSuchMethodException |
                 InvocationTargetException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    protected static <T> List<T> scan(Class<T> clazz, DynamoDBScanExpression scanExpression) {
        return DynamoClient.getInstance().scan(clazz, scanExpression);
    }

    public void save() {
        try {
            DynamoClient.getInstance().save(this);
            logger.info("Object was saved");
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    protected static <T> List<T> scan(Class<T> clazz) {
        return DynamoClient.getInstance().scan(clazz, new DynamoDBScanExpression());
    }
}
