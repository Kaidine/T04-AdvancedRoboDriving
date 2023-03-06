package com.example.t04_advancedrobodriving;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TwosComplementConverterTest {


    @Test
    void convertsPositiveIntegerCorrectly() {
        assertEquals((byte) 0x32, TwosComplementConverter.convertIntToTwosComplement( 50));
    }

    @Test
    void convertsNegativeIntegerCorrectly() {
        assertEquals((byte) 0xCE, TwosComplementConverter.convertIntToTwosComplement( -50));
    }

}