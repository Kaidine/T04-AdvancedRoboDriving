package com.example.t04_advancedrobodriving.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.t04_advancedrobodriving.R;
import com.example.t04_advancedrobodriving.databinding.FragmentRobotConnectionBinding;
import com.example.t04_advancedrobodriving.services.BluetoothConnectionService;
import com.example.t04_advancedrobodriving.services.EV3ControllerService;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RobotConnectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotConnectionFragment extends Fragment {

    FragmentRobotConnectionBinding binding;
    private ThreadPoolExecutor pollingThreadPool;

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
        binding.setBatteryLifePercentage(null);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopPollingBatteryLife();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPollingBatteryLife();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateBatteryLife(null);
        startPollingBatteryLife();
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
                    makeToast("Failed to connect to device: " + e.getMessage());
                }
                if (!connectedToDevice) {
                    return;
                }
            }
            robotConnected();
            startPollingBatteryLife();

        });
    }

    private void disconnectFromRobot() {
        BluetoothConnectionService.instance().disconnectFromDevice();
        stopPollingBatteryLife();
        updateIndicators();
    }

    private void startPollingBatteryLife() {
        stopPollingBatteryLife();
        pollingThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        pollingThreadPool.submit(() -> {
            while (true) {
                pollBatteryLife();
                SystemClock.sleep(10000);
            }
        });
    }

    private void stopPollingBatteryLife() {
        if (pollingThreadPool != null) pollingThreadPool.shutdownNow();
        updateBatteryLife(null);
    }

    private void pollBatteryLife() {
        try {
            updateBatteryLife(
                    EV3ControllerService.instance()
                            .getPercentBatteryRemaining()
            );
        } catch (IOException e) {
            System.out.println("Error While Reading battery life: " + e.getMessage());
        }
    }

    private void updateBatteryLife(Float batteryLife) {
        binding.setBatteryLifePercentage(batteryLife);
    }

    private void makeToast(String message) {
        System.out.println("Connection failed: " + message);
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    private void robotConnected() {
        updateIndicators();
    }

    private void updateIndicators() {
        binding.setBluetoothPermissionsGranted(BluetoothConnectionService.instance().hasBluetoothPermission(getContext()));

        String robotName = String.valueOf(binding.robotNameField.getText());
        binding.setRobotIsPaired(BluetoothConnectionService.instance().robotIsInDeviceList(getContext(), robotName));
        binding.setRobotIsConnected(BluetoothConnectionService.instance().isConnected());
    }

}