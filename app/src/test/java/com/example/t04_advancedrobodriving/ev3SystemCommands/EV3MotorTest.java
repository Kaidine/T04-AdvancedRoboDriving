package com.example.t04_advancedrobodriving.ev3SystemCommands;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EV3MotorTest {

    @Test
    void motorAHasCorrectValue() {
        assertEquals((byte) 0b0001, EV3Motor.A.getByteValue());
    }

    @Test
    void motorBHasCorrectValue() {
        assertEquals((byte) 0b0010, EV3Motor.B.getByteValue());
    }

    @Test
    void motorCHasCorrectValue() {
        assertEquals((byte) 0b0100, EV3Motor.C.getByteValue());
    }

    @Test
    void motorDHasCorrectValue() {
        assertEquals((byte) 0b1000, EV3Motor.D.getByteValue());
    }


}