package com.backendparkingflypass.general.utils;

import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {

    // Convierte una fecha local a UTC
    public static Date convertLocalToUTC(Date localDate) {
        long localToUtcDifference = TimeZone.getDefault().getRawOffset();
        return new Date(localDate.getTime() - localToUtcDifference);
    }

    // Convierte una fecha UTC a local
    public static Date convertUTCToLocal(Date utcDate) {
        long localToUtcDifference = TimeZone.getDefault().getRawOffset();
        return new Date(utcDate.getTime() + localToUtcDifference);
    }
}
