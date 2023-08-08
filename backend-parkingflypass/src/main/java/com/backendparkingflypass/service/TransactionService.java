package com.backendparkingflypass.service;

import com.amazonaws.services.sqs.model.Message;
import com.backendparkingflypass.config.AwsProperties;
import com.backendparkingflypass.dto.RequestTransactionCreateDTO;
import com.backendparkingflypass.dto.RequestTransactionTerminatedDTO;
import com.backendparkingflypass.general.enums.EnumTransactionStatus;
import com.backendparkingflypass.general.exception.NoDataFoundException;
import com.backendparkingflypass.general.utils.DateTimeUtils;
import com.backendparkingflypass.general.utils.JSONUtils;
import com.backendparkingflypass.model.ParkingTransaction;
import com.backendparkingflypass.repository.ParkingTransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class TransactionService {

    private final SnsService snsService;
    private final AwsProperties awsProperties;
    private MessagesService messagesService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private final ParkingTransactionRepository parkingTransactionRepository;

    public TransactionService( SnsService snsService, AwsProperties awsProperties, MessagesService messagesService, ParkingTransactionRepository parkingTransactionRepository) {
        this.snsService = snsService;
        this.awsProperties = awsProperties;
        this.messagesService = messagesService;
        this.parkingTransactionRepository = parkingTransactionRepository;
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
                ParkingTransaction parkingTransactionValidation = parkingTransactionRepository.findByPlateAndStatus(requestTransactionCreate.getPlate(), EnumTransactionStatus.STARTED.getId());
                if(parkingTransactionValidation == null){
                    createAndSaveTransaction(requestTransactionCreate);
                    logger.info("Se crea el Transaccion de la placa : " + requestTransactionCreate.getPlate());
                } else {
                    logger.warn("No se ha guardado el registro debido a que ya existe una transacción para la placa : " + requestTransactionCreate.getPlate()  + " que se encuentra en estado : " + parkingTransactionValidation.getTransactionStatus() + "con el numero de transacción : " + parkingTransactionValidation.getTransactionId());
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
        parkingTransaction.setTransactionId(parkingTransactionRepository.getNextTransactionId());
        Date entryDate = DateTimeUtils.convertLocalToUTC(new Date());
        parkingTransaction.setEntryDate(entryDate);
        parkingTransaction.setPlate(requestTransactionCreate.getPlate());
        parkingTransaction.setTransactionStatus(EnumTransactionStatus.STARTED.getId());
        parkingTransaction.setVehicleType(requestTransactionCreate.getVehicleType());
        parkingTransaction.save();
    }

    public List<Message> terminateTransactionFromQeue(List<Message> messages) {
        List<Message> messagesToDelete = new ArrayList<>();
        for (Message message : messages) {
            try {
                String jsonMessage = message.getBody();

                JsonNode rootNode = objectMapper.readTree(jsonMessage);
                String filteredMessage = rootNode.get("Message").asText();

                logger.info("Se obtiene el cuerpo del mensaje " + filteredMessage);

                RequestTransactionTerminatedDTO requestTransactionTerminate = Optional.ofNullable(JSONUtils.jsonToObject(filteredMessage, RequestTransactionTerminatedDTO.class))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo procesar el mensaje de finalización de transacción."));

                ParkingTransaction parkingTransactionValidation = parkingTransactionRepository.findByPlateAndStatus(requestTransactionTerminate.getPlate(), EnumTransactionStatus.STARTED.getId());
                if (parkingTransactionValidation != null) {
                    terminateTransaction(parkingTransactionValidation);
                    logger.info("Se ha finalizado la transacción para la placa: " + parkingTransactionValidation.getPlate());
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una transacción con el ID de placa proporcionado.");
                }
                messagesToDelete.add(message);
            } catch (JsonProcessingException | NoSuchElementException | ResponseStatusException e) {
                logger.error(e.getMessage());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al procesar el mensaje de finalización de transacción.", e);
            }
        }
        return messagesToDelete;
    }

    public void terminateTransaction(ParkingTransaction parkingTransaction){
        Date exitDate = DateTimeUtils.convertLocalToUTC(new Date());
        parkingTransaction.setExitDate(exitDate);
        Date entryDate = parkingTransaction.getEntryDate();
        long millisecondsDifference = exitDate.getTime() - entryDate.getTime();
        long minutes = millisecondsDifference / (60 * 1000); // Convertir milisegundos a minutos
        int minutesAsInt = (int) minutes;
        parkingTransaction.setTimeService(minutesAsInt);
        parkingTransaction.setTransactionStatus(EnumTransactionStatus.TERMINATED.getId());
        parkingTransaction.save();
    }


    public void sendTransactionToSNS(List<RequestTransactionCreateDTO> transactions) {
        for(RequestTransactionCreateDTO requestTransactionCreate : transactions){
            logger.info("Se envia SNS de creacion de transaccion ");
            sendSNSTransaction(requestTransactionCreate, awsProperties.getArnEntryParkingSns());
        }
    }

    public void sendEndTransactionToSNS(List<RequestTransactionTerminatedDTO> transactions) {
        for(RequestTransactionTerminatedDTO requestTransactionTerminated : transactions){
            logger.info("Se envia SNS de creacion de transaccion ");
            sendSNSTransactionTerminated(requestTransactionTerminated, awsProperties.getArnExitParkingSns());
        }
    }

    public void sendSNSTransaction(RequestTransactionCreateDTO transaction, String arn){
        snsService.sendToSNS(JSONUtils.objectToJson(transaction), arn);
    }
    public void sendSNSTransactionTerminated(RequestTransactionTerminatedDTO transaction, String arn){
        snsService.sendToSNS(JSONUtils.objectToJson(transaction), arn);
    }


}
