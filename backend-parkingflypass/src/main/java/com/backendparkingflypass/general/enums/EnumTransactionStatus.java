package com.backendparkingflypass.general.enums;

import com.backendparkingflypass.dto.EnumDTO;

public enum EnumTransactionStatus {

    STARTED(0, "Iniciada"),
    TERMINATED(1, "Transaccion Finalizada");

    private final int id;
    private final String value;

    EnumTransactionStatus(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public EnumDTO getDto() {
        return new EnumDTO(this.ordinal(), this.getValue());
    }
}
