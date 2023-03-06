package com.example.t04_advancedrobodriving.services;

import com.example.t04_advancedrobodriving.ev3SystemCommands.EV3DirectCommand;
import com.example.t04_advancedrobodriving.ev3SystemCommands.EV3Motor;
import com.example.t04_advancedrobodriving.ev3SystemCommands.EV3Opcode;

import java.util.Set;

public class EV3ControllerService {

    private final BluetoothConnectionService bluetoothConnectionService;

    public EV3ControllerService(BluetoothConnectionService bluetoothConnectionService) {
        this.bluetoothConnectionService = bluetoothConnectionService;
    }

    public void moveRobotForwardFixedAmount(int speed ) {

        byte[] buffer = new byte[20];
        buffer[0] = (byte) (0x12);
        buffer[1] = 0;

        buffer[2] = 34;
        buffer[3] = 12;

        buffer[4] = EV3DirectCommand.DIRECT_COMMAND_NOREPLY.getByteValue();

        buffer[5] = 0;
        buffer[6] = 0;

        buffer[7] = EV3Opcode.OUTPUT_STEP_SPEED.getByteValue();
        buffer[8] = 0;

        buffer[9] = (byte) 0x06;

        buffer[10] = (byte) 0x81;
        buffer[11] = (byte) 0xCE;

        buffer[12] = 0;

        buffer[13] = (byte) 0x82;
        buffer[14] = (byte) 0x84;
        buffer[15] = (byte) 0x03;

        buffer[16] = (byte) 0x82;
        buffer[17] = (byte) 0xB4;
        buffer[18] = (byte) 0x00;

        buffer[19] = 1;
    }

    public void startRobotMovingForward(int speed){}

    public void startRobotMovingLeft(int speed){}

    public void startRobotMovingRight(int speed){}

    public void startRobotClawOpening(int speed){}

    public void startRobotClawClosing(int speed){}

    public void stopRobotWheels(){

    }

}
