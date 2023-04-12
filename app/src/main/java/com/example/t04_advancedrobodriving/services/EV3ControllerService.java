package com.example.t04_advancedrobodriving.services;

import androidx.annotation.NonNull;

import com.example.t04_advancedrobodriving.TwosComplementConverter;
import com.example.t04_advancedrobodriving.ev3SystemCommands.EV3DirectCommand;
import com.example.t04_advancedrobodriving.ev3SystemCommands.EV3Motor;
import com.example.t04_advancedrobodriving.ev3SystemCommands.EV3Opcode;

import java.io.IOException;

public class EV3ControllerService {

    private static EV3ControllerService INSTANCE;

    private EV3ControllerService() {
    }

    public static EV3ControllerService instance() {
        if (INSTANCE == null) {
            INSTANCE = new EV3ControllerService();
        }
        return INSTANCE;
    }

    public void playTone() {
        byte[] buffer = new byte[17];       // 0x0f command length

        buffer[0] = (byte) (17 - 2);
        buffer[1] = 0;

        buffer[2] = 34;
        buffer[3] = 12;

        buffer[4] = (byte) 0x80;

        buffer[5] = 0;
        buffer[6] = 0;

        buffer[7] = (byte) 0x94;
        buffer[8] = 1;

        buffer[9] = (byte) 0x81;
        buffer[10] = (byte) 0x02;

        buffer[11] = (byte) 0x82;
        buffer[12] = (byte) 0xe8;
        buffer[13] = (byte) 0x03;

        buffer[14] = (byte) 0x82;
        buffer[15] = (byte) 0xe8;
        buffer[16] = (byte) 0x03;

        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(buffer);
    }

    public void startRobotMoving(int speed) {
        byte motorsAsSingleByte = (byte) (EV3Motor.B.getByteValue() ^ EV3Motor.C.getByteValue());

        int syncMovementCommandBufferLength = 15;
        byte[] syncMovementCommandBuffer = new byte[syncMovementCommandBufferLength];

        syncMovementCommandBuffer[0] = (byte) (syncMovementCommandBufferLength - 2);    // command length (2 bytes)
        syncMovementCommandBuffer[1] = 0x0;                                         // command length *not* including these two bytes

        syncMovementCommandBuffer[2] = 0x0;                                         // message counter. unused.
        syncMovementCommandBuffer[3] = 0x0;                                          // message counter. unused.

        syncMovementCommandBuffer[4] = EV3DirectCommand.DIRECT_COMMAND_NOREPLY.getByteValue();

        syncMovementCommandBuffer[5] = 0;
        syncMovementCommandBuffer[6] = 0;

        syncMovementCommandBuffer[7] = EV3Opcode.OUTPUT_STEP_SYNC.getByteValue();  // opcode

        syncMovementCommandBuffer[8] = 0;                                          // run on layer 0 (LAYER_0)
        syncMovementCommandBuffer[9] = (byte) motorsAsSingleByte;                  // motors   |D|C|B|A|
        //                      |-|-|-|-|
        //                      |0|1|1|0| = 0x06

        //set speed
        syncMovementCommandBuffer[10] = (byte) 0x81;                               // LC1 - one byte to follow
        syncMovementCommandBuffer[11] = TwosComplementConverter.convertIntToTwosComplement(speed)[0];

        // set turning ratio. 0 = matching.
        syncMovementCommandBuffer[12] = (byte) 0x0;                               // LC0 - constant (ratio = 0)

        // tach pulses (rotations), 0=infinite
        syncMovementCommandBuffer[13] = (byte) 0x0;                               // LC0 - constant (pulses = 0)

        //braking level, 0= float, 1 =brake
        syncMovementCommandBuffer[14] = (byte) 0x01;                               // LC0 - constant (brake)

        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(syncMovementCommandBuffer);
    }

    public void stopRobotMoving() {
        startRobotMoving(0);
    }

