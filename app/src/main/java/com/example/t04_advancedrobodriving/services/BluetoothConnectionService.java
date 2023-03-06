package com.example.t04_advancedrobodriving.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnectionService {
    private final AppCompatActivity activity;
    private final String targetDeviceName;


    private BluetoothSocket bluetoothSocket;
    private InputStream socketInputStream;
    private OutputStream socketOutputStream;

    public BluetoothConnectionService(AppCompatActivity activity, String targetDeviceName) {
        this.activity = activity;
        this.targetDeviceName = targetDeviceName;
    }

    public boolean checkBluetoothPermissions() {
        return (
                ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        );
    }

    public void requestBluetoothPermissions() {
        final int BLUETOOTH_SCAN_CODE = 100;
        final int BLUETOOTH_CONNECT_CODE = 101;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return;
        }
        if (checkBluetoothPermissions()) {
            Toast.makeText(activity, "Bluetooth permissions already granted.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(activity, "Requesting Bluetooth permissions.", Toast.LENGTH_SHORT).show();

        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.BLUETOOTH_SCAN},
                BLUETOOTH_SCAN_CODE);

        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                BLUETOOTH_CONNECT_CODE);
    }

    @SuppressLint("MissingPermission")
    public void connectToDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!checkBluetoothPermissions()) {
            Toast.makeText(activity, "Bluetooth permissions not granted; requesting permissions.", Toast.LENGTH_SHORT).show();
            requestBluetoothPermissions();
            return;
        }

        Set<BluetoothDevice> pairedBluetoothDevices = bluetoothAdapter.getBondedDevices();

        Optional<BluetoothDevice> optionalBluetoothDevice = pairedBluetoothDevices.stream()
                .filter((BluetoothDevice device) -> device.getName().equalsIgnoreCase(targetDeviceName))
                .findFirst();


        if (!optionalBluetoothDevice.isPresent()) {
            Toast.makeText(activity, "Device not found in list; Pair the bluetooth device named \"" + targetDeviceName + "\" and try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        BluetoothDevice targetDevice = optionalBluetoothDevice.get();
        try {
            Toast.makeText(activity, "Establishing connection to \"" + targetDeviceName + "\"...", Toast.LENGTH_SHORT).show();
            bluetoothSocket = targetDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            bluetoothSocket.connect();
            socketInputStream = bluetoothSocket.getInputStream();
            socketOutputStream = bluetoothSocket.getOutputStream();
            Toast.makeText(activity, "Successfully connected to \"" + targetDeviceName + "\".", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            System.out.println("failed to connect to device.");
            Toast.makeText(activity, "Could not connect to device. Connect the bluetooth device named \"" + targetDeviceName + "\" and try again.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void disconnectFromDevice() {
        if (bluetoothSocket != null) {
            try {
                socketInputStream.close();
                socketOutputStream.close();
                bluetoothSocket.close();
                Toast.makeText(activity, "Successfully disconnected from \"" + targetDeviceName + "\".", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendCommandToBluetoothDevice(byte[] byteBuffer) {
        if (!isConnected()){
            Toast.makeText(activity, "Device \"" + targetDeviceName + "\" not connected. Attempting to establish connection.", Toast.LENGTH_SHORT).show();
            connectToDevice();
        }

        try {
            socketOutputStream.write(byteBuffer);
            socketOutputStream.flush();

        } catch (IOException ioException) {
            Toast.makeText(activity, "Failed to open outputStream for sending commands to device.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        if (bluetoothSocket == null) {
            return false;
        }
        return bluetoothSocket.isConnected();
    }
}
