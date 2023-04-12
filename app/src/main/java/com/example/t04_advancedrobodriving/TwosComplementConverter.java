package com.example.t04_advancedrobodriving;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TwosComplementConverter {

    public static byte[] convertIntToTwosComplement(int intToConvert) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(intToConvert);
        return byteBuffer.array();
    }

    public static float convertByteArrayToFloat(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }
}
