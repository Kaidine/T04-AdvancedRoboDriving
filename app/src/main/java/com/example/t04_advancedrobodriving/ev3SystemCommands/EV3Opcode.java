package com.example.t04_advancedrobodriving.ev3SystemCommands;

public enum EV3Opcode {
    UI_READ((byte) 0x81),
    OUTPUT_SPEED((byte) 0xa5),
    OUTPUT_START((byte) 0xa6),
    OUTPUT_STEP_SPEED((byte) 0xae),
    OUTPUT_STEP_SYNC((byte) 0xb0),
    PLAY_SOUND((byte) 0x94),
    READ_SENSOR((byte) 0x99),
    RESET_MOTOR_ROTATION_COUNT((byte) 0xA2);
    private final byte byteValue;

    EV3Opcode(byte byteValue) {
        this.byteValue = byteValue;
    }

    public byte getByteValue() {
        return byteValue;
    }
}
