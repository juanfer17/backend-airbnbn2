package com.backendparkingflypass.test;

import com.backendparkingflypass.service.MessagesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MessagesServiceTest {
    @Mock
    private MessageSource messageSource;

    private MessagesService messagesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        messagesService = new MessagesService(messageSource);
    }

    @Test
    public void testGetCannotCastMessage() {
        String expectedMessage = "Cannot cast message";
        when(messageSource.getMessage("cannot_cast_message", null, LocaleContextHolder.getLocale())).thenReturn(expectedMessage);

        String actualMessage = messagesService.getCannotCastMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
