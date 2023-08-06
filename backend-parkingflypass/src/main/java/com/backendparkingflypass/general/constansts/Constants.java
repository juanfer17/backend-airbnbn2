package com.backendparkingflypass.general.constansts;

public class Constants {
    public static final String ROOT_ENDPOINT = "transactions";
    public static final String CREATE_TRANSACTIONS = "create";
    public static final String TERMINATED_TRANSACTIONS = "terminate";
    public static final String AVERAGE_TRANSACTIONS = "average";
    public static final String MAX_SERVICE_TIME = "max";
    public static final String TRANSACTIONS_SNS = "dev_parking_flypass";
    public static final String TRANSACTIONS_SQS = "parking_flypass_entry";

    public static final String TRANSACTIONS_EXIT_SNS = "dev_parking_flypass_exit";
    public static final String TRANSACTIONS_EXIT_SQS = "parking_flypass_exit";

    public static final int MAX_NUMBER_OF_MESSAGES = 10;
    public static final String APROXIMATE_NUMBER_OF_MESSAGES = "ApproximateNumberOfMessages";
    public static final String ALL = "All";
}
