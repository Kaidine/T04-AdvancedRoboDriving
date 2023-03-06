package com.example.t04_advancedrobodriving;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.t04_advancedrobodriving.databinding.ActivityMainBinding;
import com.example.t04_advancedrobodriving.ev3SystemCommands.EV3Motor;
import com.example.t04_advancedrobodriving.services.BluetoothConnectionService;
import com.example.t04_advancedrobodriving.services.EV3ControllerService;
import com.example.t04_advancedrobodriving.systemServiceWrappers.ContextCompatWrapper;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private BluetoothConnectionService bluetoothConnectionService;
    private EV3ControllerService robotControllerService;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String robotName = "HIMMYNEUTRON";
        bluetoothConnectionService = new BluetoothConnectionService(this, robotName, new ContextCompatWrapper());

        robotControllerService = new EV3ControllerService(bluetoothConnectionService);

        binding.motorSpeedBarLabel.setText(String.valueOf(50));
        binding.clawSpeedBarLabel.setText(String.valueOf(50));
        binding.motorSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.motorSpeedBarLabel.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        binding.clawSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.clawSpeedBarLabel.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.moveForwardButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                binding.moveForwardButton.setBackgroundColor(getResources().getColor(R.color.teal_700));
                robotControllerService.startRobotMoving(binding.motorSpeedBar.getProgress());
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                binding.moveForwardButton.setBackgroundColor(getResources().getColor(R.color.purple_200));
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothConnectionService.disconnectFromDevice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_first:
                bluetoothConnectionService.requestBluetoothPermissions();
                return true;
            case R.id.menu_second:
                bluetoothConnectionService.connectToDevice();
                return true;
            case R.id.menu_third:
                bluetoothConnectionService.disconnectFromDevice();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}