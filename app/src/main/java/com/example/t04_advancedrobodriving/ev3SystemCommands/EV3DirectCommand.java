package com.example.t04_advancedrobodriving.ev3SystemCommands;

public enum EV3DirectCommand {
    DIRECT_COMMAND_REPLY((byte) 0x00),
    DIRECT_COMMAND_NOREPLY((byte) 0x80);

    private final byte byteValue;

    EV3DirectCommand(byte byteValue) {

        this.byteValue = byteValue;
    }

    public byte getByteValue() {
        return byteValue;
    }
}
