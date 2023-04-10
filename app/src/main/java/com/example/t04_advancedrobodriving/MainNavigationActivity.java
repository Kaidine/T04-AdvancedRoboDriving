package com.example.t04_advancedrobodriving;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.t04_advancedrobodriving.databinding.ActivityMainNavigationBinding;
import com.example.t04_advancedrobodriving.services.BluetoothConnectionService;
import com.example.t04_advancedrobodriving.services.EV3ControllerService;


public class MainNavigationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainNavigationBinding binding = ActivityMainNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host)).getNavController();
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//
//    public static EV3ControllerService getRobotService(Context context, String robotName) {
//        if (robotServiceInstance == null) {
//            BluetoothConnectionService bluetoothConnectionService = new BluetoothConnectionService();
//            robotServiceInstance = new EV3ControllerService(bluetoothConnectionService);
//        }
//        return robotServiceInstance;
//    }
}