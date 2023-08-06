package com.backendparkingflypass.service;

import com.amazonaws.services.sqs.model.Message;
import com.backendparkingflypass.config.AwsProperties;
import com.backendparkingflypass.dto.RequestTransactionCreateDTO;
import com.backendparkingflypass.general.enums.EnumTransactionStatus;
import com.backendparkingflypass.general.exception.NoDataFoundException;
import com.backendparkingflypass.general.utils.DateTimeUtils;
import com.backendparkingflypass.general.utils.JSONUtils;
import com.backendparkingflypass.model.ParkingTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class TransactionService {

    private final SnsService snsService;
    private final AwsProperties awsProperties;
    private MessagesService messagesService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransactionService( SnsService snsService, AwsProperties awsProperties, MessagesService messagesService) {
        this.snsService = snsService;
        this.awsProperties = awsProperties;
        this.messagesService = messagesService;
    }

    private static final Logger logger = LogManager.getLogger(TransactionService.class);

    public List<Message> createTransaction(List<Message> messages) {
        List<Message> messagesToDelete = new ArrayList<>();
        for (Message message : messages) {
            try {
                String jsonMessage = message.getBody();

                JsonNode rootNode = objectMapper.readTree(jsonMessage);
                String filteredMessage = rootNode.get("Message").asText();

                logger.info("Se obtiene el body del mensaje " + filteredMessage);

                RequestTransactionCreateDTO requestTransactionCreate = Optional.ofNullable(JSONUtils.jsonToObject(filteredMessage, RequestTransactionCreateDTO.class))
                        .orElseThrow(() -> new ClassCastException(messagesService.getCannotCastMessage()));

                ParkingTransaction parkingTransactionValidation = transactionValidation(requestTransactionCreate.getPlate(), ParkingTransaction.class);
                if(parkingTransactionValidation == null){
                    createAndSaveTransaction(requestTransactionCreate);
                    logger.info("Se crea el Transaccion de la placa : " + requestTransactionCreate.getPlate() + "con el numero de transacción : " + requestTransactionCreate.getTransactionId());
                } else {
                    logger.warn("No se ha guardado el registro debido a que ya existe una transacción para la placa : " + requestTransactionCreate.getPlate()  + " que se encuentra en estado : " + requestTransactionCreate.getTransactionStatus() + "con el numero de transacción : " + requestTransactionCreate.getTransactionId());
                }
                messagesToDelete.add(message);
            } catch (JsonProcessingException | NoSuchElementException | ClassCastException e) {
                logger.error(e.getMessage());
            }
        }
        return messagesToDelete;
    }
    public void createAndSaveTransaction(RequestTransactionCreateDTO requestTransactionCreate){
        ParkingTransaction parkingTransaction = new ParkingTransaction();
        parkingTransaction.setTransactionId(requestTransactionCreate.getTransactionId());
        Date entryDate = DateTimeUtils.convertLocalToUTC(new Date());
        parkingTransaction.setEntryDate(entryDate);
        parkingTransaction.setPlate(requestTransactionCreate.getPlate());
        parkingTransaction.setTransactionStatus(EnumTransactionStatus.STARTED.getId());
        parkingTransaction.setVehicleType(requestTransactionCreate.getVehicleType());
        parkingTransaction.save();
    }

    public void sendTransactionToSNS(List<RequestTransactionCreateDTO> transactions) {
        for(RequestTransactionCreateDTO requestTransactionCreate : transactions){
            logger.info("Se envia SNS de creacion de transaccion ");
            sendSNSTransaction(requestTransactionCreate, awsProperties.getArnEntryParkingSns());
        }
    }

    public <T> T transactionValidation(String plate,  Class<T> parkingTransactionDTOClass) {
        logger.info("Consulta de vehiculo por placa: {}" , plate);
        try{
            return Optional.ofNullable(ParkingTransaction.findByPlateAndStatus(plate, EnumTransactionStatus.STARTED.getId())).map(v -> v.getDTO(parkingTransactionDTOClass)).orElseThrow(NoDataFoundException::new);
        }catch(NoDataFoundException noDataFoundException){
            return null;
        }
    }

    public void sendSNSTransaction(RequestTransactionCreateDTO transaction, String arn){
        snsService.sendToSNS(JSONUtils.objectToJson(transaction), arn);
    }


}
