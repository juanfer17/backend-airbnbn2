package com.backendparkingflypass.general.utils;

import java.text.NumberFormat;

public class FormatUtils {
    public static String formatCurrency(Object amount) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format(amount);
    }
}
