package com.example.t04_advancedrobodriving.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnectionService {
    private static BluetoothConnectionService INSTANCE;


    private BluetoothSocket bluetoothSocket;
    private InputStream socketInputStream;
    private OutputStream socketOutputStream;


    private BluetoothConnectionService() {
    }

    public static BluetoothConnectionService instance() {
        if (INSTANCE == null) {
            INSTANCE = new BluetoothConnectionService();
        }
        return INSTANCE;
    }

    public boolean hasBluetoothPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean robotIsInDeviceList(Context context, String robotName) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (
                hasBluetoothPermission(context)
        ) {
            @SuppressLint("MissingPermission") Set<BluetoothDevice> pairedBluetoothDevices = bluetoothAdapter.getBondedDevices();

            @SuppressLint("MissingPermission") Optional<BluetoothDevice> optionalBluetoothDevice = pairedBluetoothDevices.stream()
                    .filter((BluetoothDevice device) -> device.getName().equalsIgnoreCase(robotName))
                    .findFirst();

            return optionalBluetoothDevice.isPresent();
        }
        return false;


    }


    @SuppressLint("MissingPermission")
    public boolean connectToDevice(String robotName) throws IOException {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        Set<BluetoothDevice> pairedBluetoothDevices = bluetoothAdapter.getBondedDevices();

        Optional<BluetoothDevice> optionalBluetoothDevice = pairedBluetoothDevices.stream()
                .filter((BluetoothDevice device) -> device.getName().equalsIgnoreCase(robotName))
                .findFirst();


        BluetoothDevice targetDevice = optionalBluetoothDevice.get();

        bluetoothSocket = targetDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        bluetoothSocket.connect();
        socketInputStream = bluetoothSocket.getInputStream();
        socketOutputStream = bluetoothSocket.getOutputStream();


        return true;
    }

    public void disconnectFromDevice() {
        if (bluetoothSocket != null) {
            try {
                if (socketInputStream != null) socketInputStream.close();
                if (socketOutputStream != null) socketOutputStream.close();
                bluetoothSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendCommandToBluetoothDevice(byte[] byteBuffer) {

        try {
            socketOutputStream.write(byteBuffer);
            socketOutputStream.flush();

        } catch (IOException ioException) {
            System.out.println("Failed to open outputStream for sending commands to device.");
        }
    }

    public byte[] readResponseFromBluetoothDevice() throws IOException {
        try {
            boolean successfullyReadResponse = false;
            byte[] responseSizeBuffer = new byte[2];
            byte[] responseBuffer = new byte[0];
            while (!successfullyReadResponse) {
                Thread.sleep(50);

                int bytesAvailable = 0;
                while (bytesAvailable == 0) {
                    //wait for response to be ready
                    Thread.sleep(50);
                    bytesAvailable = socketInputStream.available();
                }

                int bytesRead = socketInputStream.read(responseSizeBuffer, 0, 2);
                if (bytesRead != 2) {
                    throw new IOException("Invalid response size received.");
                }

                int responseSize = responseSizeBuffer[0] + (responseSizeBuffer[1] << 8);
                responseBuffer = new byte[responseSize];
                bytesRead = socketInputStream.read(responseBuffer);

                if (bytesRead != responseSize) {
                    throw new IOException("Response of Unexpected Size Received");
                }

                int sentCommand = responseBuffer[0] + (responseBuffer[1] << 8);
                if (responseBuffer[2] != 0x04) {
                    successfullyReadResponse = true;
                }
            }
            return Arrays.copyOfRange(responseBuffer, 3, responseBuffer.length);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isConnected() {
        if (bluetoothSocket == null) {
            return false;
        }
        return bluetoothSocket.isConnected();
    }
}
