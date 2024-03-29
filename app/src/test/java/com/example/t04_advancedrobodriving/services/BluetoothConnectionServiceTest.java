package com.example.t04_advancedrobodriving.services;

import static org.mockito.Mockito.mock;

import androidx.appcompat.app.AppCompatActivity;

import com.example.t04_advancedrobodriving.systemServiceWrappers.ContextCompatWrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

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

//        bluetoothConnectionService = new BluetoothConnectionService(
//                mockAppCompatActivity,
//                expectedDeviceName
//        );
    }

    @Nested
    @DisplayName("checkBluetoothPermissions")
    public class TestCheckBluetoothPermissions {
//        @Test
//        void callsContextCompatWrapperCorrectly() {
//            if (ContextCompat.checkSelfPermission(null, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
//                ContextCompat.checkSelfPermission(null, Manifest.permission.BLUETOOTH_CONNECT);
//            }
//
//            verify(mockContextCompatWrapper).checkSelfPermission(mockAppCompatActivity, Manifest.permission.BLUETOOTH_SCAN);
//            verify(mockContextCompatWrapper).checkSelfPermission(mockAppCompatActivity, Manifest.permission.BLUETOOTH_CONNECT);
//        }

    }


}