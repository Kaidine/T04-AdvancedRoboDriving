package com.example.t04_advancedrobodriving.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.t04_advancedrobodriving.databinding.FragmentRobotSensorPageBinding;
import com.example.t04_advancedrobodriving.services.EV3ControllerService;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RobotSensorPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotSensorPageFragment extends Fragment {

    private FragmentRobotSensorPageBinding binding;
    private ThreadPoolExecutor drivingAndPollingThreadPool;

    public RobotSensorPageFragment() {
        // Required empty public constructor
    }

    public static RobotSensorPageFragment newInstance() {
        return new RobotSensorPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRobotSensorPageBinding.inflate(inflater, container, false);

        binding.setForwardSpeed(50);
        binding.setCentimetersToWall(255.0f);
        binding.setSurfaceBrightness(100.0f);
        binding.setBatteryRemainingPercentage(100.0);

        binding.driveToWallButton.setOnClickListener(view -> driveToWallAndReturn());
        binding.followLineButton.setOnClickListener(view -> startFollowingLineOnSeparateThread());
        binding.stopDrivingButton.setOnClickListener(view -> haltRobot());
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        haltRobot();
    }

    @Override
    public void onPause() {
        super.onPause();
        haltRobot();
    }

    private void haltRobot() {
        if (drivingAndPollingThreadPool != null) drivingAndPollingThreadPool.shutdownNow();
        EV3ControllerService.instance().stopRobotMoving();
    }


    private void driveToWallAndReturn() {
        drivingAndPollingThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        drivingAndPollingThreadPool.submit(() -> {
            System.out.println("a");
            EV3ControllerService.instance().stopRobotMoving();
            float initialMotorRotationsInDegrees = 0f;
            float finalMotorRotationsInDegrees = 0f;

            try {
                initialMotorRotationsInDegrees = EV3ControllerService.instance().getMotorRotationInDegrees();
            } catch (IOException e) {
                System.out.println("failed to fetch initial motor rotations.");
                throw new RuntimeException(e);
            }

            driveToWall();

            try {
                finalMotorRotationsInDegrees = EV3ControllerService.instance().getMotorRotationInDegrees();
            } catch (IOException e) {
                System.out.println("failed to fetch initial motor rotations.");
                throw new RuntimeException(e);
            }

            int totalRotations = (int) (finalMotorRotationsInDegrees - initialMotorRotationsInDegrees);
            System.out.println("total rotations: " + totalRotations);

            EV3ControllerService.instance().moveRobotFixedDistance(-20, totalRotations);

        });
    }

    private void driveToWall() {
        EV3ControllerService.instance().startRobotMoving(20);
        try {
            while (true) {
                float centimetersToWall = EV3ControllerService.instance().readCentimetersUntilObstacle();
                updateCentimetersToWall(centimetersToWall);

                if (centimetersToWall < 10.0f) {
                    break;
                }
                SystemClock.sleep(100);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EV3ControllerService.instance().stopRobotMoving();
    }

    private void startFollowingLineOnSeparateThread() {
        drivingAndPollingThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        drivingAndPollingThreadPool.submit(() -> {
            boolean shouldSearchLeft = true;
            int searchCount = 0;
            boolean isSearching = false;

            while (true) {
                updateSurfaceBrightness(EV3ControllerService.instance().readSurfaceBrightness());
                if (binding.getSurfaceBrightness() > 10.0f) { //no line is present - find it
                    if (!isSearching) {
                        if (shouldSearchLeft) {
                            EV3ControllerService.instance().startRobotTurningLeft(5);

                        } else {
                            EV3ControllerService.instance().startRobotTurningRight(5);
                        }
                        isSearching = true;
                        searchCount++;
                    }
                } else { //line is present - follow it
                    if (isSearching) {
                        isSearching = false;
                        if (searchCount >= 2) {
                            searchCount = 0;
                            shouldSearchLeft = !shouldSearchLeft;
                        }
                    }
                    EV3ControllerService.instance().startRobotMoving(20);
                }
                SystemClock.sleep(50);
            }
        });
    }

    private void updateCentimetersToWall(float centimetersToWall) {
        binding.setCentimetersToWall(centimetersToWall);
    }

    private void updateSurfaceBrightness(float surfaceBrightness) {
        binding.setSurfaceBrightness(surfaceBrightness);
    }

}