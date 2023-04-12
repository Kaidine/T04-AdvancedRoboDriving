package com.example.t04_advancedrobodriving.fragments;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.t04_advancedrobodriving.databinding.FragmentRobotConnectionBinding;
import com.example.t04_advancedrobodriving.services.BluetoothConnectionService;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RobotConnectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotConnectionFragment extends Fragment {

    FragmentRobotConnectionBinding binding;

    public RobotConnectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RobotConnectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RobotConnectionFragment newInstance() {
        return new RobotConnectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRobotConnectionBinding.inflate(inflater, container, false);
        updateIndicators();
        binding.connectButton.setOnClickListener(view -> connectToRobot());
        binding.disconnectButton.setOnClickListener(view -> disconnectFromRobot());

        ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                }
        );
        activityResultLauncher.launch(new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT});

        return binding.getRoot();
    }

    private void connectToRobot() {
        updateIndicators();
        if (!binding.getBluetoothPermissionsGranted()) {
            Toast.makeText(getContext(), "Application requires bluetooth permissions!", Toast.LENGTH_SHORT).show();
            return;
        }

        String robotName = String.valueOf(binding.robotNameField.getText());
        if (!binding.getRobotIsPaired()) {
            Toast.makeText(getContext(), "Device not found in list; Pair the bluetooth device named \"" + robotName + "\" and try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newCachedThreadPool().submit(() -> {

            if (!BluetoothConnectionService.instance().isConnected()) {
                boolean connectedToDevice = false;
                try {
                    connectedToDevice = BluetoothConnectionService.instance().connectToDevice(robotName);
                } catch (IOException e) {
                    Toast.makeText(getContext(), "Failed to connect to device: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (!connectedToDevice) {
                    return;
                }
            }
            robotConnected();
        });
    }

    private void robotConnected() {
        updateIndicators();
    }

    private void disconnectFromRobot() {
        BluetoothConnectionService.instance().disconnectFromDevice();
        updateIndicators();
    }

    private void updateIndicators() {
        binding.setBluetoothPermissionsGranted(BluetoothConnectionService.instance().hasBluetoothPermission(getContext()));

        String robotName = String.valueOf(binding.robotNameField.getText());
        binding.setRobotIsPaired(BluetoothConnectionService.instance().robotIsInDeviceList(getContext(), robotName));
        binding.setRobotIsConnected(BluetoothConnectionService.instance().isConnected());
    }

}