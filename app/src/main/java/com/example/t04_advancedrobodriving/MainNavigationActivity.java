package com.example.t04_advancedrobodriving;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.t04_advancedrobodriving.databinding.ActivityMainNavigationBinding;


public class MainNavigationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainNavigationBinding binding = ActivityMainNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

}