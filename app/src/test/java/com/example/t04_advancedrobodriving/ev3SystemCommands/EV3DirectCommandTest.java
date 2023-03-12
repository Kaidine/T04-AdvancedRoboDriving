package com.example.t04_advancedrobodriving.ev3SystemCommands;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EV3DirectCommandTest {

    @Test
    void commandReplyHasCorrectByteValue() {
        assertEquals((byte) 0x00,EV3DirectCommand.DIRECT_COMMAND_REPLY.getByteValue());
    }

    @Test
    void commandNoReplyHasCorrectByteValue() {
        assertEquals((byte) 0x80,EV3DirectCommand.DIRECT_COMMAND_NOREPLY.getByteValue());
    }



}