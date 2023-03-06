package com.example.t04_advancedrobodriving.ev3SystemCommands;

public enum EV3Opcode {
    OUTPUT_STEP_SPEED((byte) 0xae);
    private final byte byteValue;

    EV3Opcode(byte byteValue) {
        this.byteValue = byteValue;
    }

    public byte getByteValue() {
        return byteValue;
    }
}
