package com.example.t04_advancedrobodriving;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.t04_advancedrobodriving.databinding.ActivityMainBinding;
import com.example.t04_advancedrobodriving.ev3SystemCommands.EV3Motor;
import com.example.t04_advancedrobodriving.services.BluetoothConnectionService;
import com.example.t04_advancedrobodriving.services.EV3ControllerService;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private BluetoothConnectionService bluetoothConnectionService;
    private EV3ControllerService robotControllerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String robotName = "HIMMYNEUTRON";
        bluetoothConnectionService = new BluetoothConnectionService(this, robotName);
        robotControllerService = new EV3ControllerService(bluetoothConnectionService);

        binding.beepButton.setOnClickListener(view -> robotControllerService.playTone());

        binding.moveForwardButton.setOnClickListener(view -> robotControllerService.moveMotorsFixedAmount(50, new EV3Motor[]{EV3Motor.B, EV3Motor.C}));

        binding.moveBackwardButton.setOnClickListener(view -> robotControllerService.moveMotorsFixedAmount(-50, new EV3Motor[]{EV3Motor.B, EV3Motor.C}));
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
//                cv_btDevice = cpf_locateInPairedBTList(CV_ROBOTNAME);
                return true;
            case R.id.menu_third:
                bluetoothConnectionService.connectToDevice();
                return true;
            case R.id.menu_fourth:
                robotControllerService.moveMotorsFixedAmount(50, new EV3Motor[]{EV3Motor.B, EV3Motor.C});
//                cpf_EV3MoveForward();
                return true;
            case R.id.menu_fifth:
                robotControllerService.moveMotorsFixedAmount(-50, new EV3Motor[]{EV3Motor.B, EV3Motor.C});
//                cpf_EV3PlayTone();
                return true;
            case R.id.menu_sixth:
                bluetoothConnectionService.disconnectFromDevice();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
