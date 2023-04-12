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
    private ThreadPoolExecutor drivingThreadPool;
    private ThreadPoolExecutor pollingThreadPool;

    public boolean isContinueDistancePolling() {
        return continueDistancePolling;
    }

    public boolean isContinueSurfacePolling() {
        return continueSurfacePolling;
    }

    private boolean continueDistancePolling = true;
    private boolean continueSurfacePolling = true;


    public RobotSensorPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RobotSensorPageFragment.
     */
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
        // Inflate the layout for this fragment
        binding.setForwardSpeed(50);
        binding.setCentimetersToWall(255.0f);
        binding.setSurfaceBrightness(100.0f);
        binding.setBatteryRemainingPercentage(100.0);
        startDistancePolling();

        binding.driveToWallButton.setOnClickListener(view -> startDrivingUntilWallOnSeparateThread());
        binding.followLineButton.setOnClickListener(view -> startFollowingLineOnSeparateThread());
        binding.stopDrivingButton.setOnClickListener(view -> {
            stopPollingAndDriving();
            startDistancePolling();
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopPollingAndDriving();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPollingAndDriving();
    }

    @Override
    public void onResume() {
        super.onResume();
        startDistancePolling();
    }

    private void startDistancePolling() {
        stopPollingAndDriving();
        continueDistancePolling = true;
        pollingThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        pollingThreadPool.submit(() -> {
            while (isContinueDistancePolling()) {
                pollDistanceSensor();
                SystemClock.sleep(100);
            }
        });
    }

    private void startSurfacePolling() {
        stopPollingAndDriving();
        continueSurfacePolling = true;
        pollingThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        pollingThreadPool.submit(() -> {
            while (isContinueSurfacePolling()) {
                pollSurfaceSensor();
                SystemClock.sleep(100);
            }
        });
    }

    private void stopPollingAndDriving() {
        continueSurfacePolling = false;
        continueDistancePolling = false;
        if (pollingThreadPool != null) pollingThreadPool.shutdownNow();
        stopDrivingThreadAndHaltRobot();
    }

    private void stopDrivingThreadAndHaltRobot() {
        if (drivingThreadPool != null) drivingThreadPool.shutdownNow();
        EV3ControllerService.instance().stopRobotMoving();
    }


    private void startDrivingUntilWallOnSeparateThread() {
        stopPollingAndDriving();
        startDistancePolling();
        drivingThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        drivingThreadPool.submit(() -> {
            EV3ControllerService.instance().startRobotMoving(binding.getForwardSpeed());

            while (true) {
                if (binding.getCentimetersToWall() < 10.0f) {
                    break;
                }
                SystemClock.sleep(50);

            }
            EV3ControllerService.instance().stopRobotMoving();
        });
    }

    private void startFollowingLineOnSeparateThread() {
        stopPollingAndDriving();
        startSurfacePolling();
        drivingThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        drivingThreadPool.submit(() -> {
            boolean shouldSearchLeft = true;
            int searchCount = 0;
            boolean isSearching = false;

            while (true) {
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
        System.out.println("got distance: " + surfaceBrightness + " CM");
        binding.setSurfaceBrightness(surfaceBrightness);
    }

    private void pollDistanceSensor() {
        try {
            System.out.println("polling distance sensor...");
            updateCentimetersToWall(
                    EV3ControllerService.instance()
                            .readCentimetersUntilObstacle()
            );
        } catch (IOException e) {
            System.out.println("Error While Reading Distance: " + e.getMessage());
        }
    }

    private void pollSurfaceSensor() {
        try {
            System.out.println("polling surface sensor...");
            updateSurfaceBrightness(
                    EV3ControllerService.instance()
                            .readSurfaceBrightness()
            );
        } catch (IOException e) {
            System.out.println("Error While Reading Brightness: " + e.getMessage());
        }
    }
}