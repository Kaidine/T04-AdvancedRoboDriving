package com.example.t04_advancedrobodriving.ev3SystemCommands;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EV3OpcodeTest {

    @Test
    void outputSpeedOpcodeHasCorrectByteValue() {
        assertEquals((byte) 0xa5, EV3Opcode.OUTPUT_SPEED.getByteValue());
    }

    @Test
    void outputStartOpcodeHasCorrectByteValue() {
        assertEquals((byte) 0xa6, EV3Opcode.OUTPUT_START.getByteValue());
    }

    @Test
    void outputStepSpeedOpcodeHasCorrectByteValue() {
        assertEquals((byte) 0xae, EV3Opcode.OUTPUT_STEP_SPEED.getByteValue());
    }

    @Test
    void outputStepSyncOpcodeHasCorrectByteValue() {
        assertEquals((byte) 0xb0, EV3Opcode.OUTPUT_STEP_SYNC.getByteValue());
    }

}