package com.example.t04_advancedrobodriving.ev3SystemCommands;

public enum EV3Motor {
    A((byte) 0b0001),
    B((byte) 0b0010),
    C((byte) 0b0100),
    D((byte) 0b1000);

    private final byte byteValue;

    EV3Motor(byte byteValue) {
        this.byteValue = byteValue;
    }

    public byte getByteValue() {
        return byteValue;
    }
}
