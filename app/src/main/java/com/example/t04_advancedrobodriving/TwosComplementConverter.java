package com.example.t04_advancedrobodriving;

public class TwosComplementConverter {

    public static byte convertIntToTwosComplement(int intToConvert){
        return (byte) (intToConvert & 0xFF);
    }
}
