package com.example.t04_advancedrobodriving.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.t04_advancedrobodriving.systemServiceWrappers.ContextCompatWrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BluetoothConnectionServiceTest {

    BluetoothConnectionService bluetoothConnectionService;
    AppCompatActivity mockAppCompatActivity;
    ContextCompatWrapper mockContextCompatWrapper;
    String expectedDeviceName;

    @BeforeEach
    void setup() {
        expectedDeviceName = "expectedDeviceName";
        mockAppCompatActivity = mock(AppCompatActivity.class);
        mockContextCompatWrapper = mock(ContextCompatWrapper.class);

        bluetoothConnectionService = new BluetoothConnectionService(
                mockAppCompatActivity,
                expectedDeviceName,
                mockContextCompatWrapper
        );
    }

    @Nested
    @DisplayName("checkBluetoothPermissions")
    public class TestCheckBluetoothPermissions {
        @Test
        void callsContextCompatWrapperCorrectly() {
            bluetoothConnectionService.checkBluetoothPermissions();

            verify(mockContextCompatWrapper).checkSelfPermission(mockAppCompatActivity, Manifest.permission.BLUETOOTH_SCAN);
            verify(mockContextCompatWrapper).checkSelfPermission(mockAppCompatActivity, Manifest.permission.BLUETOOTH_CONNECT);
        }

    }


}