//
//    private void cpf_checkBTPermissions() {
//        if (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
//            binding.vvTvOut1.setText("BLUETOOTH_SCAN already granted.\n");
//        } else {
//            binding.vvTvOut1.setText("BLUETOOTH_SCAN NOT granted.\n");
//        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
//            binding.vvTvOut2.setText("BLUETOOTH_CONNECT NOT granted.\n");
//        } else {
//            binding.vvTvOut2.setText("BLUETOOTH_CONNECT already granted.\n");
//        }
//    }
//
//    // https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/
//    private void cpf_requestBTPermissions() {
//        // We can give any value but unique for each permission.
//        final int BLUETOOTH_SCAN_CODE = 100;
//        final int BLUETOOTH_CONNECT_CODE = 101;
//
//        // Android version < 12, "android.permission.BLUETOOTH" just fine
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
//            Toast.makeText(MainActivity.this,
//                    "BLUETOOTH granted for earlier Android", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.BLUETOOTH_SCAN},
//                    BLUETOOTH_SCAN_CODE);
//        } else {
//            Toast.makeText(MainActivity.this,
//                    "BLUETOOTH_SCAN already granted", Toast.LENGTH_SHORT).show();
//        }
//
//        if (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
//                    BLUETOOTH_CONNECT_CODE);
//        } else {
//            Toast.makeText(MainActivity.this,
//                    "BLUETOOTH_CONNECT already granted", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Modify from chap14, pp390 findRobot()
//    private BluetoothDevice cpf_locateInPairedBTList(String name) {
//        BluetoothDevice lv_bd;
//        try {
//            cv_btInterface = BluetoothAdapter.getDefaultAdapter();
//            cv_pairedDevices = cv_btInterface.getBondedDevices();
//            Iterator<BluetoothDevice> lv_it = cv_pairedDevices.iterator();
//            while (lv_it.hasNext()) {
//                lv_bd = lv_it.next();
//                if (lv_bd.getName().equalsIgnoreCase(name)) {
//                    binding.vvTvOut1.setText(name + " is in paired list");
//                    return lv_bd;
//                }
//            }
//            binding.vvTvOut1.setText(name + " is NOT in paired list");
//        } catch (Exception e) {
//            binding.vvTvOut1.setText("Failed in findRobot() " + e.getMessage());
//        }
//        return null;
//    }
//
//    // Modify from chap14, pp391 connectToRobot()
//    private void cpf_connectToEV3(BluetoothDevice bd) {
//        try {
//            cv_btSocket = bd.createRfcommSocketToServiceRecord
//                    (UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
//            cv_btSocket.connect();
//
//            //// HERE
//            cv_is = cv_btSocket.getInputStream();
//            cv_os = cv_btSocket.getOutputStream();
//            binding.vvTvOut2.setText("Connect to " + bd.getName() + " at " + bd.getAddress());
//        } catch (Exception e) {
//            binding.vvTvOut2.setText("Error interacting with remote device [" +
//                    e.getMessage() + "]");
//        }
//    }
//
//    private void cpf_disconnFromEV3(BluetoothDevice bd) {
//        try {
//            cv_btSocket.close();
//            cv_is.close();
//            cv_os.close();
//            binding.vvTvOut2.setText(bd.getName() + " is disconnect ");
//        } catch (Exception e) {
//            binding.vvTvOut2.setText("Error in disconnect -> " + e.getMessage());
//        }
//    }


    // Communication Developer Kit Page 27
    // 4.2.2 Start motor B & C forward at power 50 for 3 rotation and braking at destination
    private void cpf_EV3MoveForward() {
        try {
            byte[] buffer = new byte[20];       // 0x12 command length

            buffer[0] = (byte) (20 - 2);
            buffer[1] = 0;

            buffer[2] = 34;
            buffer[3] = 12;

            buffer[4] = (byte) 0x80;

            buffer[5] = 0;
            buffer[6] = 0;

            //actual commands here
            buffer[7] = (byte) 0xae;    // opcode 0xae - opOUTPUT_STEP_SPEED
            buffer[8] = 0;              // LC0(LAYER_0)

            buffer[9] = (byte) 0x06;    // LC0(0x06) - motors   |D|C|B|A|
            //                      |-|-|-|-|
            //                      |0|1|1|0| = 0x06
            buffer[10] = (byte) 0x81;   // LC1 - speed, one byte to follow
            buffer[11] = (byte) 0x32;   // speed 50% (signed 2's complement - 0x32 = +50)

            buffer[12] = 0;             // no Step1 - full speed from start)

            buffer[13] = (byte) 0x82;   // LC2 - Step 2 (duration), 2 bytes to follow
            buffer[14] = (byte) 0x84;   // little endian - least significant bits of rotation
            buffer[15] = (byte) 0x03;   // total 0x0384 = 900 degrees

            buffer[16] = (byte) 0x82;   // LC2 - step 3 (duration), 2 bytes to follow
            buffer[17] = (byte) 0xB4;   //
            buffer[18] = (byte) 0x00;   // total 0x00b4 = 180 degrees

            buffer[19] = 1;             // LC0(1) - brake

//            cv_os.write(buffer);
//            cv_os.flush();
        } catch (Exception e) {
            binding.vvTvOut1.setText("Error in MoveForward(" + e.getMessage() + ")");
        }
    }// Communication Developer Kit Page 27

    // 4.2.2 Start motor B & C forward at power 50 for 3 rotation and braking at destination
    private void cpf_EV3MoveBackward() {
        try {
            byte[] buffer = new byte[20];       // 0x12 command length

            buffer[0] = (byte) (20 - 2);
            buffer[1] = 0;

            buffer[2] = 34;
            buffer[3] = 12;

            buffer[4] = (byte) 0x80;

            buffer[5] = 0;
            buffer[6] = 0;

            buffer[7] = (byte) 0xae;
            buffer[8] = 0;

            buffer[9] = (byte) 0x06;

            buffer[10] = (byte) 0x81;
            buffer[11] = (byte) 0xCE;

            buffer[12] = 0;

            buffer[13] = (byte) 0x82;
            buffer[14] = (byte) 0x84;
            buffer[15] = (byte) 0x03;

            buffer[16] = (byte) 0x82;
            buffer[17] = (byte) 0xB4;
            buffer[18] = (byte) 0x00;

            buffer[19] = 1;

//            cv_os.write(buffer);
//            cv_os.flush();
        } catch (Exception e) {
            binding.vvTvOut1.setText("Error in MoveForward(" + e.getMessage() + ")");
        }
    }

    // 4.2.5 Play a 1Kz tone at level 2 for 1 sec.
    private void cpf_EV3PlayTone() {
        try {
            byte[] buffer = new byte[17];       // 0x0f command length

            buffer[0] = (byte) (17 - 2);
            buffer[1] = 0;

            buffer[2] = 34;
            buffer[3] = 12;

            buffer[4] = (byte) 0x80;

            buffer[5] = 0;
            buffer[6] = 0;

            buffer[7] = (byte) 0x94;
            buffer[8] = 1;

            buffer[9] = (byte) 0x81;
            buffer[10] = (byte) 0x02;

            buffer[11] = (byte) 0x82;
            buffer[12] = (byte) 0xe8;
            buffer[13] = (byte) 0x03;

            buffer[14] = (byte) 0x82;
            buffer[15] = (byte) 0xe8;
            buffer[16] = (byte) 0x03;

//            cv_os.write(buffer);
//            cv_os.flush();
        } catch (Exception e) {
            binding.vvTvOut2.setText("Error in MoveForward(" + e.getMessage() + ")");
        }
    }
}