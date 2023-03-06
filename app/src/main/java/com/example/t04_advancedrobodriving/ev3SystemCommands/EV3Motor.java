package com.example.t04_advancedrobodriving.ev3SystemCommands;

public enum EV3Motor {
    A((byte) 1),
    B((byte) 2),
    C((byte) 4),
    D((byte) 8);

    private final byte byteValue;

    EV3Motor(byte byteValue) {
        this.byteValue = byteValue;
    }

    public byte getByteValue() {
        return byteValue;
    }
}
