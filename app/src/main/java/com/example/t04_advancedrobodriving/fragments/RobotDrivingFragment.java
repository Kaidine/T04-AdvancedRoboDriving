package com.example.t04_advancedrobodriving.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.t04_advancedrobodriving.R;
import com.example.t04_advancedrobodriving.databinding.FragmentRobotDrivingBinding;
import com.example.t04_advancedrobodriving.services.BluetoothConnectionService;
import com.example.t04_advancedrobodriving.services.EV3ControllerService;
import com.example.t04_advancedrobodriving.services.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RobotDrivingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotDrivingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    FragmentRobotDrivingBinding binding;
    private EV3ControllerService robotControllerService;

    public RobotDrivingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RobotDrivingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RobotDrivingFragment newInstance() {
        RobotDrivingFragment fragment = new RobotDrivingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String robotName = getString(R.string.robot_name);

        ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {

        BluetoothConnectionService bluetoothConnectionService = new BluetoothConnectionService(getContext(), robotName);
        robotControllerService = new EV3ControllerService(bluetoothConnectionService);
        });
        activityResultLauncher.launch(new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT});

//        robotControllerService = ServiceLocator.getInstance().getEv3ControllerServiceInstance();
        binding = FragmentRobotDrivingBinding.inflate(inflater, container, false);

        binding.motorSpeedBarLabel.setText(String.valueOf(50));
        binding.clawSpeedBarLabel.setText(String.valueOf(50));
        binding.motorSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.motorSpeedBarLabel.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        binding.clawSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.clawSpeedBarLabel.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.moveForwardButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                binding.moveForwardButton.setBackgroundColor(RobotDrivingFragment.this.getResources().getColor(R.color.teal_700));
                robotControllerService.startRobotMoving(binding.motorSpeedBar.getProgress());
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                binding.moveForwardButton.setBackgroundColor(RobotDrivingFragment.this.getResources().getColor(R.color.purple_200));
                robotControllerService.stopRobotMoving();
            }
            return false;
        });
        binding.moveBackwardButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                binding.moveBackwardButton.setBackgroundColor(getResources().getColor(R.color.teal_700));
                robotControllerService.startRobotMoving((binding.motorSpeedBar.getProgress() * -1));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                binding.moveBackwardButton.setBackgroundColor(getResources().getColor(R.color.purple_200));
                robotControllerService.stopRobotMoving();
            }
            return false;
        });
        binding.leftButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                binding.leftButton.setBackgroundColor(getResources().getColor(R.color.teal_700));
                robotControllerService.startRobotTurningLeft((binding.motorSpeedBar.getProgress()));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                binding.leftButton.setBackgroundColor(getResources().getColor(R.color.purple_200));
                robotControllerService.stopRobotMoving();
            }
            return false;
        });
        binding.rightButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                binding.rightButton.setBackgroundColor(getResources().getColor(R.color.teal_700));
                robotControllerService.startRobotTurningRight((binding.motorSpeedBar.getProgress()));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                binding.rightButton.setBackgroundColor(getResources().getColor(R.color.purple_200));
                robotControllerService.stopRobotMoving();
            }
            return false;
        });

        binding.beepButton.setOnClickListener(view -> robotControllerService.playTone());

        binding.openClawButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                binding.openClawButton.setBackgroundColor(getResources().getColor(R.color.teal_700));
                robotControllerService.startRobotClawMoving((binding.clawSpeedBar.getProgress()));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                binding.openClawButton.setBackgroundColor(getResources().getColor(R.color.purple_200));
                robotControllerService.stopRobotClaw();
            }
            return false;
        });

        binding.closeClawButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                binding.closeClawButton.setBackgroundColor(getResources().getColor(R.color.teal_700));
                robotControllerService.startRobotClawMoving((binding.clawSpeedBar.getProgress() * -1));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                binding.closeClawButton.setBackgroundColor(getResources().getColor(R.color.purple_200));
                robotControllerService.stopRobotClaw();
            }
            return false;
        });
        return binding.getRoot();
    }
}