    public void startRobotTurningLeft(int speed) {
        byte motorsAsSingleByte = (byte) (EV3Motor.B.getByteValue() ^ EV3Motor.C.getByteValue());

        int syncMovementCommandBufferLength = 17;
        byte[] syncMovementCommandBuffer = new byte[syncMovementCommandBufferLength];

        syncMovementCommandBuffer[0] = (byte) (syncMovementCommandBufferLength - 2);    // command length (2 bytes)
        syncMovementCommandBuffer[1] = 0x0;                                         // command length *not* including these two bytes

        syncMovementCommandBuffer[2] = 0x0;                                         // message counter. unused.
        syncMovementCommandBuffer[3] = 0x0;                                          // message counter. unused.

        syncMovementCommandBuffer[4] = EV3DirectCommand.DIRECT_COMMAND_NOREPLY.getByteValue();

        syncMovementCommandBuffer[5] = 0;
        syncMovementCommandBuffer[6] = 0;

        syncMovementCommandBuffer[7] = EV3Opcode.OUTPUT_STEP_SYNC.getByteValue();  // opcode

        syncMovementCommandBuffer[8] = 0;                                          // run on layer 0 (LAYER_0)
        syncMovementCommandBuffer[9] = (byte) motorsAsSingleByte;                  // motors   |D|C|B|A|
        //                      |-|-|-|-|
        //                      |0|1|1|0| = 0x06

        //set speed
        syncMovementCommandBuffer[10] = (byte) 0x81;                               // LC1 - one byte to follow
        syncMovementCommandBuffer[11] = TwosComplementConverter.convertIntToTwosComplement(speed)[0];

        // set turning ratio. 0 = matching.
        syncMovementCommandBuffer[12] = (byte) 0x82;                               // LC0 - constant (ratio = -200)
        syncMovementCommandBuffer[13] = (byte) 0x38;
        syncMovementCommandBuffer[14] = (byte) 0xFF;

        // tach pulses (rotations), 0=infinite
        syncMovementCommandBuffer[15] = (byte) 0x0;                               // LC0 - constant (pulses = 0)

        //braking level, 0= float, 1 =brake
        syncMovementCommandBuffer[16] = (byte) 0x01;                               // LC0 - constant (brake)

        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(syncMovementCommandBuffer);
    }

    public void startRobotTurningRight(int speed) {
        byte motorsAsSingleByte = (byte) (EV3Motor.B.getByteValue() ^ EV3Motor.C.getByteValue());

        int syncMovementCommandBufferLength = 17;
        byte[] syncMovementCommandBuffer = new byte[syncMovementCommandBufferLength];

        syncMovementCommandBuffer[0] = (byte) (syncMovementCommandBufferLength - 2);    // command length (2 bytes)
        syncMovementCommandBuffer[1] = 0x0;                                         // command length *not* including these two bytes

        syncMovementCommandBuffer[2] = 0x0;                                         // message counter. unused.
        syncMovementCommandBuffer[3] = 0x0;                                          // message counter. unused.

        syncMovementCommandBuffer[4] = EV3DirectCommand.DIRECT_COMMAND_NOREPLY.getByteValue();

        syncMovementCommandBuffer[5] = 0;
        syncMovementCommandBuffer[6] = 0;

        syncMovementCommandBuffer[7] = EV3Opcode.OUTPUT_STEP_SYNC.getByteValue();  // opcode

        syncMovementCommandBuffer[8] = 0;                                          // run on layer 0 (LAYER_0)
        syncMovementCommandBuffer[9] = (byte) motorsAsSingleByte;                  // motors   |D|C|B|A|
        //                      |-|-|-|-|
        //                      |0|1|1|0| = 0x06

        //set speed
        syncMovementCommandBuffer[10] = (byte) 0x81;                               // LC1 - one byte to follow
        syncMovementCommandBuffer[11] = TwosComplementConverter.convertIntToTwosComplement(speed)[0];

        // set turning ratio. 0 = matching.
        syncMovementCommandBuffer[12] = (byte) 0x82;                               // LC0 - constant (ratio = -200)
        syncMovementCommandBuffer[13] = (byte) 0xc8;
        syncMovementCommandBuffer[14] = (byte) 0x00;

        // tach pulses (rotations), 0=infinite
        syncMovementCommandBuffer[15] = (byte) 0x0;                               // LC0 - constant (pulses = 0)

        //braking level, 0= float, 1 =brake
        syncMovementCommandBuffer[16] = (byte) 0x01;                               // LC0 - constant (brake)

        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(syncMovementCommandBuffer);
    }

