package com.backendparkingflypass.scheduled;

import com.amazonaws.services.datasync.model.TaskSchedule;
import com.amazonaws.services.sqs.model.Message;
import com.backendparkingflypass.config.AWSClient;
import com.backendparkingflypass.service.SqsService;
import com.backendparkingflypass.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SQSExitScheduled extends TaskSchedule {
    @Autowired
    private final TransactionService transactionService;
    @Autowired
    private final SqsService sqsService;

    protected SQSExitScheduled(TransactionService transactionService, SqsService sqsService) {
        super();
        this.transactionService = transactionService;
        this.sqsService = sqsService;
    }

    @Scheduled(fixedDelay = 500)
    public void executeTaskGetTransactionsFromSqs() {
        processTask();
    }

    protected void processTask() {
        List<Message> messages = sqsService.getMessages(AWSClient.TRANSACTION_EXIT_SQS_URL);
        List<Message> messagesToDelete = transactionService.terminateTransactionFromQeue(messages);
        sqsService.deleteMessages(messagesToDelete, AWSClient.TRANSACTION_EXIT_SQS_URL);
    }
}
