package com.example.t04_advancedrobodriving.services;

import com.example.t04_advancedrobodriving.TwosComplementConverter;
import com.example.t04_advancedrobodriving.ev3SystemCommands.EV3DirectCommand;
import com.example.t04_advancedrobodriving.ev3SystemCommands.EV3Motor;
import com.example.t04_advancedrobodriving.ev3SystemCommands.EV3Opcode;

public class EV3ControllerService {

    private final BluetoothConnectionService bluetoothConnectionService;

    public EV3ControllerService(BluetoothConnectionService bluetoothConnectionService) {
        this.bluetoothConnectionService = bluetoothConnectionService;
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

        bluetoothConnectionService.sendCommandToBluetoothDevice(buffer);
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
        syncMovementCommandBuffer[11] = TwosComplementConverter.convertIntToTwosComplement(speed);

        // set turning ratio. 0 = matching.
        syncMovementCommandBuffer[12] = (byte) 0x0;                               // LC0 - constant (ratio = 0)

        // tach pulses (rotations), 0=infinite
        syncMovementCommandBuffer[13] = (byte) 0x0;                               // LC0 - constant (pulses = 0)

        //braking level, 0= float, 1 =brake
        syncMovementCommandBuffer[14] = (byte) 0x01;                               // LC0 - constant (brake)

        bluetoothConnectionService.sendCommandToBluetoothDevice(syncMovementCommandBuffer);
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
        syncMovementCommandBuffer[11] = TwosComplementConverter.convertIntToTwosComplement(speed);

        // set turning ratio. 0 = matching.
        syncMovementCommandBuffer[12] = (byte) 0x82;                               // LC0 - constant (ratio = -200)
        syncMovementCommandBuffer[13] = (byte) 0x38;
        syncMovementCommandBuffer[14] = (byte) 0xFF;

        // tach pulses (rotations), 0=infinite
        syncMovementCommandBuffer[15] = (byte) 0x0;                               // LC0 - constant (pulses = 0)

        //braking level, 0= float, 1 =brake
        syncMovementCommandBuffer[16] = (byte) 0x01;                               // LC0 - constant (brake)

        bluetoothConnectionService.sendCommandToBluetoothDevice(syncMovementCommandBuffer);
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
        syncMovementCommandBuffer[11] = TwosComplementConverter.convertIntToTwosComplement(speed);

        // set turning ratio. 0 = matching.
        syncMovementCommandBuffer[12] = (byte) 0x82;                               // LC0 - constant (ratio = -200)
        syncMovementCommandBuffer[13] = (byte) 0xc8;
        syncMovementCommandBuffer[14] = (byte) 0x00;

        // tach pulses (rotations), 0=infinite
        syncMovementCommandBuffer[15] = (byte) 0x0;                               // LC0 - constant (pulses = 0)

        //braking level, 0= float, 1 =brake
        syncMovementCommandBuffer[16] = (byte) 0x01;                               // LC0 - constant (brake)

        bluetoothConnectionService.sendCommandToBluetoothDevice(syncMovementCommandBuffer);
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
        syncMovementCommandBuffer[11] = TwosComplementConverter.convertIntToTwosComplement(speed);


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

        bluetoothConnectionService.sendCommandToBluetoothDevice(syncMovementCommandBuffer);
        bluetoothConnectionService.sendCommandToBluetoothDevice(startMovementCommandBuffer);
    }

    public void stopRobotClaw() {
        startRobotClawMoving(0);
    }


}
