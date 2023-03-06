package com.example.t04_advancedrobodriving.systemServiceWrappers;

import android.app.Activity;

import androidx.core.app.ActivityCompat;

public class ActivityCompatWrapper {

    public void requestPermissions(Activity activity, String[] permissions, int requestCode){
        ActivityCompat.requestPermissions(activity,permissions, requestCode);
    }
}
