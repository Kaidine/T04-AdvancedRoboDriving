package com.example.t04_advancedrobodriving.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;

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

        when(mockContextCompatWrapper.checkSelfPermission(any(), any())).thenReturn(PackageManager.PERMISSION_GRANTED);

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

        @Test
        void returnsTrueIfBothPermissionsChecksReturnPermissionGranted() {
            assertTrue(bluetoothConnectionService.checkBluetoothPermissions());
        }

        @Test
        void returnsFalseIfBluetoothScanCheckFails() {
            when(mockContextCompatWrapper.checkSelfPermission(mockAppCompatActivity, Manifest.permission.BLUETOOTH_SCAN))
                    .thenReturn(PackageManager.PERMISSION_DENIED);

            assertFalse(bluetoothConnectionService.checkBluetoothPermissions());
        }

        @Test
        void returnsFalseIfBluetoothConnectCheckFails() {
            when(mockContextCompatWrapper.checkSelfPermission(mockAppCompatActivity, Manifest.permission.BLUETOOTH_CONNECT))
                    .thenReturn(PackageManager.PERMISSION_DENIED);

            assertFalse(bluetoothConnectionService.checkBluetoothPermissions());
        }


    }


}