    public void startRobotClawMoving(int speed) {
        byte motorsAsSingleByte = EV3Motor.A.getByteValue();

        int syncMovementCommandBufferLength = 12;
        byte[] syncMovementCommandBuffer = new byte[syncMovementCommandBufferLength];

        syncMovementCommandBuffer[0] = (byte) (syncMovementCommandBufferLength - 2);    // command length (2 bytes)
        syncMovementCommandBuffer[1] = 0x0;                                         // command length *not* including these two bytes

        syncMovementCommandBuffer[2] = 0x0;                                         // message counter. unused.
        syncMovementCommandBuffer[3] = 0x0;                                          // message counter. unused.

        syncMovementCommandBuffer[4] = EV3DirectCommand.DIRECT_COMMAND_NOREPLY.getByteValue();

        syncMovementCommandBuffer[5] = 0;
        syncMovementCommandBuffer[6] = 0;

        syncMovementCommandBuffer[7] = EV3Opcode.OUTPUT_SPEED.getByteValue();  // opcode

        syncMovementCommandBuffer[8] = 0;                                          // run on layer 0 (LAYER_0)
        syncMovementCommandBuffer[9] = (byte) motorsAsSingleByte;                  // motors   |D|C|B|A|
        //                      |-|-|-|-|
        //                      |0|1|1|0| = 0x06

        //set speed
        syncMovementCommandBuffer[10] = (byte) 0x81;                               // LC1 - one byte to follow
        syncMovementCommandBuffer[11] = TwosComplementConverter.convertIntToTwosComplement(speed)[0];


        int startMovementCommandBufferLength = 10;
        byte[] startMovementCommandBuffer = new byte[startMovementCommandBufferLength];

        startMovementCommandBuffer[0] = (byte) (startMovementCommandBufferLength - 2);    // command length (2 bytes)
        startMovementCommandBuffer[1] = 0x0;                                         // command length *not* including these two bytes

        startMovementCommandBuffer[2] = 0x0;                                         // message counter. unused.
        startMovementCommandBuffer[3] = 0x0;                                          // message counter. unused.

        startMovementCommandBuffer[4] = EV3DirectCommand.DIRECT_COMMAND_NOREPLY.getByteValue();

        startMovementCommandBuffer[5] = 0;
        startMovementCommandBuffer[6] = 0;

        startMovementCommandBuffer[7] = EV3Opcode.OUTPUT_START.getByteValue();  // opcode

        startMovementCommandBuffer[8] = 0;                                          // run on layer 0 (LAYER_0)
        startMovementCommandBuffer[9] = (byte) motorsAsSingleByte;                  // motors   |D|C|B|A|
        //                      |-|-|-|-|
        //                      |0|1|1|0| = 0x06

        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(syncMovementCommandBuffer);
        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(startMovementCommandBuffer);
    }

    public void stopRobotClaw() {
        startRobotClawMoving(0);
    }

    public void startPlayingTone(int frequency, int volume) {

        int playToneCommandBufferLength = 15;
        byte[] playToneCommandBuffer = new byte[playToneCommandBufferLength];

        playToneCommandBuffer[0] = (byte) (playToneCommandBufferLength - 2);    // command length (2 bytes)
        playToneCommandBuffer[1] = 0x0;                                         // command length *not* including these two bytes

        playToneCommandBuffer[2] = 0x0;                                         // message counter. unused.
        playToneCommandBuffer[3] = 0x0;                                          // message counter. unused.

        playToneCommandBuffer[4] = EV3DirectCommand.DIRECT_COMMAND_NOREPLY.getByteValue();

        playToneCommandBuffer[5] = 0;
        playToneCommandBuffer[6] = 0;

        playToneCommandBuffer[7] = EV3Opcode.PLAY_SOUND.getByteValue();  // opcode

        playToneCommandBuffer[8] = (byte) 0x01;                                          // Play a tone
        byte[] volumeAsBytes = TwosComplementConverter.convertIntToTwosComplement(volume);

        playToneCommandBuffer[9] = (byte) 0x81; // volume 0-100
        playToneCommandBuffer[10] = volumeAsBytes[0]; // volume 0-100
        playToneCommandBuffer[11] = (byte) 0x82;                               // LC2 - two bytes to follow
        byte[] frequencyAsBytes = TwosComplementConverter.convertIntToTwosComplement(frequency);
        playToneCommandBuffer[12] = frequencyAsBytes[0]; //frequency 250-10000
        playToneCommandBuffer[13] = frequencyAsBytes[1]; //frequency 250-10000
        playToneCommandBuffer[14] = 0x0; //duration 0 = infinite

        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(playToneCommandBuffer);
    }

