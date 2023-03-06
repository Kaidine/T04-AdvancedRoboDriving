package com.example.t04_advancedrobodriving.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothStatusCodes;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.t04_advancedrobodriving.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnectionService implements ActivityCompat.OnRequestPermissionsResultCallback {
    private final AppCompatActivity activity;
    private final String targetDeviceName;


    private BluetoothSocket bluetoothSocket;

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
            return;
        }

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
            requestBluetoothPermissions();
            return;
        }

        Set<BluetoothDevice> pairedBluetoothDevices = bluetoothAdapter.getBondedDevices();

        Optional<BluetoothDevice> optionalBluetoothDevice = pairedBluetoothDevices.stream()
                .filter((BluetoothDevice device) -> device.getName().equalsIgnoreCase(targetDeviceName))
                .findFirst();


        if (!optionalBluetoothDevice.isPresent()) {
            Toast.makeText(activity, "Could not connect to device. Connect the bluetooth device named \"" + targetDeviceName + "\" and try again.", Toast.LENGTH_LONG).show();
            return;
        }

        BluetoothDevice targetDevice = optionalBluetoothDevice.get();
        try {
            bluetoothSocket = targetDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromDevice() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allPermissionsGranted = true;
        for (int grantResult : grantResults) {
            allPermissionsGranted = allPermissionsGranted && (grantResult == PackageManager.PERMISSION_GRANTED);
        }

        if (!allPermissionsGranted) {
            connectToDevice();
        }
    }
}
