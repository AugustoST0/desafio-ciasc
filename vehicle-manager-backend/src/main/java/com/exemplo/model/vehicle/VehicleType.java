package com.exemplo.model.vehicle;

import lombok.Getter;

@Getter
public enum VehicleType {

    CARRO(0),
    MOTO(1),
    CAMINHAO(2),
    ONIBUS(3);

    private int code;

    private VehicleType(int code) {
        this.code = code;
    }
}
