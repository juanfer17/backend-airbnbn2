package com.backendparkingflypass.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessagesService {
    private final MessageSource messageSource;

    public MessagesService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getAttentionPointServiceNameNotFoundMessage(String stationId) {
        return messageSource.getMessage("attention_point_service_name_not_found", new Object[]{stationId}, LocaleContextHolder.getLocale());
    }

    public String getCannotCastMessage() {
        return messageSource.getMessage("cannot_cast_message", null, LocaleContextHolder.getLocale());
    }

    public String getVehicleNotFoundMessage(Long id) {
        return messageSource.getMessage("vehicle_not_found", new Object[]{id}, LocaleContextHolder.getLocale());
    }

    public String getPlateNotFoundMessage(String plate) {
        return messageSource.getMessage("plate_not_found", new Object[]{plate}, LocaleContextHolder.getLocale());
    }
}
