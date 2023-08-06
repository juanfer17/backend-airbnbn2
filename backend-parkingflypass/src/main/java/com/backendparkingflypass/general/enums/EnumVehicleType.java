package com.backendparkingflypass.general.enums;

import com.backendparkingflypass.dto.EnumDTO;

public enum EnumVehicleType {
    AUTOMOBILE(0, "Automovil"),
    TRUCK(1, "Camión"),
    MOTORCYCLE (2, "Motocicleta");

    private final int id;
    private final String value;

    EnumVehicleType(int id, String value) {
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