    public void stopPlayingTone() {

        int playToneCommandBufferLength = 9;
        byte[] playToneCommandBuffer = new byte[playToneCommandBufferLength];

        playToneCommandBuffer[0] = (byte) (playToneCommandBufferLength - 2);    // command length (2 bytes)
        playToneCommandBuffer[1] = 0x0;                                         // command length *not* including these two bytes

        playToneCommandBuffer[2] = 0x0;                                         // message counter. unused.
        playToneCommandBuffer[3] = 0x0;                                          // message counter. unused.

        playToneCommandBuffer[4] = EV3DirectCommand.DIRECT_COMMAND_NOREPLY.getByteValue();

        playToneCommandBuffer[5] = 0;
        playToneCommandBuffer[6] = 0;

        playToneCommandBuffer[7] = EV3Opcode.PLAY_SOUND.getByteValue();  // opcode

        playToneCommandBuffer[8] = 0x00;                                          // Stop playing a tone

        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(playToneCommandBuffer);
    }

    public void playSoundFile(int volume) {
        String pathToSoundFile = "/home/root/lms2012/prjs/Sounds/Blip 4";
        byte[] filePathBytes = pathToSoundFile.getBytes();

        int playSoundFileCommandBufferLength = 12 + filePathBytes.length + 1;
        byte[] playSoundFileCommandBuffer = new byte[playSoundFileCommandBufferLength];

        playSoundFileCommandBuffer[0] = (byte) (playSoundFileCommandBufferLength - 2);    // command length (2 bytes)
        playSoundFileCommandBuffer[1] = 0x0;                                         // command length *not* including these two bytes

        playSoundFileCommandBuffer[2] = 0x0;                                         // message counter. unused.
        playSoundFileCommandBuffer[3] = 0x0;                                          // message counter. unused.

        playSoundFileCommandBuffer[4] = EV3DirectCommand.DIRECT_COMMAND_NOREPLY.getByteValue();

        playSoundFileCommandBuffer[5] = 0;
        playSoundFileCommandBuffer[6] = 0;

        playSoundFileCommandBuffer[7] = EV3Opcode.PLAY_SOUND.getByteValue();  // opcode

        playSoundFileCommandBuffer[8] = (byte) 0x02;                                          // Play a file
        byte[] volumeAsBytes = TwosComplementConverter.convertIntToTwosComplement(volume);

        playSoundFileCommandBuffer[9] = (byte) 0x81; // volume 0-100
        playSoundFileCommandBuffer[10] = volumeAsBytes[0]; // volume 0-100
        playSoundFileCommandBuffer[11] = (byte) 0x84;                               // LCS - zero-terminated string to follow
        System.arraycopy(filePathBytes, 0, playSoundFileCommandBuffer, 12, filePathBytes.length);
        playSoundFileCommandBuffer[playSoundFileCommandBufferLength - 1] = (byte) 0x00; //zero-terminate file path

        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(playSoundFileCommandBuffer);
    }

    public float readCentimetersUntilObstacle() throws IOException {
        byte messageCounterLow = (byte) 0x01;
        byte messageCounterHigh = (byte) 0x00;
        byte[] getSensorDistanceCommandBuffer = buildReadSensorCommand((byte) 0x00, (byte) 0x1e, (byte) 0x00, messageCounterLow, messageCounterHigh);

        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(getSensorDistanceCommandBuffer);
        byte[] distanceAsIeee754Float = BluetoothConnectionService.instance().readResponseFromBluetoothDevice(messageCounterLow, messageCounterHigh);

        return TwosComplementConverter.convertByteArrayToFloat(distanceAsIeee754Float);
    }

