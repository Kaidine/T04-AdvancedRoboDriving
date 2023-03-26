package com.example.t04_advancedrobodriving;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TwosComplementConverter {

    public static byte[] convertIntToTwosComplement(int intToConvert){
        ByteBuffer byteBuffer = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(intToConvert);
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (intToConvert & 0x000000FF);
        bytes[1] = (byte) (intToConvert & 0x0000FF00);
        bytes[2] = (byte) (intToConvert & 0x00FF0000);
        bytes[3] = (byte) (intToConvert & 0xFF000000);
        return byteBuffer.array();
    }
}
