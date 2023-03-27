package com.example.t04_advancedrobodriving.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnectionService {
    private final Context context;
    private final String targetDeviceName;


    private BluetoothSocket bluetoothSocket;
    private InputStream socketInputStream;
    private OutputStream socketOutputStream;

    public BluetoothConnectionService(Context context, String targetDeviceName) {
        this.context = context;
        this.targetDeviceName = targetDeviceName;

    }

    public boolean checkBluetoothPermissions() {
        return (
                ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        );
    }



    @SuppressLint("MissingPermission")
    public void connectToDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!checkBluetoothPermissions()) {
            displayStatusMessage("Bluetooth permissions not granted.");
            return;
        }

        Set<BluetoothDevice> pairedBluetoothDevices = bluetoothAdapter.getBondedDevices();

        Optional<BluetoothDevice> optionalBluetoothDevice = pairedBluetoothDevices.stream()
                .filter((BluetoothDevice device) -> device.getName().equalsIgnoreCase(targetDeviceName))
                .findFirst();


        if (!optionalBluetoothDevice.isPresent()) {
            displayStatusMessage("Device not found in list; Pair the bluetooth device named \"" + targetDeviceName + "\" and try again.");
            return;
        }

        BluetoothDevice targetDevice = optionalBluetoothDevice.get();
        try {
            displayStatusMessage("Establishing connection to \"" + targetDeviceName + "\"...");
            bluetoothSocket = targetDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            bluetoothSocket.connect();
            socketInputStream = bluetoothSocket.getInputStream();
            socketOutputStream = bluetoothSocket.getOutputStream();
            displayStatusMessage("Successfully connected to \"" + targetDeviceName + "\".");

        } catch (IOException e) {
            displayStatusMessage("Could not connect to device. Connect the bluetooth device named \"" + targetDeviceName + "\" and try again.");
            e.printStackTrace();
        }
    }

    public void disconnectFromDevice() {
        if (bluetoothSocket != null) {
            try {
                socketInputStream.close();
                socketOutputStream.close();
                bluetoothSocket.close();
                displayStatusMessage("Successfully disconnected from \"" + targetDeviceName + "\".");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendCommandToBluetoothDevice(byte[] byteBuffer) {

        if (!isConnected()){
            displayStatusMessage("Device \"" + targetDeviceName + "\" not connected. Attempting to establish connection.");
            connectToDevice();
        }

        try {
            socketOutputStream.write(byteBuffer);
            socketOutputStream.flush();

        } catch (IOException ioException) {
            displayStatusMessage("Failed to open outputStream for sending commands to device.");
        }
    }

    private boolean isConnected() {
        if (bluetoothSocket == null) {
            return false;
        }
        return bluetoothSocket.isConnected();
    }

    private void displayStatusMessage(String message) {
        System.out.println(message);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