    public float readSurfaceBrightness() throws IOException {
        byte messageCounterLow = (byte) 0x02;
        byte messageCounterHigh = (byte) 0x00;
        byte[] getSurfaceBrightnessCommandBuffer = buildReadSensorCommand((byte) 0x03, (byte) 0x1d, (byte) 0x00, messageCounterLow, messageCounterHigh);

        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(getSurfaceBrightnessCommandBuffer);
        byte[] surfaceBrightnessAsIeee754Float = BluetoothConnectionService.instance().readResponseFromBluetoothDevice(messageCounterLow, messageCounterHigh);
        return TwosComplementConverter.convertByteArrayToFloat(surfaceBrightnessAsIeee754Float);

    }

    public float getPercentBatteryRemaining() throws IOException {
        int getPercentBatteryCommandBufferLength = 10;
        byte[] getPercentBatteryCommandBuffer = new byte[getPercentBatteryCommandBufferLength];

        getPercentBatteryCommandBuffer[0] = (byte) (getPercentBatteryCommandBufferLength - 2);    // command length (2 bytes)
        getPercentBatteryCommandBuffer[1] = 0x0;                                         // command length *not* including these two bytes

        byte messageCounterLow = (byte) 0x03;
        byte messageCounterHigh = (byte) 0x00;
        getPercentBatteryCommandBuffer[2] = messageCounterLow;                                         // message counter. unused.
        getPercentBatteryCommandBuffer[3] = messageCounterHigh;                                          // message counter. unused.

        getPercentBatteryCommandBuffer[4] = EV3DirectCommand.DIRECT_COMMAND_REPLY.getByteValue();

        getPercentBatteryCommandBuffer[5] = 0x04;
        getPercentBatteryCommandBuffer[6] = 0;

        getPercentBatteryCommandBuffer[7] = EV3Opcode.UI_READ.getByteValue();  // opcode

        getPercentBatteryCommandBuffer[8] = 0x12;                                          // read battery life as percentage
        getPercentBatteryCommandBuffer[9] = 0x60;                                          // not specified, but breaks without it???

        BluetoothConnectionService.instance().sendCommandToBluetoothDevice(getPercentBatteryCommandBuffer);

        byte[] batteryLifeAsInt = BluetoothConnectionService.instance().readResponseFromBluetoothDevice(messageCounterLow, messageCounterHigh);

        return (int) batteryLifeAsInt[0];
    }

    @NonNull
    private byte[] buildReadSensorCommand(byte port, byte type, byte mode, byte messageCounterLowByte, byte messageCounterHighByte) {
        int getSensorDistanceCommandBufferLength = 15;
        byte[] getSensorDistanceCommandBuffer = new byte[getSensorDistanceCommandBufferLength];

        getSensorDistanceCommandBuffer[0] = (byte) (getSensorDistanceCommandBufferLength - 2);    // command length (2 bytes)
        getSensorDistanceCommandBuffer[1] = 0x0;                                         // command length *not* including these two bytes

        getSensorDistanceCommandBuffer[2] = messageCounterLowByte;                                         // message counter.
        getSensorDistanceCommandBuffer[3] = messageCounterHighByte;                                          // message counter.

        getSensorDistanceCommandBuffer[4] = EV3DirectCommand.DIRECT_COMMAND_REPLY.getByteValue();

        getSensorDistanceCommandBuffer[5] = 0x04;
        getSensorDistanceCommandBuffer[6] = 0;

        getSensorDistanceCommandBuffer[7] = EV3Opcode.READ_SENSOR.getByteValue();  // opcode

        getSensorDistanceCommandBuffer[8] = 0x1D;                                          // read sensor in SI unit mode
        getSensorDistanceCommandBuffer[9] = 0x00;                                          // layer 0
        getSensorDistanceCommandBuffer[10] = port;                                          // port
        getSensorDistanceCommandBuffer[11] = type;                                          // sensor type. 0x1e = ultrasonic, 0x1d = color
        getSensorDistanceCommandBuffer[12] = mode;                                          // mode
        getSensorDistanceCommandBuffer[13] = 0x01;                                          // return 1 value
        getSensorDistanceCommandBuffer[14] = 0x60;                                          // offset???
        return getSensorDistanceCommandBuffer;
    }

}
